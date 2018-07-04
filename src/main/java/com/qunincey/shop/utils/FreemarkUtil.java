package com.qunincey.shop.utils;


import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/*
* Freemarker的工具类
* */
public class FreemarkUtil {

    private static  Configuration cfg ;

    static String templatePath="/WEB-INF/freeMarker/";

    public FreemarkUtil(HttpServletRequest request){
        cfg=new Configuration(Configuration.VERSION_2_3_23);
        cfg.setServletContextForTemplateLoading(request.getSession().getServletContext(),templatePath);
        cfg.setDefaultEncoding("utf-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static Configuration getCfg() {
        return cfg;
    }
}
