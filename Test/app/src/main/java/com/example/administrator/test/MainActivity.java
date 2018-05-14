package com.example.administrator.test;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {
    static String pk;
    static final int MAX_ENCRYPT_BLOCK = 117;
    static final int MAX_DECRYPT_BLOCK = 128;
    byte re[]=new byte[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pk=getServerPublicKey();

    }
    public void requestMatch(final View view)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=new JSONObject();
                //String p="android端测试";
                try {
                    jsonObject.put("name","游客001");

                    final int id=view.getId();
                    byte[] o=encryptByPublicKey(jsonObject.toString().getBytes(),pk);

                    byte[] s=HttpRequest.post("http://192.168.0.211:8080/RSA", o);
                    re=decryptByPrivateKey(s,getLocalPrivateKey());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String s=new String(re,"utf-8");
                                System.out.println(new String(re));
                                Toast.makeText(MainActivity.this,new String(re),Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(s);
                                builder.create().show();
                            }catch (Exception e)
                            {

                            }

                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception{
        byte[ ] keyBytes=Base64.decode(privateKey,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  throws Exception
    {
        PrivateKey privateK = getPrivateKey(privateKey);
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
    public static PublicKey getPublicKey(String publicKey) throws Exception{
        byte[] keyBytes= Base64.decode(publicKey,Base64.DEFAULT);
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception
    {
        //PublicKey publicK=getPublicKey(publicKey);
        PublicKey publicK=getPublicKey(pk);

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
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String getServerPublicKey();
    public native String getLocalPrivateKey();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

}
