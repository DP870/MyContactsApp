// PersonalEntry.java
package com.Model;
public class PersonalEntry extends Entry {
    protected PersonalEntry(EntryBuilder b) { super(b); }
    @Override public String getEntryType() { return "PERSONAL"; }
}
