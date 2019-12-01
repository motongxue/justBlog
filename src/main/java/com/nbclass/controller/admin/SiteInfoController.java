package com.nbclass.controller.admin;

import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.service.ConfigService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/site/info")
public class SiteInfoController {
    @Autowired
    private ConfigService configService;

    @PostMapping("/edit")
    public ResponseVo save(@RequestParam Map<String,String> map){
        try {
            for (String key : map.keySet()) {
                configService.updateByKey(key,map.get(key));
            }
            return ResponseUtil.success("保存网站信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("保存网站信息失败");
        }
    }
}
