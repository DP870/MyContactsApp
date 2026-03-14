// EntryFactory.java
package com.Model;
public class EntryFactory {
    public static Entry createEntry(String type, EntryBuilder b) {
        if ("BUSINESS".equalsIgnoreCase(type)) return new BusinessEntry(b);
        return new PersonalEntry(b);
    }
}

