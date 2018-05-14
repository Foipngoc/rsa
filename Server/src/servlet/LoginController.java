package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rsa.Jwt;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/12/6.
 */
@Controller
@RequestMapping("user")
public class LoginController
{
    public static void main(String []args)
    {
        StringBuffer sb=new StringBuffer();
        Map<String,List<Double>> map=new HashMap<>();
        List<Map<String,Object>> listPoints=new ArrayList<>();
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            InputStream isjx=new FileInputStream("E://jx.inp");
            BufferedReader br = new BufferedReader(new InputStreamReader(isjx));
            String str=null;
            boolean flag=false;
            int linen=0;
            List<String[]> listlink=new ArrayList<>();
            while ((str = br.readLine()) != null) {
                if(str.equals("[PIPES]"))
                {
                    flag=true;
                    linen++;
                    continue;
                }  if(flag&&linen<=1)
                {
                    linen++;
                    continue;
                }
                if(str.equals("[PUMPS]")){
                    break;
                }
                if(linen>1)
                {
                    //str=str.replaceAll(" ",",");
                    str=str.replaceAll("\t",",");
                    //System.out.println(str);
                    String s[]=str.trim().split(",");
                    if(s!=null&&s.length>3)
                    {
                        String[] item=new String[2];
                        item[0]=(s[1].trim());
                        item[1]=(s[2].trim());
                        listlink.add(item);
                    }

                }

            }
            System.out.println("eerre");
            InputStream is=new FileInputStream("E://s.json");
            byte[] tempbytes = new byte[100];
            int byteread = 0;

            while ((byteread = is.read(tempbytes)) != -1) {
                sb.append(new String(tempbytes));
            }

            map=objectMapper.readValue(sb.toString().getBytes(),Map.class);
            Map valMap=new HashMap<>();
            for(String key:map.keySet())
            {
                List<Double> list1=map.get(key);
                //Double lat=new Double(0),lon=new Double(0);
                double dd[]=GaussToBL(list1.get(0),list1.get(1));
                //double dd[]=Test.gaussInverseFormula(list1.get(1),list1.get(0),0);
                Map<String,Object> mapnode=new HashMap<>();
                mapnode.put("node",key);
                mapnode.put("lat",dd[1]);
                mapnode.put("lng",dd[0]);
                //System.out.println("x"+dd[0]);
                //System.out.println("y"+dd[1]);
                Map<String,Object> mapPoint=new HashMap<>();
                mapPoint.put("node",mapnode);
                mapPoint.put("links",new ArrayList<>());

                valMap.put(key,mapnode);

                listPoints.add(mapPoint);
            }

            //System.out.println(json);
            for(Map<String,Object> point:listPoints)
            {
                String node=(String) ((Map)(point.get("node"))).get("node");
                for(String[] vals:listlink)
                {
                    if(vals[0].equals(node))
                    {
                        ((List)point.get("links")).add(valMap.get(vals[1]));
                        //point.put("links",);
                    }else if(vals[1].equals(node))
                    {
                        ((List)point.get("links")).add(valMap.get(vals[0]));
                    }
                }
            }

            String json=objectMapper.writeValueAsString(listPoints);
            File file = new File("E://re.js");
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print("const res=");
            ps.print(json);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static double[] GaussProjInvCal(double X, double Y)
    {
        double longitude,  latitude;
        int ProjNo; int ZoneWide; ////带宽
        double longitude1, latitude1, longitude0, latitude0, X0, Y0, xval, yval;
        double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai, iPI;
        iPI = 0.0174532925199433; ////3.1415926535898/180.0;
        //a=6378137.00;f=1.0/298.257;
        //a = 6378245.0; f = 1.0 / 298.3; //54年北京坐标系参数
        a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ZoneWide = 3; ////3度带宽
        ProjNo = (int)(X / 1000000L); //查找带号
        //ProjNo=40;
        longitude0 = (ProjNo) * ZoneWide ;
        longitude0 = longitude0 * iPI; //中央经线
        X0 = ProjNo * 1000000L + 500000L;
        Y0 = 0;
        xval = X - X0; yval = Y - Y0; //带内大地坐标
        e2 = 2 * f - f * f;
        e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
        ee = e2 / (1 - e2);
        M = yval;
        u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
        fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(
                4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
        C = ee * Math.cos(fai) * Math.cos(fai);
        T = Math.tan(fai) * Math.tan(fai);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
        R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin
                (fai) * Math.sin(fai)));
        D = xval / NN;
        //计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D
                * D * D * D * D / 120) / Math.cos(fai);
        latitude1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24
                + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
        //转换为度 DD
        longitude = longitude1 / iPI;
        latitude = latitude1 / iPI;
        double dd[]=new double[2];
        dd[0]=longitude+120;
        dd[1]=latitude;
        return dd;
    }

    public static double[] GaussToBL(double X, double Y)//, double *longitude, double *latitude)
    {
        int ProjNo; int ZoneWide; ////带宽
        double[] output = new double[2];
        double longitude1,latitude1, longitude0, X0,Y0, xval,yval;//latitude0,
        double e1,e2,f,a, ee, NN, T,C, M, D,R,u,fai, iPI;
        iPI = 0.0174532925199433; ////3.1415926535898/180.0;
        a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数
        //a=6378135; f=1/298.257223563; //80年西安坐标系参数
        ZoneWide = 6; ////6度带宽
        ProjNo = (int)(X/1000000L) ; //查找带号
        //ProjNo=20;
        longitude0 = (ProjNo-1) * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI ; //中央经线


        X0 = ProjNo*1000000L+500000L;
        Y0 = 0;
        xval = X-X0; yval = Y-Y0; //带内大地坐标
        e2 = 2*f-f*f;
        e1 = (1.0-Math.sqrt(1-e2))/(1.0+Math.sqrt(1-e2));
        ee = e2/(1-e2);
        M = yval;
        u = M/(a*(1-e2/4-3*e2*e2/64-5*e2*e2*e2/256));
        fai = u+(3*e1/2-27*e1*e1*e1/32)*Math.sin(2*u)+(21*e1*e1/16-55*e1*e1*e1*e1/32)*Math.sin(
                4*u)
                +(151*e1*e1*e1/96)*Math.sin(6*u)+(1097*e1*e1*e1*e1/512)*Math.sin(8*u);
        C = ee*Math.cos(fai)*Math.cos(fai);
        T = Math.tan(fai)*Math.tan(fai);
        NN = a/Math.sqrt(1.0-e2*Math.sin(fai)*Math.sin(fai));
        R = a*(1-e2)/Math.sqrt((1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin(fai)*Math.sin(fai))*(1-e2*Math.sin
                (fai)*Math.sin(fai)));
        D = xval/NN;
        //计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0+(D-(1+2*T+C)*D*D*D/6+(5-2*C+28*T-3*C*C+8*ee+24*T*T)*D
                *D*D*D*D/120)/Math.cos(fai);
        latitude1 = fai -(NN*Math.tan(fai)/R)*(D*D/2-(5+3*T+10*C-4*C*C-9*ee)*D*D*D*D/24
                +(61+90*T+298*C+45*T*T-256*ee-3*C*C)*D*D*D*D*D*D/720);
        //转换为度 DD
        output[0] = longitude1 / iPI+123.001;
        output[1] = latitude1 / iPI;
        return output;
        //*longitude = longitude1 / iPI;
        //*latitude = latitude1 / iPI;
    }

    @Resource(name = "sessionFactoryMySql")
    SessionFactory sessionFactory;
    @RequestMapping("appLogin")
    @ResponseBody
    public Object appLogin(String username,String passwd){
        Map<String,Object> map=new HashMap<>();

       Session session= sessionFactory.openSession();
       UserEntity userEntity=(UserEntity) session.createQuery("select user from UserEntity user where user.name='"+username+"' and user.passwd='"+passwd+"'")
               .uniqueResult();

        if(userEntity==null) {
            map.put("success",0);
            map.put("token",null);
            map.put("dsc","用户名或密码错误");
            map.put("errorCode","1");

        }else {
            Jwt jwt=new Jwt();
            String token=jwt.generateToken(username);
            map.put("success",1);
            map.put("token",token);
            map.put("dsc","登录成功");
            map.put("errorCode","0");
        }
        session.close();
       return map;
    }
    @RequestMapping("resetPasswd")
    @ResponseBody
    public Object resetPasswd(String username,String pswold,String pswnew,String token){
        Map<String,Object> map=new HashMap<>();
        if(token==null||"".equals(token)){
            map.put("success",0);
            map.put("dsc","token不能为空");
            map.put("errorCode","2");
            return map;
        }
        Session session= sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        UserEntity userEntity=(UserEntity) session.createQuery("select user from UserEntity user where user.name='"+username+"' and user.passwd='"+pswold+"'")
                .uniqueResult();

        if(userEntity==null) {
            map.put("success",0);
            map.put("dsc","用户名或密码错误");
            map.put("errorCode","1");
            return map;
        }
        try{
            Jwt jwt=new Jwt();
            String name=jwt.parseToken(token).getAudience();
            if(name==null||!name.equals(username)){
                map.put("success",0);
                map.put("dsc","token验证错误");
                map.put("errorCode","3");
                return map;
            }
        }catch (Exception e){
            map.put("success",0);
            map.put("dsc","token验证错误");
            map.put("errorCode","3");
            return map;
        }
        if(pswnew==null){
            map.put("success",0);
            map.put("dsc","新密码不能为空");
            map.put("errorCode","3");
            return map;
        }
        userEntity.setPasswd(pswnew);
        session.update(userEntity);
        transaction.commit();
        session.close();
        map.put("success",1);
        map.put("dsc","密码修改成功");
        map.put("errorCode",0);
        return map;
    }
}
