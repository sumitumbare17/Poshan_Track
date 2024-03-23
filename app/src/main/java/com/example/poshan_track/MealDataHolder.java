package com.example.poshan_track;

public class MealDataHolder {

    private String name;
    private String cls;
    private boolean selected;

    public MealDataHolder() {}

    public MealDataHolder(String id, String name, String cls, boolean selected) {
        this.name = name;
        this.cls = cls;
        this.selected = selected;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}