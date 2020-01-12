package com.nbclass.service.impl;

import com.nbclass.enums.TemplateType;
import com.nbclass.service.EmailService;
import com.nbclass.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailService mailService;

    @Override
    public void sendVerificationCode(String email, String username, String code, String purpose) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("code",code);
        map.put("purpose",purpose);
        mailService.sendTemplateMail(TemplateType.VerificationCode,email,"验证码",map);
    }

    @Override
    public void sendCommentReply(String email, String username, String url, String sName, String content) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("url",url);
        map.put("sName",sName);
        map.put("content",content);
        mailService.sendTemplateMail(TemplateType.CommentReply,email,"您的评论有新的回复",map);
    }
}
