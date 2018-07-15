package com.qunincey.shop.web.servlet;

import com.qunincey.shop.bean.Product;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.utils.FreemarkUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "test",urlPatterns = "/test")
public class test extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


      /*  Map<String, Object> root = new HashMap<>();
        ProductService proS=new ProductService();
        Product product=proS.findProductInfo("1");
        root.put("product",product);

        FreemarkUtil fu=new FreemarkUtil(req);
        Configuration configuration=fu.getCfg();

        Template template= configuration.getTemplate("test2.ftl");
        resp.setContentType("text/html;charset="+template.getEncoding());
        Writer writer=resp.getWriter();
        try {
            template.process(root,writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }*/


        try {
              /*
      * 创建磁盘文件项工厂
      * */
            DiskFileItemFactory factory=new DiskFileItemFactory();
        /*
        * 创建文件上传的核心类
        * */
            ServletFileUpload upload=new ServletFileUpload(factory);
            /*
            * 解析request 获得文件项集合
            * */
            List<FileItem> parseRequest=upload.parseRequest(req);
            /*
            * 遍历集合
            * */
            for (FileItem item:
                 parseRequest) {
               boolean formField= item.isFormField();//是否是普通表单项
                if (formField){
                    /*
                    * 普通表单项
                    * */
                    String filedName=item.getFieldName();
                    String filedValue=item.getString();
                    System.out.println(filedName+":"+filedValue );
                }else {
                    String fileName=item.getName();
                    InputStream in=item.getInputStream();
//                    将in中的数据拷贝到服务器
                    String path=this.getServletContext().getRealPath("upload");
                    OutputStream out=new FileOutputStream(path+"/"+fileName);
                    int len=0;
                    byte[] buffer =new byte[1024];
                    while ((len=in.read(buffer))>0){
                        out.write(buffer,0,len);
                    }
                    in.close();
                    out.close();
                }

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
