// BusinessEntry.java
package com.Model;
public class BusinessEntry extends Entry {
    protected BusinessEntry(EntryBuilder b) { super(b); }
    @Override public String getEntryType() { return "BUSINESS"; }
}
