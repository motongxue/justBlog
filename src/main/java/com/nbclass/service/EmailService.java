package com.nbclass.service;

public interface EmailService {

    void sendVerificationCode(String email, String username,String code, String purpose);

    void sendCommentReply(String email, String username, String url, String sName, String content);
}
