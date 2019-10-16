package com.nbclass.enums;

public enum TemplateType {
    RegisterSuccess("registerSuccess", "注册成功邮件模板"),
    
    ;
    private String templateName;
    private String desc;

    TemplateType(String templateName, String desc){
        this.templateName = templateName;
        this.desc = desc;
    }


    public String getName() {
        return templateName;
    }

    public static TemplateType getByName(String name) {
        for(TemplateType type : TemplateType.values()){
            if(type.name().equalsIgnoreCase(name)){
                return type;
            }
        }
        return null;
    }
}
