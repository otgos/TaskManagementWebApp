package com.taskmanager.upper.entity;

public enum Importance {
    IMPORTANT ("Important") ,
    NOTIMPORTANT ("Not Important");

    private final String type;

    Importance(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
