package com.qunincey.shop.dao;


import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class CookieTest {

    @Test
    public void get(){
        String pid="2";
        String pids="10-1-3";
        String[] split=pids.split("-");
        List<String> asList= Arrays.asList(split);
        LinkedList<String> linkedList=new LinkedList<>(asList);
//                    如果cookie保函该商品的id，则把该商品id移动到cookie前面
        if (linkedList.contains(pid)){
            linkedList.remove(pid);
            linkedList.add(pid);
        }else {
//          如果没有就加入
            linkedList.add(pid);
        }

        StringBuffer sb=new StringBuffer();
        for(int i=0;i<linkedList.size();i++){
            sb.append(linkedList.get(i));
            sb.append("-");
        }
        pids=sb.substring(0,sb.length()-1);
        System.out.println(pids);
        /*for (String string:
             pids) {
            System.out.println(string);
        }*/
    }

}
