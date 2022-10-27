
import com.gaobug.Textmysql;
import com.gaobug.utils.ReadFile;
import com.gaobug.utils.RedisUtils;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class test extends RedisUtils {

    public static void main(String[] args)
    {
        System.out.println("111111111111");
    }
    public static Map readConfigSqlTxt(String path){
        Map map=new HashMap();
        String[] library;
        File file =new File(path);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                library= s.split("=>|\\t");  //获取数据库名
                if(library[0]!="")
                    map.put(library[0],library[1]);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //return  arrayList;
        return map;
    }
    static {

        System.out.println("fdsafsdfasfsadf");
    }
}
