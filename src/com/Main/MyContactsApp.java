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
@version 7.0
 */
public class MyContactsApp {
	private static final Scanner sc = new Scanner(System.in);
    private static final PasswordHashing vaultSecurity = new PasswordHashing();
    private static Account activeClient = null;

    public static void processRegistration() {
        System.out.println("\n--- Registration ---");
        System.out.print("Email: ");
        String m = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        System.out.print("Type (PREMIUM/FREE): ");
        String lvl = sc.next();
        System.out.print("Username: ");
        String uname = sc.next();
        System.out.print("Bio: ");
        String b = sc.next();
        System.out.print("Mobile: ");
        String mob = sc.next();

        try {
            Regex.checkMail(m);
            Regex.checkPass(p);
            Regex.checkCell(mob);

            String hashedPass = vaultSecurity.encryptKey(p);
            
            AccountDetail detail = new DetailBuilder()
                    .setHandle(uname)
                    .setInfo(b)
                    .setMobile(mob)
                    .build();

            Account newAcc = new AccountBuilder()
                    .setMail(m)
                    .setSecret(hashedPass)
                    .setDetail(detail)
                    .setCategory(lvl.toUpperCase())
                    .build();

            Storage.storeAccount(newAcc);
            System.out.println("Success: User " + newAcc.getMail() + " registered.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void processLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        Account found = Storage.getAccount(email);

        if (found != null) {
            String attemptHash = vaultSecurity.encryptKey(pass);
            if (found.getSecret().equals(attemptHash)) {
                activeClient = found;
                System.out.println("Login Successful! Welcome, " + activeClient.getDetail().getHandle());
            } else {
                System.out.println("Error: Wrong password.");
            }
        } else {
            System.out.println("Error: User does not exist.");
        }
    }

    public static void manageProfile() {
        System.out.println("\n--- Settings ---");
        System.out.println("1. Update Bio/Mobile");
        System.out.println("2. Change Password");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        
        if (choice == 1) {
            System.out.print("New Bio: ");
            String b = sc.next();
            System.out.print("New Mobile: ");
            String m = sc.next();
            activeClient.setDetail(new DetailBuilder().setHandle(activeClient.getDetail().getHandle()).setInfo(b).setMobile(m).build());
            System.out.println("Status: Updated.");
        } else if (choice == 2) {
            System.out.print("New Password: ");
            String newP = sc.next();
            try {
                Regex.checkPass(newP);
                activeClient.setSecret(vaultSecurity.encryptKey(newP));
                System.out.println("Status: Password changed.");
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        }
    }

    public static void createContact() {
        System.out.println("\n--- Add Contact ---");
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Phone: ");
        String phone = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Type (PERSONAL/BUSINESS): ");
        String type = sc.next();

        try {
            Regex.checkMail(email);
            Regex.checkCell(phone);
            activeClient.addEntry(new EntryBuilder().setName(name).setPhone(phone).setEmail(email).setType(type).build());
            System.out.println("System: Contact added.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public static void viewContacts() {
        if (activeClient.getContacts().isEmpty()) {
            System.out.println("System: No contacts saved yet.");
            return;
        }
        System.out.println("\n--- Your Contact List ---");
        int count = 1;
        for (Entry e : activeClient.getContacts()) {
            System.out.println(count + ". " + e.toString());
            count++;
        }
    }

    public static void editContact() {
        if (activeClient.getContacts().isEmpty()) {
            System.out.println("System: List is empty.");
            return;
        }
        viewContacts();
        System.out.print("Enter Contact Number to Edit: ");
        int index = sc.nextInt() - 1;

        if (index >= 0 && index < activeClient.getContacts().size()) {
            System.out.print("New Phone: ");
            String p = sc.next();
            System.out.print("New Email: ");
            String e = sc.next();
            System.out.print("New Type (PERSONAL/BUSINESS): ");
            String t = sc.next();

            try {
                Regex.checkMail(e);
                Regex.checkCell(p);
                Entry updated = new EntryBuilder().setName(activeClient.getContacts().get(index).getName()).setPhone(p).setEmail(e).setType(t).build();
                activeClient.updateEntry(index, updated);
                System.out.println("System: Updated.");
            } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
        } else {
            System.out.println("Error: Invalid index.");
        }
    }

    //Delete Contact
    public static void deleteContact() {
        if (activeClient.getContacts().isEmpty()) {
            System.out.println("System: List is empty. Nothing to delete.");
            return;
        }

        viewContacts();
        System.out.print("Enter Contact Number to Delete: ");
        int index = sc.nextInt() - 1;

        if (index >= 0 && index < activeClient.getContacts().size()) {
            String removedName = activeClient.getContacts().get(index).getName();
            activeClient.removeEntry(index);
            System.out.println("System: Contact '" + removedName + "' removed.");
        } else {
            System.out.println("Error: Invalid selection.");
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
            System.out.println("1. Profile 2. Settings 3. Add Contact 4. View Contacts 5. Edit Contact 6. Delete Contact 0. Log Out");
            System.out.print("Choice: ");
            
            int nav = sc.nextInt();
            switch(nav) {
                case 1 -> System.out.println("Info: " + activeClient.getDetail().toString());
                case 2 -> manageProfile();
                case 3 -> createContact();
                case 4 -> viewContacts();
                case 5 -> editContact();
                case 6 -> deleteContact(); // UC 7 Trigger
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
}