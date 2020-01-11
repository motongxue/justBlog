package com.nbclass.service.impl;

import com.nbclass.enums.TemplateType;
import com.nbclass.framework.exception.MailException;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.service.ConfigService;
import com.nbclass.service.MailService;
import com.nbclass.vo.ConfigEmailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Properties;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private static final String templatePrefix = "email/";

    private static String from = "";

    @Autowired
    private ConfigService configService;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        try {
            JavaMailSenderImpl mailSender = getMailSender();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        } catch (MailException e) {
            throw new MailException(e.getMessage(),e);
        } catch (Exception e) {
            log.error("发送邮件失败:{}",e);
            throw new ZbException("发送邮件失败",e);
        }
    }

    /**
     * html邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        try {
            JavaMailSenderImpl mailSender = getMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MailException e) {
            throw new MailException(e.getMessage(),e);
        } catch (Exception e) {
            log.error("发送邮件失败:{}",e);
            throw new ZbException("发送邮件失败",e);
        }
    }

    /**
     * 带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        try {
            JavaMailSenderImpl mailSender = getMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MailException e) {
            throw new MailException(e.getMessage(),e);
        } catch (MessagingException e) {
            log.error("发送邮件失败:{}",e);
            throw new ZbException("发送邮件失败",e);
        }
    }

    @Override
    public void sendTemplateMail(TemplateType templateType, String to, String subject, Map<String,Object> map) {
        try {
            JavaMailSenderImpl mailSender = getMailSender();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            Context context = new Context();
            context.setVariables(map);
            String emailContent = templateEngine.process(templatePrefix+templateType.getName(), context); //指定模板路径
            messageHelper.setText(emailContent,true);
            mailSender.send(mimeMessage);
        }catch (MailException e) {
            throw new MailException(e.getMessage(),e);
        }catch (Exception e) {
            log.error("发送邮件失败:{}",e);
            throw new ZbException("发送邮件失败",e);
        }
    }

    private JavaMailSenderImpl getMailSender(){
        ConfigEmailVo emailConfig = configService.selectEmailConfig();
        if(emailConfig.getSetFlag()==0){
            throw new MailException("邮件服务未设置");
        }
        from = emailConfig.getFrom();
        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(emailConfig.getHost());
        jms.setUsername(emailConfig.getUsername());
        jms.setPassword(emailConfig.getPassword());
        jms.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.port", emailConfig.getPort());
        p.setProperty("mail.smtp.ssl.enable", "true");
        jms.setJavaMailProperties(p);
        return jms;
    }

}