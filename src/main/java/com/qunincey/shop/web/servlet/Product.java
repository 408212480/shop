package com.qunincey.shop.web.servlet;


import com.google.gson.Gson;
import com.qunincey.shop.bean.Cart;
import com.qunincey.shop.bean.CartItem;
import com.qunincey.shop.bean.Category;
import com.qunincey.shop.bean.PageBean;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.utils.FreemarkUtil;
import com.qunincey.shop.utils.JedisPoolUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@WebServlet(name = "product",urlPatterns = "/product")
public class Product extends BaseServlet{

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

    public void addProductCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession  session=req.getSession();
        String pid=req.getParameter("pid");
        String buyNum=req.getParameter("buyNum");

        ProductService pro=new ProductService();
        com.qunincey.shop.bean.Product product=pro.findProductInfo(pid);
        /*
        * 计算当个商品的金额
        * */
        double subtotal=product.getShop_price()*Integer.parseInt(buyNum);
        CartItem cartItem=new CartItem(product,Integer.parseInt(buyNum),subtotal);

        Cart cart= (Cart) session.getAttribute("cart");
        if (cart==null){
            cart=new Cart();
        }
        //判断该商品是否已经在购物车中
        if (cart.getCartItems().containsKey(pid)){
            CartItem cartItem1=cart.getCartItems().get(pid);
            cartItem.setBuyNum(cartItem1.getBuyNum()+cartItem.getBuyNum());
            cart.getCartItems().put(product.getPid(),cartItem);
        }else {
            cart.getCartItems().put(product.getPid(),cartItem);
        }
        /*
         计算总金额
        */
        double totle=cart.getTotle()+cartItem.getSubTotal();
        cart.setTotle(totle);
        session.setAttribute("cart",cart);

       /* Map<String, Object> root = new HashMap<>();
        root.put("cart",cart);
        FreemarkUtil fu=new FreemarkUtil(req);
        Configuration cfg=fu.getCfg();
        Template template=cfg.getTemplate("cart.ftl");
        resp.setContentType("text/html;charset="+template.getEncoding());
        Writer writer=resp.getWriter();
        try {
            template.process(root,writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }*/
        resp.sendRedirect(req.getContextPath()+"/cart.jsp");

    }

    public void delProFormCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pid=req.getParameter("pid");
        HttpSession session=req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart!=null){
            Map<String,CartItem> cartItemMap=cart.getCartItems();
            cartItemMap.remove(pid);
            cart.setCartItems(cartItemMap);
            cart.setTotle(cart.getTotle()-cartItemMap.get(pid).getSubTotal());
        }
        session.setAttribute("cart",cart);

        resp.sendRedirect(req.getContextPath()+"/cart.jsp");

    }



}
