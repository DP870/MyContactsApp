
package com.Storage;

import java.util.HashMap;
import java.util.Map;
import com.Model.Account;

public class Storage {
    private static final Map<String, Account> db = new HashMap<>();

    public static void storeAccount(Account acc) {
        db.put(acc.getMail(), acc);
        System.out.println("Storage: Account for " + acc.getMail() + " added to HashMap.");
    }

    public static Account getAccount(String email) {
        return db.get(email);
    }
}
