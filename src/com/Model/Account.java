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

    public void setSecret(String newSecret) { this.secret = newSecret; }
    public void setDetail(AccountDetail newDetail) { this.detail = newDetail; }
    
    public abstract String getRank();

	
}
