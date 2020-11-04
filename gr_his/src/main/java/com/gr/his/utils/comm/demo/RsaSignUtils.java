package com.gr.his.utils.comm.demo;

import org.apache.logging.log4j.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class RsaSignUtils {
    private static Logger logger = LoggerFactory.getLogger(RsaSignUtils.class);
    public final static String VERIFY_MD5 = "MD5withRSA";
//    public final static String VERIFY_ARITHMETIC = "MD5withRSA";

    private static Comparator<String> compareStr = new Comparator<String>() {
        public int compare(String str1,String str2) {
            return str1.compareTo(str2);
        }
    };

    /**
     * 生成请求参数的签名
     * @param paramMeters 签名的参数
     * @param pk 公钥
     * @Param sign 签名
     * @return 是否验签成功
     */

    public static  boolean verifySign(String co,String ty,String id,String rd,String tk,
                               String si, String pk, Map<String,String> paramMeters) {
        try {
            paramMeters.put("co", co);
            paramMeters.put("ty", ty);
            paramMeters.put("id", id);
            paramMeters.put("rd", rd);
            paramMeters.put("tk", tk);
        } catch (Exception e) {
            return false;
        }
        return verify(paramMeters, pk, RsaSignUtils.VERIFY_MD5, si);
    }
    public static boolean verify(Map<String, String> params, String pk, String alg, String sign) {
        Set k1 = params.keySet();
        String[] keys = new String[k1.size()];
        k1.toArray(keys);
        Arrays.sort(keys, compareStr);
        String url = "";
        for (int i = 0; i < keys.length; i++) {
            url += (keys[i] + '=' + params.get(keys[i]) + '&');
        }

        url = url.substring(0, url.length() - 1);
        try {
            //获取加密算法
            Signature verifySign = Signature.getInstance(alg);
           //获取公钥开始
            byte[] encodedKey = Base64.decode(pk.getBytes("UTF-8"));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = kf.generatePublic(new X509EncodedKeySpec(encodedKey));
            verifySign.initVerify(pubKey);
            //获取公钥结束
            //本地待验签名，先进行MD5加密
            url=md5(url);
            byte[] data =  url.getBytes("utf-8");
            verifySign.update(data);
            //解码签名
            final BASE64Decoder decoder = new BASE64Decoder();
            ///私钥加密签名
            return verifySign.verify(decoder.decodeBuffer(sign));
        } catch (Exception ex) {
            logger.error("RsaSignUtils.sign Exception");
        }
        return false;
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

    // 签名
    public static String sign(String privateKeyStr, String message) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKeyStr.getBytes());
        PrivateKey priKey = (PrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Signature sign = Signature.getInstance(RsaSignUtils.VERIFY_MD5);
        sign.initSign(priKey);
        message =md5(message) ;
        sign.update(message.getBytes("UTF-8"));
        BASE64Encoder decoder = new BASE64Encoder();
        return new String(decoder.encode(sign.sign()));
    }


    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decode(publicKey.getBytes());
        PublicKey pubKey = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        final BASE64Encoder decoder = new BASE64Encoder();
        String outStr = decoder.encode(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * 分段加密
     * @param input
     * @param rsaPublicKey
     * @return
     */
    public static String rsaEncrypt(String input, String rsaPublicKey) {
        String result = "";
        try {
            // 将Base64编码后的公钥转换成PublicKey对象
            byte[] buffer = Base64.decode(rsaPublicKey.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            // 加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] inputArray = input.getBytes("UTF-8");
            int inputLength = inputArray.length;
            System.out.println("加密字节数：" + inputLength);
            // 最大加密字节数，超出最大字节数需要分组加密
            int MAX_ENCRYPT_BLOCK = 117;
            // 标识
            int offSet = 0;
            byte[] resultBytes = {};
            byte[] cache = {};
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                    offSet += MAX_ENCRYPT_BLOCK;
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                    offSet = inputLength;
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
            }
            final BASE64Encoder decoder = new BASE64Encoder();
            result =decoder.encode(resultBytes);
        } catch (Exception e) {
            System.out.println("rsaEncrypt error:" + e.getMessage());
        }
        System.out.println("加密的结果：" + result);
        return result;
    }
    /**
     * 解密
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKey.getBytes());
        PrivateKey priKey = (PrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 分段解码
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String rsaDecrypt(String str, String privateKey) throws Exception {
        byte[] dectyptedText = null;
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKey.getBytes());
        PrivateKey priKey = (PrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        try {
           //秘钥初始化时加入制定默认的算法库
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
           //使用第三方算法加密库 BC
            final Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            //分段
            int inputLen = inputByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(inputByte, offSet, 128);
                } else {
                    cache = cipher.doFinal(inputByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            dectyptedText = out.toByteArray();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String(dectyptedText);
    }

    /**
     * 获取排序信息
     * @param params
     * @return
     */
    public static String getSignContent(Map<String, String> params){
        String signContent ="";
        ArrayList akeys = new ArrayList(params.keySet());
        akeys.sort(compareStr);
        for (int i = 0; i < akeys.size(); i++) {
            signContent += (akeys.get(i).toString() + '=' + params.get(akeys.get(i)) + '&');
        }
        signContent = signContent.substring(0, signContent.length() - 1);
        return signContent;
    };

    public static String getSi(String privateKey,ECCodePayRequstBaseDto dto) throws Exception{
        Map<String,String> map = new HashMap<>();
        map.put("ty",dto.getTy());
        map.put("id",dto.getId());
        map.put("rd",dto.getRd());
        map.put("tk",dto.getTk());
        return  RsaSignUtils.sign(privateKey,RsaSignUtils.getSignContent(map));
    }

}
