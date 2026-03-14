package com.Main;

import java.util.Scanner;
import com.Model.*;
import com.Storage.Storage;
import com.Validation.Regex; 
import com.Storage.PasswordHashing;

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
@version 2.0
 */

public class MyContactsApp {
    private static final Scanner sc=new Scanner(System.in);
    private static final PasswordHashing vaultSecurity=new PasswordHashing();
    private static Account activeClient=null;

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
            
            AccountDetail detail = new DetailBuilder().setHandle(uname).setInfo(b).setMobile(mob).build();
            Account newAcc = new AccountBuilder().setMail(m).setSecret(hashedPass).setDetail(detail).setCategory(lvl.toUpperCase()).build();

            Storage.storeAccount(newAcc);
            System.out.println("Success: User " + newAcc.getMail() + " registered.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // UC2: Authentication Logic
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
            System.out.println("\n--- User Dashboard (" + activeClient.getMail() + ") ---");
            System.out.println("1. View Profile");
            System.out.println("0. Log Out");
            System.out.print("Choice: ");
            
            int nav = sc.nextInt();
            if (nav == 1) {
                System.out.println("Profile Info: " + activeClient.getDetail().toString());
            } else if (nav == 0) {
                activeClient = null;
                System.out.println("Logged out.");
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("     CONTACT MANAGEMENT SYSTEM - UC 2       ");
        System.out.println("============================================");
        
        boolean isRunning = true;
        while(isRunning) {
            isRunning = displayMenu();
        }
    }
}









