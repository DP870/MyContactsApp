package com.Model;

public abstract class Entry {
    private String name, phone, email;

    protected Entry(EntryBuilder b) {
        this.name = b.getName();
        this.phone = b.getPhone();
        this.email = b.getEmail();
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public abstract String getEntryType();

    @Override
    public String toString() {
        return "[" + getEntryType() + "] " + name + " | " + phone + " | " + email;
    }
}
