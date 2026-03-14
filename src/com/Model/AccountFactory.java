package com.Model;

public class AccountFactory {
    public static Account createAccount(String type, AccountBuilder b) {
        if ("PREMIUM".equalsIgnoreCase(type)) return new PremiumUser(b);
        return new FreeUser(b);
    }
}