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
@version 3.0
 */

public class MyContactsApp {
	
	    private static final Scanner sc = new Scanner(System.in);
	    private static final PasswordHashing vaultSecurity = new PasswordHashing();
	    private static Account activeClient = null;

	    public static void manageProfile() {
	        System.out.println("\n--- Edit Profile Settings ---");
	        System.out.println("1. Update Details");
	        System.out.println("2. Change Password");
	        System.out.println("0. Back");
	        System.out.print("Choice: ");
	        
	        int choice = sc.nextInt();
	        
	        if (choice == 1) {
	            System.out.print("New Bio: ");
	            String b = sc.next();
	            System.out.print("New Mobile: ");
	            String m = sc.next();
	            
	            //Rebuilding the Detail object
	            AccountDetail updated = new DetailBuilder()
	                    .setHandle(activeClient.getDetail().getHandle())
	                    .setInfo(b)
	                    .setMobile(m)
	                    .build();
	            
	            activeClient.setDetail(updated);
	            System.out.println("Status: Profile updated successfully.");
	            
	        } else if (choice == 2) {
	            System.out.print("Enter New Password: ");
	            String newP = sc.next();
	            try {
	                Regex.checkPass(newP);
	                String hashed = vaultSecurity.encryptKey(newP);
	                activeClient.setSecret(hashed);
	                System.out.println("Status: Password changed successfully.");
	            } catch (Exception e) {
	                System.out.println("Error: " + e.getMessage());
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
	            System.out.println("0. Log Out");
	            System.out.print("Choice: ");
	            
	            int nav = sc.nextInt();
	            switch(nav) {
	                case 1 -> System.out.println("Profile: " + activeClient.getDetail().toString());
	                case 2 -> manageProfile();
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
	        System.out.println("     CONTACT MANAGEMENT SYSTEM - UC 3       ");
	        System.out.println("============================================");
	        
	        boolean isRunning = true;
	        while(isRunning) {
	            isRunning = displayMenu();
	        }
	    }
	}







