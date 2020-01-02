package com.nbclass.framework.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eden
 * @version 1.0
 * @website https://www.nbclass.com
 * @date 2018/8/07 18:13
 * @since 1.0
 */
public class XssKillerUtil {
    private static final String[] WHITE_LIST = {"div","p", "strong", "pre", "code", "span", "blockquote", "em", "a",
            "br","br/","font","img","u","b","table","tbody","th","tr","td","ul","li","ol","h1","h2","h3","h4","h5"};
    private static String reg = null;
    private static String legalTags = null;

    static {
        StringBuilder regSb = new StringBuilder("<");
        StringBuilder tagsSb = new StringBuilder();
        for (String s : WHITE_LIST) {
            regSb.append("(?!").append(s).append(" )");
            tagsSb.append("<").append(s).append(">");
        }
        regSb.append("(?!/)[^>]*>");
        reg = regSb.toString();
        legalTags = tagsSb.toString();
    }

    /**
     * xss白名单验证
     *
     * @param xssStr
     * @return
     */
    public static boolean isValid(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return true;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(xssStr);
        while (matcher.find()) {
            String tag = matcher.group();
            if (!legalTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * xss白名单验证（Jsoup工具，效率较自己实现的那个有些差劲，见com.zyd.blog.util.XssKillerTest.test1()）
     *
     * @param xssStr
     * @return
     */
    public static boolean isValidByJsoup(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return true;
        }
        return Jsoup.isValid(xssStr, custome());
    }

    /**
     * 自定义的白名单
     *
     * @return
     */
    private static Whitelist custome() {
        return Whitelist.none().addTags("p", "strong", "pre", "code", "span", "blockquote", "em", "a",
                "br","br/","font","img","u","b","table","tbody","th","tr","td","ul","li","ol","h1","h2","h3","h4","h5").addAttributes("span", "class");
    }

    /**
     * 根据白名单，剔除多余的属性、标签
     *
     * @param xssStr
     * @return
     */
    public static String clean(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return "";
        }
        return Jsoup.clean(xssStr, custome());
    }

    public static String escape(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return "";
        }

        // TODO ...
        return xssStr;
    }



}