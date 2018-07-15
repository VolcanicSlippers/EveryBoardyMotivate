package com.karimtimer.everyboardymotivate.models;

public class Quote {


    private String desc;
    private String name;

    public Quote(){

    }
    public Quote(String desc, String name){
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
