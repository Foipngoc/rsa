package com.example.administrator.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ChessRoomActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    public void connect()
    {
        try {
            URI uri = new URI("ws://192.168.0.211:8080/socket");

            Client client=new Client(uri,new Draft_17());
            client.setOpenCallBack(new Client.OpenCallBack() {
                @Override
                public void onOpen(Client client1) {
                    try {
                        JSONObject jsonObject=new JSONObject();

                        jsonObject.put("msgType","1");
                        jsonObject.put("roomId","1");
                        jsonObject.put("userId","1");
                        jsonObject.put("first",0);//后手
                        client1.send(jsonObject.toString().getBytes());

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            client.connectBlocking();

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
