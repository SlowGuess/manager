package com.example.common.enums;

public enum LikesModuleEnum {
    BLOG("博客");

    private String value;

    public String getValue(){
        return value;
    }

    LikesModuleEnum(String value){
        this.value = value;
    }
}
