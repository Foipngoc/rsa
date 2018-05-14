package com.example.administrator.test;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

public class Client extends WebSocketClient 
{
	interface OpenCallBack{
		void onOpen(Client client);
	}

	OpenCallBack openCallBack;
	public void setOpenCallBack(OpenCallBack openCallBack)
	{
		this.openCallBack=openCallBack;
	}

	public Client(URI serverUri, Draft draft) 
	{
		super(serverUri, draft);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String msg) 
	{
		System.out.println(msg);
	}

	@Override
	public void onOpen(ServerHandshake arg0) 
	{
		System.out.println(arg0.toString());
		System.out.println("onOpen");

		openCallBack.onOpen(this);
		
	}
}
