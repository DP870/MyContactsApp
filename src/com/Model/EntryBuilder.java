package com.Model;
public class EntryBuilder {
    private String name, phone, email, type;

    public EntryBuilder setName(String n) { this.name = n; return this; }
    public EntryBuilder setPhone(String p) { this.phone = p; return this; }
    public EntryBuilder setEmail(String e) { this.email = e; return this; }
    public EntryBuilder setType(String t) { this.type = t; return this; }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public Entry build() { return EntryFactory.createEntry(this.type, this); }
}