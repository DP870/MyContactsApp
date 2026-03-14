
package com.Model;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private String mail;
    private String secret;
    private AccountDetail detail;
    private List<Entry> contactList = new ArrayList<>();
    
    protected Account(AccountBuilder builder) {
        this.mail = builder.getMail();
        this.secret = builder.getSecret();
        this.detail = builder.getDetail();
    }
    
    public String getMail() { return mail; }
    public String getSecret() { return secret; }
    public AccountDetail getDetail() { return detail; }
    public List<Entry> getContacts() { return contactList; }

    public void setSecret(String newSecret) { this.secret = newSecret; }
    public void setDetail(AccountDetail newDetail) { this.detail = newDetail; }
    
    public void addEntry(Entry e) { this.contactList.add(e); }

    // UC6: Method to update an existing contact by index
    public void updateEntry(int index, Entry updatedEntry) {
        if (index >= 0 && index < contactList.size()) {
            contactList.set(index, updatedEntry);
        }
    }
    
    public abstract String getRank();
}

