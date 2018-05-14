package interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.RedisClient;
import rsa.HttpEcode;
import rsa.Jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/12/6.
 */
public class AccessInterceptor implements HandlerInterceptor
{
    RedisClient redisClient;
    Jwt jwt;

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }
    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception
    {
        try
        {
            String token=httpServletRequest.getParameter("token");
            if(token==null||"".equals(token)) return false;
            String username=jwt.parseToken(token).getAudience();
            if(username==null||"".equals(username)) return false;
            String tokenRedis= redisClient.jedis().get(username);
            if(!token.equals(tokenRedis)) return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
