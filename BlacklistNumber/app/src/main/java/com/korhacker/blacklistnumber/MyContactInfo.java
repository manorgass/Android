package com.korhacker.blacklistnumber;

/**
 * Created by KorHacker on 2017-10-20.
 */

public class MyContactInfo {
    String name;
    String phoneNum;
    boolean isChecked;

    public MyContactInfo() {
        name = "";
        phoneNum = "";
        isChecked = false;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
