package com.example.poshan_track;

public class Dataholder {

    String name,age,cls,phone,addr;

    public Dataholder(){}
    public Dataholder(String name, String age, String cls, String phone, String addr) {
        this.name = name;
        this.age = age;
        this.cls = cls;
        this.phone = phone;
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
