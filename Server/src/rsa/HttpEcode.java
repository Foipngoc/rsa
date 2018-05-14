package rsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by wangzd on 2018/1/15.
 */
@Component
public class HttpEcode
{
    public HttpEcode()
    {
        System.out.println("ffffd");
    }
    @Resource
    Rsa rsa;
    public void encode(HttpServletResponse response,String content) throws Exception
    {
        byte[] reb=rsa.encryptByPublicKey(content.getBytes("utf-8"),Rsa.clientPublicKey);
        response.getOutputStream().write(reb);
    }
    public String decode(HttpServletRequest request) throws Exception
    {
        byte[] buffer = new byte[128];
        InputStream inputStream=request.getInputStream();
        int len=-1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len=inputStream.read(buffer))!=-1)
        {
            out.write(buffer,0,buffer.length);
        };

        byte re[]=rsa.decryptByPrivateKey(out.toByteArray(),Rsa.serverPrivateKey);

        return new String(re,"utf-8");
    }

    public Map<String,String> decodeMap(HttpServletRequest request) throws Exception
    {
        String s=decode(request);
        ObjectMapper mapper=new ObjectMapper();
        return mapper.readValue(s.getBytes(), Map.class);
    }
}
