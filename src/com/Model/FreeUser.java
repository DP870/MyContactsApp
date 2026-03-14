package com.Model;

public class FreeUser extends Account {
    protected FreeUser(AccountBuilder builder) {
        super(builder);
    }
    @Override
    public String getRank() { return "FREE"; }
}
