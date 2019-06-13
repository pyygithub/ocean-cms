package cn.com.thtf.utils;

import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 非对称加密
 */
public class RSAUtil {

    private static final Logger log = LoggerFactory.getLogger(RSAUtil.class);

	//非对称密钥算法
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    //公钥 key
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥 key
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() {
        try {
            //实例化密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            //初始化密钥生成器
            keyPairGenerator.initialize(KEY_SIZE);
            //生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //甲方公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            //甲方私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //将密钥存储在map中
            Map<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (Exception e) {
            log.error("### 初始化密钥对失败 e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }


    /**
     * 私钥加密
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key){
        try {
            //取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("### 私钥加密失败 e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key){
        try {
            //实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //初始化公钥
            //密钥材料转换
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
            //产生公钥
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

            //数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("### 公钥加密失败 e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 私钥解密
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) {
        try {
            //取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("### 私钥解密失败 e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 公钥解密
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        try {
            //实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //初始化公钥
            //密钥材料转换
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
            //产生公钥
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            //数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            log.error("### 公钥解密失败 e={}###", e.getMessage());
            throw new CustomException(ResultCode.FAIL);
        }
    }

    /**
     * 取得私钥
     * @return byte[] 私钥
     */
    public static String getPrivateKey() {
        Map<String, Object> keyMap = RSAUtil.initKey();
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 取得公钥
     * @return byte[] 公钥
     */
    public static String getPublicKey(){
        Map<String, Object> keyMap = RSAUtil.initKey();
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 获取甲方加密数据
     * @param ip
     * @param privateKey
     * @return
     */
    public static String getSecret(String ip, String privateKey) {
        String str = ip + "|" + System.currentTimeMillis();
        byte[] code = RSAUtil.encryptByPrivateKey(str.getBytes(), Base64.decodeBase64(privateKey));
        return Base64.encodeBase64String(code);
    }


    public static void main(String[] args) throws Exception {
        //初始化密钥
        //生成密钥对
       Map<String, Object> keyMap = RSAUtil.initKey();
        //公钥
//        byte[] publicKey = RSAUtil.getPublicKey(keyMap);
//        //私钥
//        byte[] privateKey = RSAUtil.getPrivateKey(keyMap);
//        System.out.println("公钥：/n = " + Base64.encodeBase64String(publicKey));
//        System.out.println("私钥：/n = " + Base64.encodeBase64String(privateKey));

/*        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        System.out.println("/n===========甲方向乙方发送加密数据==============");
        System.out.println("原文:" + str);*/
        //甲方进行数据的加密
        //私钥
    	String str = "10.10.50.149" + "|" +System.currentTimeMillis();
        String pri = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAjbDKh3FwKSciMasUg+nWsChQ6Yu7HtngcXMuW5BtwTUxqtW0AJKm8mJJa5Y+Bs43+sYgcSPJOqZTPcFqErOCfwIDAQABAkAUjvND5bS1c+UlOcJ3RWWOF7ttt4WF+2zyaRjv1ivmnX7W8FfiE2I29eUVezD6J4ia/z3iAAB30EMMPf+Q6QsxAiEAzLCBvqjriAnC5ZjFBRkDFKnmQ3bXMoCYCO070ipe8mkCIQCxNXhqVWJavyj1n0gLC3KZMUW2OwyjAhQ+Zsj1AXxgpwIgCxGgcob/9aNRfsj8HoJzwDJP2WYbzQdXMWRiz8Kqz8ECIBkLQ39TndjRV0qPPjFBZuq7ieydH8YRzyaahCAMG7XVAiBE8Lcn0NkzXJZMklS3SZw+b2PvI6FWkKuovRt5sBXNxw==";
        System.out.println(str);
        byte[] code1 = RSAUtil.encryptByPrivateKey(str.getBytes(), Base64.decodeBase64(pri));
        System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
        //密文
       /* String sec = "YLZHHL4fxzi/BuzCgCjN2kBJsjImh+IFK+kkKSQtNZGLyBVhzOzEnnaKMAikWAR4YWmaWiXBM53pYuFkrmrR7g==";
        
        
        //公钥
        String pub  = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJaHYquinelJIcn+yvMYtXfEK2EogTCmuQYonTFVfCz8jz7ZDf4quV/NEJNUNMh/rzkbVYO+lFrtq0ozeeN9AFMCAwEAAQ==";
        
        byte[] b = RSAUtil.decryptByPublicKey(Base64.encodeBase64(Base64.decodeBase64(sec)), Base64.decodeBase64(pub));
        
        byte[] cipher = RSAUtil.decryptByPublicKey(Base64.decodeBase64(sec), Base64.decodeBase64(pub)); 
        System.out.println(new String(cipher));*/
        
        /*
        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
        //乙方进行数据的解密
        byte[] decode1 = RSAUtil.decryptByPublicKey(code1, publicKey);
        System.out.println("乙方解密后的数据：" + new String(decode1) + "/n/n");
        System.out.println("===========反向进行操作，乙方向甲方发送数据==============/n/n");
        str = "乙方向甲方发送数据RSA";
        System.out.println("原文:" + str);
        //乙方使用公钥对数据进行加密
        byte[] code2 = RSAUtil.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("加密后的数据：" + Base64.encodeBase64String(code2));

        System.out.println("=============乙方将数据传送给甲方======================");
        System.out.println("===========甲方使用私钥对数据进行解密==============");

        //甲方使用私钥对数据进行解密
        byte[] decode2 = RSAUtil.decryptByPrivateKey(code2, privateKey);
        System.out.println("甲方解密后的数据：" + new String(decode2));
        */
    }
}
