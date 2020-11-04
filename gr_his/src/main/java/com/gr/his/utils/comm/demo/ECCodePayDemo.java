package com.gr.his.utils.comm.demo;

import com.alibaba.fastjson.JSON;
import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;


/**
 * @author: du
 * @DATE: 2020/7/28
 **/
public class ECCodePayDemo {

    private static String pr= "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM8BL2lu/xWx5Tfz17N8FPYJTjubh1XePioiph758JYW8gKcHle3fly3Jbowq1WMzE+RhFYx7pXNcnj0FMKWikniDAXms70kGzE0xwyDftmi6uj75dU5wO19g58N5JaZAynd9Pv4n9xPxmlp12jd+eNEp7tO7ulM56zHsSR5MvuxAgMBAAECgYAMP7+0d3L0r7AmnXE4F7GN1YMhl9RsSlhXWfGGh7eqyfTMHrzU5/C8P8IlwFeDGm/EjZHNH+tERgHuElNgKFRKa6eJNZxMI6RD0t5tSdmGyxKUwiE5j52bTpP2XHaYHLt5VWLjUxCuIswOhC6v1FloR8+XL3MjuyEvKzRQlCqLOQJBAO/bMeGqtinRpx8IcJ8p2cu67ZL8NbYkB0CILhYy/voPdXgXvvzyt0lYatS/20lDOCONgMoY2OPucukGmneNadsCQQDc7/ItmZz20/BUtG64cgC5p5wKHlJ8q6Aeqgz+wpXy8saxXnIPnLGrDeycZ/Tjt0l7JzvFPmtb16vqUZE8dORjAkEAlKJckt9a6ydETPLJ95fTo3A+8KriDfDdnZvGE3Oelg8mWDk+3Tj7Y7Qq35PUq92/2ChD+vjSzsxg4Q56gkNbqQJBAJlnAQAoaJP5FCW8IiRvA5HkjrN32k413DduWxeIhBTr/ImqygeNBM+AG4l9pMt3t82KsDWD4YcjeHp5qB4SC4ECQQCzYzETJC7pnfeXmRtCQy0rEr5hO2kVIdwNgrHVl8U3kh+A2BXag5RIfrUz8eJikxl0xysz2rUMS1JaRUGMGDAc";
    private List<String> signPrams=Arrays.asList(new String[]{"co","ty", "id","rd","tk","si"});
    private  Comparator<String> compareStr = new Comparator<String>() {
        public int compare(String str1,String str2) {
            return str1.compareTo(str2);
        }
    };

    public String getConnection(){
        GetConnection getConnection = new GetConnection();
        getConnection.setCo("1002");
        getConnection.setTy("T1001");
        getConnection.setId("1231231");
        getConnection.setRd("string");
        getConnection.setTk("string");
        getConnection.setSi("string");
        /*getConnection.setCo("1002");
        getConnection.setTy("T1001");
        getConnection.setId("123");
        getConnection.setRd("111");
        getConnection.setTk("123");*/
        try {
            String sign=getSign(getConnection,pr);
            System.out.println("sign="+sign);
            sign=URLEncoder.encode(sign,"UTF-8");
            getConnection.setSi(sign);
            return JSON.toJSONString(getConnection);
        }catch (Exception ex){
            return "";
        }

    }
    private String getSign(GetConnection getConnection, String pr) throws Exception {
        Map<String,String> params =new HashMap<>();
        params.put("co", getConnection.getCo());
        params.put("ty", getConnection.getTy());
        params.put("id", getConnection.getId());
        params.put("rd", getConnection.getRd());
        params.put("tk", getConnection.getTk());
        params.put("si", getConnection.getSi());
      return  getRSASign(params,pr);

    }
    public String getRSASign(Map<String,String> params,String pr) throws Exception {
        Set k1 = params.keySet();
        String[] keys = new String[k1.size()];
        k1.toArray(keys);
        Arrays.sort(keys, compareStr);
        String url = "";
        for (int i = 0; i < keys.length; i++) {
          if(signPrams.contains(keys[i])){
              url += (keys[i] + '=' + params.get(keys[i]) + '&');
          }
        }
        url = url.substring(0, url.length() - 1);
        url=md5(url);
        return  sign(pr,url);
    }

    // 签名
    public static String sign(String privateKeyStr, String message) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyStr.getBytes()); //Base64.decode(privateKeyStr.getBytes());
        PrivateKey priKey = (PrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Signature sign = Signature.getInstance(RsaSignUtils.VERIFY_MD5);
        sign.initSign(priKey);
        sign.update(message.getBytes("UTF-8"));
        BASE64Encoder decoder = new BASE64Encoder();
        return new String(decoder.encode(sign.sign()));
    }
    ///加密签名前，先对信息进行md5加密
    public final static String md5(String md5Str) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = md5Str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
