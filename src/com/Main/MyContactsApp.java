package com.Main;

import java.util.Scanner;
import com.Model.*;
import com.Validation.*;
import com.Storage.*;
/*
 * MyContacts App Use Case Scenarios - Object Programming Approach

Problem Domain Overview
MyContacts App is a Java-based, console-driven application implemented use-case-wise to demonstrate object-oriented design, design patterns, and core Java concepts through a contact management system.

Features
This application consolidates multiple use cases (UC1 to UC10) into a single, cohesive user experience:

User Management: Registration and Authentication (UC1, UC2).
Profile: Manage personal details, passwords, and preferences (UC3).
Contacts:
Create and Manage Person & Organization contacts (UC4).
View details with Decorator enhancements (UpperCase, Masked Email) (UC5).
Edit contacts with Undo/Redo capabilities (Command Pattern) (UC6).
Delete contacts (Soft & Hard Delete) (UC7).
Groups: Create and manage contact groups with bulk operations (Composite Pattern) (UC8).
Search: Advanced search using Specification Pattern (Name, Phone, Email, Tag, etc.) (UC9).
Filter & Sort: Advanced filtering and sorting options (Strategy Pattern) (UC10).
Admin Features: User oversight and global search capabilities.

@author Dhruv
@version 10.0
 */
public class MyContactsApp {

	private static final Scanner sc = new Scanner(System.in);
    private static final PasswordHashing vaultSecurity = new PasswordHashing();
    private static Account activeClient = null;

    public static void processRegistration() {
        System.out.println("\n--- Registration ---");
        System.out.print("Email: "); String m = sc.next();
        System.out.print("Password: "); String p = sc.next();
        System.out.print("Type (PREMIUM/FREE): "); String lvl = sc.next();
        System.out.print("Username: "); String uname = sc.next();
        System.out.print("Bio: "); String b = sc.next();
        System.out.print("Mobile: "); String mob = sc.next();

        try {
            Regex.checkMail(m); Regex.checkPass(p); Regex.checkCell(mob);
            String hashedPass = vaultSecurity.encryptKey(p);
            AccountDetail detail = new DetailBuilder().setHandle(uname).setInfo(b).setMobile(mob).build();
            Account newAcc = new AccountBuilder().setMail(m).setSecret(hashedPass).setDetail(detail).setCategory(lvl.toUpperCase()).build();
            Storage.storeAccount(newAcc);
            System.out.println("Success: Registered.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public static void processLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: "); String email = sc.next();
        System.out.print("Password: "); String pass = sc.next();
        Account found = Storage.getAccount(email);
        if (found != null && found.getSecret().equals(vaultSecurity.encryptKey(pass))) {
            activeClient = found;
            System.out.println("Welcome, " + activeClient.getDetail().getHandle());
        } else { System.out.println("Invalid Credentials."); }
    }

    public static void manageProfile() {
        System.out.println("\n--- Settings --- 1. Update Bio 2. Change Pass");
        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.print("Bio: "); String b = sc.next();
            System.out.print("Mobile: "); String m = sc.next();
            activeClient.setDetail(new DetailBuilder().setHandle(activeClient.getDetail().getHandle()).setInfo(b).setMobile(m).build());
        } else if (choice == 2) {
            System.out.print("New Pass: "); String n = sc.next();
            try { Regex.checkPass(n); activeClient.setSecret(vaultSecurity.encryptKey(n)); } catch (Exception e) {}
        }
    }

    public static void createContact() {
        System.out.println("\n--- Add Contact ---");
        System.out.print("Name: "); String n = sc.next();
        System.out.print("Phone: "); String p = sc.next();
        System.out.print("Email: "); String e = sc.next();
        System.out.print("Type (PERSONAL/BUSINESS): "); String t = sc.next();
        try {
            Regex.checkMail(e); Regex.checkCell(p);
            activeClient.addEntry(new EntryBuilder().setName(n).setPhone(p).setEmail(e).setType(t).build());
        } catch (Exception ex) { System.out.println(ex.getMessage()); }
    }

    public static void viewContacts() {
        if (activeClient.getContacts().isEmpty()) return;
        int i = 1;
        for (Entry e : activeClient.getContacts()) System.out.println((i++) + ". " + e.toString());
    }

    public static void editContact() {
        viewContacts();
        System.out.print("Edit Index: ");
        int idx = sc.nextInt() - 1;
        if (idx >= 0 && idx < activeClient.getContacts().size()) {
            System.out.print("Phone: "); String p = sc.next();
            System.out.print("Email: "); String e = sc.next();
            activeClient.updateEntry(idx, new EntryBuilder().setName(activeClient.getContacts().get(idx).getName()).setPhone(p).setEmail(e).setType("PERSONAL").build());
        }
    }

    public static void deleteContact() {
        viewContacts();
        System.out.print("Delete Index: ");
        int idx = sc.nextInt() - 1;
        if (idx >= 0 && idx < activeClient.getContacts().size()) activeClient.removeEntry(idx);
    }

    public static void deleteAllContacts() { activeClient.clearAllEntries(); }

    public static void searchSystem() {
        System.out.print("Query: "); String q = sc.next();
        for (Entry e : activeClient.getContacts()) if (e.getName().contains(q)) System.out.println(e);
    }

    public static void filterContacts() {
        System.out.print("1. Personal 2. Business: ");
        String t = (sc.nextInt() == 1) ? "PERSONAL" : "BUSINESS";
        for (Entry e : activeClient.getContacts()) if (e.getEntryType().equals(t)) System.out.println(e);
    }

    public static void tagContact() {
        viewContacts();
        System.out.print("Index: "); int idx = sc.nextInt() - 1;
        if (idx >= 0 && idx < activeClient.getContacts().size()) {
            System.out.print("Tag: "); activeClient.getContacts().get(idx).addTag(sc.next());
        }
    }

    public static void editTags() {
        if (activeClient.getContacts().isEmpty()) return;
        viewContacts();
        System.out.print("Select Contact Number to edit tags: ");
        int idx = sc.nextInt() - 1;

        if (idx >= 0 && idx < activeClient.getContacts().size()) {
            Entry e = activeClient.getContacts().get(idx);
            System.out.println("Current Tags: " + e.getTags());
            System.out.println("1. Add another tag 2. Reset all tags");
            int opt = sc.nextInt();
            
            if (opt == 1) {
                System.out.print("Enter tag: ");
                e.addTag(sc.next());
                System.out.println("Tag added.");
            } else if (opt == 2) {
                e.clearTags();
                System.out.println("Tags cleared.");
            }
        }
    }


    public static boolean displayMenu() {
        if (activeClient == null) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. Sign Up\n2. Log In\n0. Exit");
            System.out.print("Choice: ");
            int nav = sc.nextInt();
            return switch(nav) {
                case 1 -> { processRegistration(); yield true; }
                case 2 -> { processLogin(); yield true; }
                case 0 -> false;
                default -> true;
            };
        } else {
            System.out.println("\n--- Dashboard (" + activeClient.getMail() + ") ---");
            System.out.println("1. Profile 2. Settings 3. Add Contact 4. View Contacts 5. Edit Contact 6. Delete Contact 7. Wipe All 8. Search Contacts 9. Filter 10.Edit Tags 0. Log Out");
            System.out.print("Choice: ");
            
            int nav = sc.nextInt();
            switch(nav) {
                case 1 -> System.out.println("Info: " + activeClient.getDetail().toString());
                case 2 -> manageProfile();
                case 3 -> createContact();
                case 4 -> viewContacts();
                case 5 -> editContact();
                case 6 -> deleteContact();
                case 7 -> deleteAllContacts();
                case 8 -> searchSystem(); 
                case 9 -> filterContacts();
                case 10 -> editTags();
                case 0 -> { activeClient = null; System.out.println("Logged out."); }
            }
            return true;
        }
    }


    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("     CONTACT MANAGEMENT SYSTEM");
        System.out.println("============================================");
        boolean isRunning = true;
        while(isRunning) { isRunning = displayMenu(); }
    }
}
