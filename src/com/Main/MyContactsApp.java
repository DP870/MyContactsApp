package com.Main;

import java.util.Scanner;
import com.Model.*;
import com.Storage.PasswordHashing;
import com.Storage.Storage;
import com.Validation.Regex;

public class MyContactsApp {
    private static final Scanner sc = new Scanner(System.in);
    private static final PasswordHashing vaultSecurity = new PasswordHashing();
    private static Account activeClient = null;

    public static void processRegistration() {
        System.out.println("--- Register New Account ---");
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
            
            System.out.println("Account created successfully for"+newAcc.getMail());
        } catch (Exception e) {
            System.out.println("Registration Error: "+e.getMessage());
        }
    }

    public static boolean displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In (UC2)");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
        
        int nav = sc.nextInt();

        return switch(nav) {
            case 1 -> {
                processRegistration();
                yield true;
            }
            case 2 -> {
                System.out.println("Login module is part of UC2.");
                yield true;
            }
            case 0 -> {
                System.out.println("Shutting down...");
                yield false;
            }
            default -> true;
        };
    }

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("     CONTACT MANAGEMENT SYSTEM - UC 1");
        System.out.println("============================================");
        
        boolean isRunning = true;
        while(isRunning) {
            isRunning = displayMenu();
        }
    }
}













