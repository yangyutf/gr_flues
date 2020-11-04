package com.gr.his.utils.comm.demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: du
 * @DATE: 2020/7/27
 **/
public class HttpTest {


    @Test
    public void Test1() throws IOException {
        HttpUtils httpUtils=  HttpUtils.getInstance();
//        String url="https://dmsdev.gxgrtech.com.cn/api/fpqr/pub/deal";
        String url ="http://59.211.220.105:8008/apit/his/pub/deal";
        ECCodePayDemo ecCodePayDemo = new ECCodePayDemo();
        String json=ecCodePayDemo.getConnection();
        if(StringUtils.isNotBlank(json)){
            String result= httpUtils.postJson(url,json);
            System.out.println(result);
        }
    }


}
