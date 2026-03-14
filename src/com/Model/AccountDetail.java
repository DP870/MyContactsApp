package com.Model;

public class AccountDetail {
    private String handle, info, mobile;

    protected AccountDetail(DetailBuilder b) {
        this.handle = b.getHandle();
        this.info = b.getInfo();
        this.mobile = b.getMobile();
    }

    public String getHandle() { return handle; }
    @Override
    public String toString() {
        return handle + " | " + info + " | " + mobile;
    }
}
