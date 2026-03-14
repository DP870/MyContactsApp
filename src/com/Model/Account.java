package com.Model;

public abstract class Account {
    private String mail;
    private String secret;
    private AccountDetail detail;
    
    protected Account(AccountBuilder builder) {
        this.mail = builder.getMail();
        this.secret = builder.getSecret();
        this.detail = builder.getDetail();
    }
    
    public String getMail() { return mail; }
    public String getSecret() { return secret; }
    public AccountDetail getDetail() { return detail; }
    public abstract String getRank();
}
