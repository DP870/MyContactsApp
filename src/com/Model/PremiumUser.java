
package com.Model;

public class PremiumUser extends Account {
    protected PremiumUser(AccountBuilder builder) {
        super(builder);
    }
    @Override
    public String getRank() { return "PREMIUM"; }
}
