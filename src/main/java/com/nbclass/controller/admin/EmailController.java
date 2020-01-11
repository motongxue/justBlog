package com.nbclass.controller.admin;

import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.service.MailService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/email")
public class EmailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    @AccessToken
    public ResponseVo send(String to, String subject, String content){
        mailService.sendSimpleMail(to, subject, content);
        return ResponseUtil.success("发送邮件成功");
    }
}
