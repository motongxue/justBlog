package com.nbclass.framework.util;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class QQUtil {
    private static String getQQJsonStr(Long qqId) {
        StringBuilder jsonString = new StringBuilder();
        URLConnection connection;
        try {
            URL urlObject = new URL("https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?get_nick=1&uins="+qqId);
            connection = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonString.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString.toString().substring("portraitCallBack(".length(),jsonString.length()-1);
    }

    public static QQ getQqInfo(Long qqId) {
        Map<String, String[]> map = GsonUtil.fromJson(getQQJsonStr(qqId), new TypeToken<Map<String, String[]>>(){}.getType());
        String[] qqInfo =  map.get(String.valueOf(qqId));
        QQ qq = new QQ();
        qq.setId(qqId);
        qq.setName(qqInfo[6]);
        qq.setAvatar(qqInfo[0]);
        return qq;
    }

}