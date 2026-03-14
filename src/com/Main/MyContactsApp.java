package com.Main;

import java.util.Scanner;
import com.Model.*;
import com.Validation.*;
import com.Storage.*;

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
                System.out.println("Login Successful! Welcome,"+activeClient.getDetail().getHandle());
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

    /**
     * UC 5: View and Search Contacts
     */
    public static void viewContacts() {
        if (activeClient.getContacts().isEmpty()) {
            System.out.println("System: No contacts saved yet.");
            return;
        }

        System.out.println("\n--- Contact Directory ---");
        System.out.println("1. View All Contacts");
        System.out.println("2. Search by Name");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        int choice = sc.nextInt();

        if (choice == 1) {
            System.out.println("\nListing all saved contacts:");
            for (Entry e : activeClient.getContacts()) {
                System.out.println(e.toString());
            }
        } else if (choice == 2) {
            System.out.print("Enter name to search: ");
            String target = sc.next();
            boolean found = false;
            for (Entry e : activeClient.getContacts()) {
                if (e.getName().equalsIgnoreCase(target)) {
                    System.out.println("Result: " + e.toString());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("System: No contact found with name " + target);
            }
        }
    }

    public static boolean displayMenu() {
        if (activeClient == null) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("0. Exit");
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
            System.out.println("1. View Profile");
            System.out.println("2. Manage Settings");
            System.out.println("3. Add New Contact");
            System.out.println("4. View Saved Contacts");
            System.out.println("0. Log Out");
            System.out.print("Choice: ");
            
            int nav = sc.nextInt();
            switch(nav) {
                case 1 -> System.out.println("Profile: " + activeClient.getDetail().toString());
                case 2 -> manageProfile();
                case 3 -> createContact();
                case 4 -> viewContacts(); 
                case 0 -> {
                    activeClient = null;
                    System.out.println("Logged out.");
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("     CONTACT MANAGEMENT SYSTEM - UC 5");
        System.out.println("============================================");
        
        boolean isRunning = true;
        while(isRunning) {
            isRunning = displayMenu();
        }
    }
}



