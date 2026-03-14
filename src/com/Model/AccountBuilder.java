
package com.Model;

public class AccountBuilder {
    private String mail, secret, category;
    private AccountDetail detail;

    public AccountBuilder setMail(String m) { this.mail = m; return this; }
    public AccountBuilder setSecret(String s) { this.secret = s; return this; }
    public AccountBuilder setDetail(AccountDetail d) { this.detail = d; return this; }
    public AccountBuilder setCategory(String c) { this.category = c; return this; }

    public String getMail() { return mail; }
    public String getSecret() { return secret; }
    public AccountDetail getDetail() { return detail; }

    public Account build() {
        return AccountFactory.createAccount(this.category, this);
    }
}
