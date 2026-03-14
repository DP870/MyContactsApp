package com.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Entry {
    private String name, phone, email;
    private List<String> tags = new ArrayList<>();

    protected Entry(EntryBuilder b) {
        this.name = b.getName();
        this.phone = b.getPhone();
        this.email = b.getEmail();
  }
   public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
   public void addTag(String t) { this.tags.add(t.toUpperCase()); }
    public List<String> getTags() { return tags; }
  public void clearTags() { this.tags.clear(); }
    public abstract String getEntryType();
   @Override
    public String toString() {
        String base = "[" + getEntryType() + "] " + name + " | " + phone + " | " + email;
        if (!tags.isEmpty()) {
            base += " | Tags: " + tags.toString();
        }
        return base;
    }
}