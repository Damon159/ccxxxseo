
import com.alibaba.fastjson.JSON;
import com.gaobug.seo.GreatSeo;

import java.io.*;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ms {
    public static ScheduledExecutorService pools= Executors.newScheduledThreadPool(100);
    public static ScheduledExecutorService root1= Executors.newScheduledThreadPool(5);

//    public static void main(String[] args) {
//      //  ConnectionMysql("newzcjp","root","123456", "newzcjp");
//
//
//     //   readConfigSqlTxt();
//
//    }

    public ms(){
        Map arrrymap=   readConfigSqlTxt();
        for (Object key : arrrymap.keySet()) {
            root1.schedule(new Runnable() {
                @Override
                public void run() {
                    ConnectionMysql((String) key,"root","ef25ace010b50bc6", (String) arrrymap.get(key));
                    //    ConnectionMysql((String) key,"root","123456", "newzcjp");
                }
            },0, TimeUnit.SECONDS);

        }
    }
    public static void ConnectionMysql(String library, String root, String password,String fileName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/"+library+"?useSSL=false&serverTimezone=UTC";
            String username=root;
            String userpwd=password;
            Connection conn = DriverManager.getConnection(url,username,userpwd);
            //创建Statement，执行sql
            Statement st4=conn.createStatement();
            //获取分类start
            Statement st=conn.createStatement();
            Statement st1=conn.createStatement();
            Statement st2=conn.createStatement();
            Statement st3=conn.createStatement();
            ResultSet categories1;
            ResultSet categories2 = null;
            ResultSet categories3 = null;
            ResultSet categories4 = null;
            Map<Object,String> map=new HashMap<>();
            categories1=st.executeQuery("select categories_id from categories where parent_id=0 and categories_status=1");
            while(categories1.next()){
                String cateSql2= "select categories_id from categories where parent_id="+categories1.getString("categories_id")+" and categories_status=1";  //获取二级
                categories2=st1.executeQuery(cateSql2);
                while(categories2.next()){
                    map.put(categories2.getString("categories_id"),categories1.getString("categories_id"));
                    String cateSql3= "select categories_id from categories where parent_id="+categories2.getString("categories_id")+" and categories_status=1";  //获取二级
                    categories3=st2.executeQuery(cateSql3);
                    while(categories3.next()){
                        map.put(categories3.getString("categories_id"),categories2.getString("categories_id"));
                        String cateSql4= "select categories_id from categories where parent_id="+categories3.getString("categories_id")+" and categories_status=1";  //获取二级
                        categories4=st3.executeQuery(cateSql4);
                        while(categories4.next()){
                            map.put(categories4.getString("categories_id"),categories3.getString("categories_id"));
                        }
                    }
                }
            }
            st.close();
            st1.close();
            st2.close();
            st3.close();
            //查询分类的详情start
            Map<Object,String>  cateDesMap=new HashMap<>();
            Statement cateDesSta=conn.createStatement();
            ResultSet cateDesRus = cateDesSta.executeQuery("select categories_id,categories_name from `categories_description`");
            while(cateDesRus.next()){
                cateDesMap.put(cateDesRus.getString("categories_id"),cateDesRus.getString("categories_name"));
            }
            cateDesSta.close();
            //查询分类的详情end
            //获取分类end
            //开始查询内容
            ResultSet rs5=st4.executeQuery("SELECT `products_id`,`products_name`,`products_description`,`products_name_two`,`products_url`,`products_related` FROM `products_description`");
            while(rs5.next()){
                GreatSeo greatSeo = new GreatSeo();
                //查询指定产品的分类链接star
                String cateDesName1="";
                String cateDesName2="";
                String cateDesName3="";
                String cateDesName4="";  //四级分类预留
                String products_image="";  //产品主图
                String products_model="";  //产品model号
                String sqlCa="select `master_categories_id`,`products_image`,`products_model` from `products` where `products_id` = "+rs5.getString("products_id");
                Statement productCateDesSta=conn.createStatement();
                ResultSet productCateDesRes=productCateDesSta.executeQuery(sqlCa);
                while(productCateDesRes.next()){
                    products_image=productCateDesRes.getString("products_image");
                    products_model=productCateDesRes.getString("products_model");
                    if(productCateDesRes.getString("master_categories_id")!=null&&productCateDesRes.getString("master_categories_id")!=""){ //3级
                        String feiId3=productCateDesRes.getString("master_categories_id");
                        cateDesName3=  cateDesMap.get(feiId3);
                        if (feiId3!=null&&feiId3!=""){ //2级
                            String feiId2=map.get(feiId3);
                            cateDesName2=  cateDesMap.get(feiId2);
                            if (feiId2!=null&&feiId2!=""){ //1级
                                String feiId1=map.get(feiId2);
                                cateDesName1=cateDesMap.get(feiId1);
                            }
                        }
                    }
                }
                //查询指定产品的分类链接end

                //查询指定产品的特价 start
                String spPriceSql="select `specials_new_products_price` from `specials` where `products_id` ="+rs5.getString("products_id");
                String spicePrice="";
                Statement spPriceSta=conn.createStatement();
                ResultSet spPriceRes=spPriceSta.executeQuery(spPriceSql);
                while (spPriceRes.next()){
                    spicePrice=spPriceRes.getString("specials_new_products_price");
                }
                //查询指定产品的特价 end
                //查询指定产品的属性 start
                String productOptionNameSql="select `options_values_id` from `products_attributes` where `products_id` ="+rs5.getString("products_id");
                String productOptionName="";
                Statement productOptionSta=conn.createStatement();
                ResultSet productOptionRes=productOptionSta.executeQuery(productOptionNameSql);
                while (productOptionRes.next()){
                    String productOptionValueSql = "select `products_options_values_name` from `products_options_values` where `products_options_values_id` = " + productOptionRes.getString("options_values_id") ;
                    Statement productOptionValueSta=conn.createStatement();
                    ResultSet productOptionValueRes=productOptionValueSta.executeQuery(productOptionValueSql);
                    while (productOptionValueRes.next()){
                        productOptionName=productOptionName+"|"+productOptionValueRes.getString("products_options_values_name");
                    }
                }
                //查询指定产品的属性 end
                greatSeo.setProduct_id(rs5.getString("products_id"));  //写入文件
                greatSeo.setProduct_name(rs5.getString("products_name"));
                greatSeo.setProduct_model(products_model);
                greatSeo.setProduct_main_img(products_image);
                greatSeo.setProduct_price(spicePrice);
                greatSeo.setProduct_description(rs5.getString("products_description"));
                greatSeo.setProduct_name_two(rs5.getString("products_name_two"));
                greatSeo.setProduct_url(rs5.getString("products_url"));
                greatSeo.setProduct_related(rs5.getString("products_related"));
                greatSeo.setProduct_cate1(cateDesName1);
                greatSeo.setProduct_cate2(cateDesName2);
                greatSeo.setProduct_cate3(cateDesName3);
                String stringJSON = JSON.toJSONString(greatSeo);
                System.out.println(fileName+":"+rs5.getString("products_id"));
                creatFlieAndWrite(library,rs5.getString("products_id"),stringJSON,fileName);
            }
//关闭连接
            rs5.close();
            st4.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 创建文件夹写入文件
     *
     * */
    public static void creatFlieAndWrite(String library,String p_id,String context,String siteFileName){

        pools.schedule(new Runnable() {
            @Override
            public void run() {
                File file1 = new File("site\\"+siteFileName+"\\p"+p_id+".txt");
                if(!file1.exists()){
                    file1.getParentFile().mkdirs();
                }
                try {
                    file1.createNewFile();

                    FileWriter fw = new FileWriter(file1);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(context);
                    bw.flush();
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },0, TimeUnit.SECONDS);
    }


    /**
     * 读取配置文件内容
     *  */

    public static  Map<String,Object>  readConfigSqlTxt(){
        Map<String,Object> map=new HashMap();
        File file =new File("configsql.txt");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                String[] library= s.split("=>");  //获取数据库名
                map.put(library[0],library[1]);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return  map;
    }


}



