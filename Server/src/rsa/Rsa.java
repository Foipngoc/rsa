package rsa;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzd on 2017/12/8.
 */
public class Rsa
{
    static final int MAX_ENCRYPT_BLOCK = 117;
    static final int MAX_DECRYPT_BLOCK = 128;

    public static final String clientPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPZ6Er9Y3b1YKhmU2oJhKMUb+KWLtQdBeIFEsI5SDTM6YsjeAmSHWRfZGBvWqJrk5qzj7wDGvJUmMpIlFRZ/KqxhwCOKvgio4BXRbstG4Kb7Zuv5SQJdLPlOeQ4LsQfU4PRw5DCPY5VLFT8NitTFnoD5t5jgrs+py0AB6QMcg4AwIDAQAB";
    public static final String serverPrivateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALuNyy+k8NCjYLILzIKu7b9Fz7SA/Pn4fxsPnZJ4a0i8rLRJ5BOeIChJnt6os1tIed0iHxb774Owp4icPDHhOpi2dEc2rK43SMVhxfg7nSjVzlrerEpKWayDqsnjZkDXHWW4iwAWA3QofkGfzDRJO7X9jzIsMuJuNNLUuCeU6VtlAgMBAAECgYEAs1fnT2vdiuRvdlGqAdzcIubSsrwVK8LfaDUKbCESFb+1NcDig4/VGDEt5aRTvZoUTcxvBj9qzPaDfTJIrTm+GNwv1+sR7XyF0z6PPpZgjhlzrENwNK6Qsnfp1FNpz+W5nYLS/M56FeMdwQz1lCyMrndym9/IUD3O5ds9GmClyQECQQDzBdUdopxsQMeo4mNDfP9S+a/1iko0J+gEwDFyE1NaLF40nF7AJL80rGKvQkjHxCaYZWyDA/L2uJVHlUIqavq1AkEAxZGw2SAxEwdF6T7WKNbCtRoW8EWBwA9qn3LBoYS3/hZabFGWVSOgt+9U0nqF4MqjGcxf/93fSAmI0kJT7uFb8QJAV7HWGpCQQ5wIsE+pJDRZ6jZBoagaaMGxvu0fqpuAq3xxLijzJADeooJGRhn0K2oDx2BqDMkDBrbK337j1myuNQJBAIsbSamXQPZk6JSR6bhXfkCFXiFOz0yrjic2ZZ+UEviURJZ/25mWvFLF8LNCEYF1K+G+RNmjHtNZ4zwn93D6vAECQF/LAvOhEPJ/c85JkioUF14o84VU/VQRrWIfH8/W7AHdxDS2CaoxlQUGXgj3RWXxgE6mgH5Zq4SRkZcNfbPxcRc=";

    //static Map<String,Object> map=new HashMap<>();
     /*public static PublicKey getPublicKey()
    {
        return (PublicKey) map.get("public");
    }*/
     /*public static PrivateKey getPrivateKey()
    {
        return (PrivateKey) map.get("private");
    }*/


    /*public static void main(String args[])
    {
        writeKeyPair();
        /*try {
            byte[] re=encryptByPublicKey("这是测试代码".getBytes(),pk);
            String s=new String(re);
            System.out.println(s);
            byte[] co=decryptByPrivateKey(re,prk);
            System.out.println("aaa");
            System.out.println(new String(co));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    public static void writeKeyPair()
    {
        try
        {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //map.put("public",publicKey);
            //map.put("private",privateKey);
            //System.out.println("公钥："+new String(Base64.getEncoder().encode(publicKey.getEncoded())));
            //System.out.println("si钥："+new String(Base64.getEncoder().encode(privateKey.getEncoded())));
            FileOutputStream fo=new FileOutputStream("E://key.txt");
            fo.write(new String(Base64.getEncoder().encode(publicKey.getEncoded())).getBytes());
            fo.write(System.getProperty("line.separator").getBytes());
            fo.write("**************************************************************".getBytes());
            fo.write(System.getProperty("line.separator").getBytes());
            fo.write(new String(Base64.getEncoder().encode(privateKey.getEncoded())).getBytes());
            fo.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicKey(String publicKey) throws Exception{
        byte[ ] keyBytes=Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception{
        byte[ ] keyBytes=Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    //公钥分块加密
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception
    {
        PublicKey publicK=getPublicKey(publicKey);
        //PublicKey publicK=getPublicKey();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  throws Exception
    {
        Key privateK = getPrivateKey(privateKey);
        //Key privateK = getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }
}
