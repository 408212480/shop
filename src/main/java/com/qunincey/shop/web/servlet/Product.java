package com.qunincey.shop.web.servlet;


import com.google.gson.Gson;
import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.PageBean;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "product",urlPatterns = "/product")
public class Product extends HttpServlet{


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String methodName=req.getParameter("method");
        switch (methodName){
            case "CategoryList":
                CategoryList(req,resp);
                break;
            case "ProductListByid":
                ProductListByid(req,resp);
                break;
            case "ProductInfo":
                ProductInfo(req,resp);
                break;
            case "indexProduct":
                indexProduct(req,resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    /*
    * 获取商品类别
    * */
    public void CategoryList(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        Jedis jedis= JedisPoolUtils.getJedis();
        String categoryListjson=jedis.get("categoryList");
        if (categoryListjson==null){
            ProductService proS=new ProductService();
            //        查找分类
            List<Category> category=proS.findCategroy();
            Gson gson=new Gson();
            categoryListjson=gson.toJson(category);
            jedis.set("categoryListjson",categoryListjson);
        }
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(categoryListjson);
    }

    /*
    * 查看首页商品
    * */
    public void indexProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ProductService proS=new ProductService();
//        查找热门商品
        List<com.qunincey.shop.bean.Product> HotProduct=proS.findHotProduct();
//        查找最新商品
        List<com.qunincey.shop.bean.Product> NewProduct=proS.findNewProduct();



        req.setAttribute("HotProduct",HotProduct);
        req.setAttribute("NewProduct",NewProduct);
        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    /*
    * 获取商品列表
    * */
    public void ProductListByid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cid= Integer.parseInt(req.getParameter("cid"));
        String currentPage= req.getParameter("currentPage");
        if (currentPage==null){
            currentPage="1";
        }
        int currentCount=12;
        ProductService psd=new ProductService();
        PageBean pageBean=psd.findProductByCid(cid, Integer.parseInt(currentPage),currentCount);
        req.setAttribute("pageBean",pageBean);
        req.setAttribute("cid",cid);
        req.setAttribute("currentPage",currentPage);


        Cookie[] cookies= req.getCookies();
        ArrayList<com.qunincey.shop.bean.Product> historyProduct=new ArrayList<>();
        if (cookies!=null) {
            for (Cookie cookie :
                    cookies) {
                if ("pids".equals(cookie.getName())) {
                    String pids = cookie.getValue();
                    String[] split = pids.split("-");
                    for (String history :
                            split) {
                        ProductService pro = new ProductService();
                        historyProduct.add(pro.findProductInfo(history));
                    }
                }

            }
        }

        req.setAttribute("historyProduct",historyProduct);
        req.getRequestDispatcher("/product_list.jsp").forward(req,resp);
    }

    /*
    * 获取商品详细信息
    * */
    public void ProductInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid=req.getParameter("pid");
        ProductService proS=new ProductService();
        com.qunincey.shop.bean.Product product=proS.findProductInfo(pid);
        req.setAttribute("product",product);

        //获得客户端携带的cookie
        String pids=pid;
        Cookie[] cookies=req.getCookies();
        if (cookies!=null){
            for (Cookie cookie:cookies
                    ) {
                if ("pids".equals(cookie.getName())){
                    pids=cookie.getValue();
//                   cookie处理
                    String[] split=pids.split("-");
                    List<String> asList= Arrays.asList(split);
                    LinkedList<String> linkedList=new LinkedList<>(asList);
//                    如果cookie保函该商品的id，则把该商品id移动到cookie前面
                    if (linkedList.contains(pid)){
                        linkedList.remove(pid);
                        linkedList.add(pid);
                    }else {
//                        如果没有就加入
                        linkedList.add(pid);
                    }
//                    循环加入
                    StringBuffer sb=new StringBuffer();
                    for(int i=0;i<linkedList.size();i++){
                        sb.append(linkedList.get(i));
                        sb.append("-");
                    }
                    pids=sb.substring(0,sb.length()-1);
                }

            }
        }
        Cookie cookie_pids=new Cookie("pids",pids);
        resp.addCookie(cookie_pids);
        req.getRequestDispatcher("/product_info.jsp").forward(req,resp);
    }



}
