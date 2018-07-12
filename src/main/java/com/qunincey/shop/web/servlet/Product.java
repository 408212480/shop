package com.qunincey.shop.web.servlet;


import com.google.gson.Gson;
import com.qunincey.shop.bean.*;
import com.qunincey.shop.bean.User;
import com.qunincey.shop.service.ProductService;
import com.qunincey.shop.service.UserService;
import com.qunincey.shop.utils.CommonsUtiles;
import com.qunincey.shop.utils.JedisPoolUtils;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@WebServlet(name = "product",urlPatterns = "/product")
public class Product extends BaseServlet {


    /*
    *
    * 查询用户订单
    * */
    public void myOrders(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session=req.getSession();
        User user= (User) session.getAttribute("user");
        /*
          判断用户是否登录
        */
        if (user==null){
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
            return;
        }

        //该用户的所有的订单
        ProductService productService=new ProductService();
        List<Order> Orderlist=productService.findAllOrder(user.getUid());
        List<Map<String,Object>> OderItemlist=null;
        if (Orderlist!=null){
            for (Order order:
                    Orderlist) {
                OderItemlist=productService.findAllOrderItem(order.getOid());
                for (Map<String,Object> map:
                        OderItemlist ) {
                /*
                * 封装OderItem
                * */
                    int count= (int) map.get("count");
                    double subtotal= (double) map.get("subtotal");
                    OrderItem orderItem=new OrderItem();
                    orderItem.setCount(count);
                    orderItem.setSubtotal(subtotal);
                /*
                * 封装product
                * */
                    String pimage= (String) map.get("pimage");
                    String pname= (String) map.get("pname");
                    double shop_price= (double) map.get("shop_price");
                    com.qunincey.shop.bean.Product product= new com.qunincey.shop.bean.Product();
                    product.setPimage(pimage);
                    product.setPname(pname);
                    product.setShop_price(shop_price);
                    orderItem.setProduct(product);
                /*
                * 封装order
                * */
                    List<OrderItem> list=new ArrayList<>();
                    list.add(orderItem);
                    order.setOrderItems(list);
                }

            }
        }else {

        }

        req.setAttribute("userOrder",Orderlist);
        req.getRequestDispatcher("/order_list.jsp").forward(req,resp);

    }


    /*
    * 确认表单
    * */

    public void confirmOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException{

        /*
        * 更新收货人信息
        * */
        Map<String,String[]> parameterMap=req.getParameterMap();
        Order order=new Order();
        try {
            BeanUtils.populate(order,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        ProductService proS=new ProductService();
        proS.updateOrderAdrr(order);


    }

    /*
    * 提交订单
    *
    * */
    public void submitOrder(HttpServletRequest req,HttpServletResponse resp) throws IOException {

        HttpSession session=req.getSession();
        User user= (User) session.getAttribute("user");
        /*
          判断用户是否登录
        */
        if (user==null){
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
            return;
        }


        Order order =new Order();
        /* private String oid;//该订单的订单号*/
         order.setOid(CommonsUtiles.getUUID());
        /*  private Date  ordertime;//下单时间*/
        order.setOrdertime(new Date());
        //private double total;//该订单的总金额
        Cart cart= (Cart)session.getAttribute("cart");
        order.setTotal(cart.getTotle());
        //private int state;//订单支付状态1 付款 0 未付款
        order.setState(0);
        //private String address;//地址
        order.setAddress(null);
        //private String name;//姓名
        order.setName(null);
        //private String telephone;//电话
        order.setTelephone(null);
        //private User user;*/
        order.setUser(user);
        /* List<OrderItem> orderItems=new ArrayList<OrderItem>();*/
        Map<String,CartItem> cartItem=cart.getCartItems();
        for (Map.Entry<String,CartItem> entry:
             cartItem.entrySet()) {
            CartItem cartItem1=entry.getValue();
            OrderItem orderItem=new OrderItem();
            orderItem.setItemid(CommonsUtiles.getUUID());
            orderItem.setCount(cartItem1.getBuyNum());
            orderItem.setProduct(cartItem1.getProduct());
            orderItem.setSubtotal(cartItem1.getSubTotal());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        //order对象分装完毕
        ProductService proS=new ProductService();
        proS.submitOrder(order);

        session.setAttribute("order",order);

        resp.sendRedirect(req.getContextPath()+"/order_info.jsp");




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
        resp.sendRedirect(req.getContextPath()+"/cart.jsp");

    }

    /*
    * 删除购物车
    * */

    public void delProFormCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pid=req.getParameter("pid");
        HttpSession session=req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart!=null){
            Map<String,CartItem> cartItemMap=cart.getCartItems();
            cart.setTotle(cart.getTotle()-cartItemMap.get(pid).getSubTotal());
            cartItemMap.remove(pid);
            cart.setCartItems(cartItemMap);
        }
        session.setAttribute("cart",cart);

        resp.sendRedirect(req.getContextPath()+"/cart.jsp");

    }







}
