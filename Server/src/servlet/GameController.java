package servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rsa.HttpEcode;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wangzd on 2017/12/7.
 */
@Controller
public class GameController
{
    @Resource
    HttpEcode httpEcode;

    @RequestMapping(value = "RSA",method = RequestMethod.POST)
    @ResponseBody
    public void RSA(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String r= httpEcode.decode(request);
        System.out.println(r);
        httpEcode.encode(response,"来自服务器的数据:这是加密接口");

    }
    @RequestMapping(value = "noRSA",method = RequestMethod.POST)
    @ResponseBody
    public void noRSA(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        byte[] buffer = new byte[128];
        InputStream inputStream=request.getInputStream();
        int len=-1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len=inputStream.read(buffer))!=-1)
        {
            out.write(buffer,0,buffer.length);
        };

        System.out.println(new String(out.toByteArray()));
        response.getOutputStream().write("来自服务器的数据:这是未加密接口".getBytes("utf-8"));

    }
}
