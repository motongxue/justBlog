package com.nbclass.enums;

public enum TemplateType {
    ResetPassword("resetPassword", "重置密码"),
    CommentAuditReply("commentAuditReply", "评论审核回复"),
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
