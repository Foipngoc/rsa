package com.example.administrator.test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

public class HttpRequest 
{
	onResult onresult;
	
	public HttpRequest(onResult onresult)
	{
		this.onresult=onresult;
		
	}
	
	public interface onResult
	{
		void onSuccess(String result);
		void onError(int httpcode);
		void onError(String desc);
	}
	public static String get(final String actionUrl)
	{
		String sb2 = "";
		try
		{
			URL uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

			conn.setConnectTimeout(15 * 1000);
			conn.setReadTimeout(15 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/json");


			/*DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(obj.toString().getBytes());

			outStream.flush();*/
			InputStream in = null;
			int res = conn.getResponseCode();
			if (res == 200)
			{
				in = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine())
				{
					sb2 = sb2 + line;
				}
				return sb2;
			}
			else if(res==204)
			{
				return "sucess";
			}
			else
			{
				in = conn.getErrorStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine())
				{
					sb2 = sb2 + line;
				}

			}
			conn.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb2;
	}
	
	public static byte[] post(final String actionUrl,byte[] bytes)
	{
		String sb2 = "";
			try
			{
				URL uri = new URL(actionUrl);
                Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.2.2",8888));
				HttpURLConnection conn = (HttpURLConnection) uri.openConnection(proxy);


                conn.setConnectTimeout(150 * 1000);
				conn.setReadTimeout(150 * 1000);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Charsert", "UTF-8");
				conn.setRequestProperty("Content-Type", "application/octet-stream");

				DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
				outStream.write(bytes);
				InputStream in = null;
				outStream.flush();

				int res = conn.getResponseCode();
				if (res == 200)
				{
					in = conn.getInputStream();

					int len=-1;
					byte[] buffer = new byte[128];
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while ((len=in.read(buffer))!=-1)
					{
						out.write(buffer,0,buffer.length);
					};
					return out.toByteArray();
				}
				else if(res==204)
				{
					return "sucess".getBytes();
				}
				else
				{
					in = conn.getErrorStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(in,"iso-8859-1"));
					String line = "";
					for (line = br.readLine(); line != null; line = br.readLine())
					{
						sb2 = sb2 + line;
					}

				}
				outStream.close();
				conn.disconnect();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return sb2.getBytes();
	}
	
}
