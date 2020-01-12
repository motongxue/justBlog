package com.nbclass.enums;

public enum TemplateType {
    VerificationCode("verificationCode", "重置密码"),
    CommentReply("CommentReply", "评论回复"),
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
    public String getDesc() {
        return desc;
    }

}
