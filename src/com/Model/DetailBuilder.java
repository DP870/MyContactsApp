

package com.Model;

public class DetailBuilder {
    private String handle, info, mobile;

    public DetailBuilder setHandle(String h) { this.handle = h; return this; }
    public DetailBuilder setInfo(String i) { this.info = i; return this; }
    public DetailBuilder setMobile(String m) { this.mobile = m; return this; }

    public String getHandle() { return handle; }
    public String getInfo() { return info; }
    public String getMobile() { return mobile; }

    public AccountDetail build() { return new AccountDetail(this); }
}
