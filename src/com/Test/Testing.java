package com.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Model.*;
import com.Storage.*;
import com.Validation.*;
import com.Main.*;

import java.util.*;


public class Testing {

    private PasswordHashing hasher;

    @BeforeEach
    void setUp() {
        hasher = new PasswordHashing();
    }

    @Test
    void testUC1_UC2_AccountCreationAndLogin() throws Exception {
        
        String email = "dhruv@example.com";
        String rawPass = "SecurePass123";
        Regex.checkMail(email);
        Regex.checkPass(rawPass);
        String hashed = hasher.encryptKey(rawPass);

        
        AccountDetail detail = new DetailBuilder()
                .setHandle("Dhruv")
                .setInfo("Software Dev")
                .setMobile("+919876543210")
                .build();

        Account user = new AccountBuilder()
                .setMail(email)
                .setSecret(hashed)
                .setDetail(detail)
                .setCategory("PREMIUM")
                .build();

        Storage.storeAccount(user);

        // UC2: Authentication Retrieval
        Account retrieved = Storage.getAccount(email);
        assertNotNull(retrieved);
        assertEquals(hashed, retrieved.getSecret());
        assertEquals("PREMIUM", retrieved.getRank());
    }

    @Test
    void testUC3_ProfileManagement() {
        AccountDetail oldDetail = new DetailBuilder().setHandle("User1").setInfo("Old Bio").setMobile("111").build();
        Account user = new AccountBuilder().setMail("u@u.com").setSecret("hash").setDetail(oldDetail).setCategory("FREE").build();

        // Update Profile
        AccountDetail newDetail = new DetailBuilder()
                .setHandle("User1") // keep handle
                .setInfo("New Updated Bio")
                .setMobile("999")
                .build();
        
        user.setDetail(newDetail);
        assertEquals("New Updated Bio", user.getDetail().toString().split(" \\| ")[1]);
    }

    @Test
    void testUC4_UC7_UC8_ContactLifecycle() {
        Account user = new AccountBuilder().setMail("test@test.com").setSecret("pw").setCategory("FREE")
                .setDetail(new DetailBuilder().setHandle("H").build()).build();

        //UC4:Create Contact
        Entry contact = new EntryBuilder()
                .setName("Alice")
                .setPhone("12345")
                .setEmail("alice@mail.com")
                .setType("PERSONAL")
                .build();
        
        user.addEntry(contact);
        assertEquals(1, user.getContacts().size());

        //UC7:Delete Contact
        user.removeEntry(0);
        assertTrue(user.getContacts().isEmpty());

        //UC8:Clear All
        user.addEntry(contact);
        user.addEntry(contact);
        user.clearAllEntries();
        assertEquals(0, user.getContacts().size());
    }

    @Test
    void testUC10_PolymorphicFiltering() {
        Entry p = new EntryBuilder().setName("P").setType("PERSONAL").build();
        Entry b = new EntryBuilder().setName("B").setType("BUSINESS").build();

        assertEquals("PERSONAL", p.getEntryType());
        assertEquals("BUSINESS", b.getEntryType());
        assertNotEquals(p.getClass(), b.getClass());
    }

    @Test
    void testUC11_UC12_TaggingSystem() {
        Entry contact = new EntryBuilder().setName("Bob").setType("PERSONAL").build();

        //UC11:Add Tag
        contact.addTag("Work");
        contact.addTag("Urgent");
        assertTrue(contact.getTags().contains("WORK"));

        //UC12:Clear/Edit Tags
        contact.clearTags();
        assertEquals(0, contact.getTags().size());
    }

    @Test
    void testValidationExceptions() {
        //Test that Regex correctly throws exceptions for bad data
        assertThrows(Exception.class, () -> Regex.checkMail("bad-email"));
        assertThrows(Exception.class, () -> Regex.checkPass("short"));
    }
}
