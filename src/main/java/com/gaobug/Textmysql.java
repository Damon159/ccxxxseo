package com.gaobug;

import com.alibaba.fastjson.JSON;
import com.gaobug.seo.GreatSeo;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Textmysql {
   public static ScheduledExecutorService pools = Executors.newScheduledThreadPool(500);
   public static Map<String, Object> map = readConfigWeb();
   public static int i = 0;

   public Textmysql(String root, String password, String ip) {
      File folder = new File("../site/");
      if (!folder.exists() && !folder.isDirectory()) {
         folder.mkdirs();
      }

      File libraryLog = new File("runlogs/libraryLog.txt");
      if (!libraryLog.exists() && !libraryLog.isDirectory()) {
         libraryLog.mkdirs();
      }

      Map arrrymap = readConfigSqlTxt();
      File file = new File("runlogs/libraryLog.txt");
      libraryLog(file, "\n开始获取操作：GetDataStart");
      Iterator var8 = arrrymap.keySet().iterator();

      while(var8.hasNext()) {
         Object key = var8.next();
         ConnectionMysql((String)key, root, password, (String)arrrymap.get(key), String.valueOf(ip));
         System.out.println("siteweb:" + arrrymap.get(key));
      }

      //RandomText();
   }

   public static void ConnectionMysql(String library, String root, String password, String fileName, String ip) {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         String url = "jdbc:mysql://" + ip + ":3306/" + library + "?useSSL=false&serverTimezone=UTC";
         Connection conn = DriverManager.getConnection(url, root, password);
         Statement st4 = conn.createStatement();
         Statement st = conn.createStatement();
         Statement st1 = conn.createStatement();
         Statement st2 = conn.createStatement();
         Statement st3 = conn.createStatement();
         ResultSet categories2 = null;
         ResultSet categories3 = null;
         ResultSet categories4 = null;
         Map<Object, String> map = new HashMap();
         ResultSet categories1 = st.executeQuery("select categories_id from categories where parent_id=0 and categories_status=1");

         String cateSql4;
         while(categories1.next()) {
            String cateSql2 = "select categories_id from categories where parent_id=" + categories1.getString("categories_id") + " and categories_status=1";
            categories2 = st1.executeQuery(cateSql2);

            while(categories2.next()) {
               map.put(categories2.getString("categories_id"), categories1.getString("categories_id"));
               String cateSql3 = "select categories_id from categories where parent_id=" + categories2.getString("categories_id") + " and categories_status=1";
               categories3 = st2.executeQuery(cateSql3);

               while(categories3.next()) {
                  map.put(categories3.getString("categories_id"), categories2.getString("categories_id"));
                  cateSql4 = "select categories_id from categories where parent_id=" + categories3.getString("categories_id") + " and categories_status=1";
                  categories4 = st3.executeQuery(cateSql4);

                  while(categories4.next()) {
                     map.put(categories4.getString("categories_id"), categories3.getString("categories_id"));
                  }
               }
            }
         }

         categories1 = null;
         categories2 = null;
         categories3 = null;
         categories4 = null;
         st.close();
         st1.close();
         st2.close();
         st3.close();
         Map<Object, String> cateDesMap = new HashMap();
         Statement cateDesSta = conn.createStatement();
         ResultSet cateDesRus = cateDesSta.executeQuery("select categories_id,categories_name from `categories_description`");

         while(cateDesRus.next()) {
            cateDesMap.put(cateDesRus.getString("categories_id"), cateDesRus.getString("categories_name"));
         }

         cateDesSta.close();
         cateSql4 = null;
         GreatSeo greatSeo = new GreatSeo();
         ResultSet rs5 = st4.executeQuery("SELECT `products_id`,`products_name`,`products_description`,`products_url` FROM `products_description`  LIMIT 0,30000");

         String cateDesName1;
         String cateDesName2;
         String cateDesName3;
         String cateDesName4;
         String products_image;
         String products_model;
         String sqlCa;
         Statement productCateDesSta;
         ResultSet productCateDesRes;
         String feiId3;
         String feiId2;
         String feiId1;
         ResultSet spPriceRes;
         String productOptionNameSql;
         String productOptionName;
         Statement productOptionSta;
         ResultSet productOptionRes;
         String productOptionValueSql;
         Statement productOptionValueSta;
         ResultSet productOptionValueRes;
         Statement spPriceSta;
         while(rs5.next()) {
            cateDesName1 = "";
            cateDesName2 = "";
            cateDesName3 = "";
            cateDesName4 = "";
            products_image = "";
            products_model = "";
            String product_reviews="";
            sqlCa = "select `master_categories_id`,`products_image`,`products_model` ,`v_products_reviewstui` from `products` where `products_id` = " + rs5.getString("products_id");
            productCateDesSta = conn.createStatement();
            productCateDesRes = productCateDesSta.executeQuery(sqlCa);

            while(productCateDesRes.next()) {
               products_image = productCateDesRes.getString("products_image");
               products_model = productCateDesRes.getString("products_model");
               product_reviews=productCateDesRes.getString("v_products_reviewstui");
               if (productCateDesRes.getString("master_categories_id") != null && productCateDesRes.getString("master_categories_id") != "") {
                  feiId3 = productCateDesRes.getString("master_categories_id");
                  cateDesName3 = (String)cateDesMap.get(feiId3);
                  if (feiId3 != null && feiId3 != "") {
                     feiId2 = (String)map.get(feiId3);
                     cateDesName2 = (String)cateDesMap.get(feiId2);
                     if (feiId2 != null && feiId2 != "") {
                        feiId1 = (String)map.get(feiId2);
                        cateDesName1 = (String)cateDesMap.get(feiId1);
                     }
                  }
               }
            }

            feiId3 = "select `specials_new_products_price` from `specials` where `products_id` =" + rs5.getString("products_id");
            feiId2 = "";
            spPriceSta = conn.createStatement();

            for(spPriceRes = spPriceSta.executeQuery(feiId3); spPriceRes.next(); feiId2 = spPriceRes.getString("specials_new_products_price")) {
            }

            productOptionNameSql = "select `options_values_id` from `products_attributes` where `products_id` =" + rs5.getString("products_id");
            productOptionName = "";
            productOptionSta = conn.createStatement();
            productOptionRes = productOptionSta.executeQuery(productOptionNameSql);

            while(productOptionRes.next()) {
               productOptionValueSql = "select `products_options_values_name` from `products_options_values` where `products_options_values_id` = " + productOptionRes.getString("options_values_id");
               productOptionValueSta = conn.createStatement();

               for(productOptionValueRes = productOptionValueSta.executeQuery(productOptionValueSql); productOptionValueRes.next(); productOptionName = productOptionName + "|" + productOptionValueRes.getString("products_options_values_name")) {
               }
            }

            greatSeo.setProduct_id(rs5.getString("products_id"));
            greatSeo.setProduct_name(rs5.getString("products_name"));
            greatSeo.setProduct_model(products_model);
            String images[]=products_image.split(",|\\|\\|\\|");
            String tamp="";
            if (images.length>0){
               greatSeo.setProduct_main_img(images[0]);
               for (int i = 1; i <images.length ; i++) {
                  //<img src=\"https://static.mercdn.net/item/detail/orig/photos/m14795505943_1.jpg?1634299122\"><
                  tamp+="<img src=\""+images[i]+"\"><br>";
               }
            }
            greatSeo.setProduct_price(feiId2);
            String des=rs5.getString("products_description");
            String []tt={"*","=>",",","&",">",">>","/"};
            int temp= (int) (Math.random()*tt.length);
            des=des.replace(" > ",tt[temp]);
            des=des.replace("<h2>","");
            des=des.replace("</h2>","");
            greatSeo.setProduct_description(des+tamp);
            greatSeo.setProduct_name_two("");
            greatSeo.setProduct_url(rs5.getString("products_url"));
            greatSeo.setProduct_related(product_reviews);
            greatSeo.setProduct_cate1(cateDesName1);
            greatSeo.setProduct_cate2(cateDesName2);
            greatSeo.setProduct_cate3(cateDesName3);
            greatSeo.setGoogle_desH(new ArrayList());
            greatSeo.setGoogle_desP(new ArrayList());
            productOptionValueSql = JSON.toJSONString(greatSeo);
            creatFlieAndWrite(library, rs5.getString("products_id"), productOptionValueSql, fileName, false);
         }

         rs5 = st4.executeQuery("SELECT `products_id`,`products_name`,`products_description`,`products_url`  FROM `products_description`  LIMIT 30000,60000");

         while(rs5.next()) {
            cateDesName1 = "";
            cateDesName2 = "";
            cateDesName3 = "";
            cateDesName4 = "";
            products_image = "";
            products_model = "";
            String product_reviews="";
            sqlCa = "select `master_categories_id`,`products_image`,`products_model`,`v_products_reviewstui` from `products` where `products_id` = " + rs5.getString("products_id");
            productCateDesSta = conn.createStatement();
            productCateDesRes = productCateDesSta.executeQuery(sqlCa);

            while(productCateDesRes.next()) {
               products_image = productCateDesRes.getString("products_image");
               products_model = productCateDesRes.getString("products_model");
               product_reviews=productCateDesRes.getString("v_products_reviewstui");
               if (productCateDesRes.getString("master_categories_id") != null && productCateDesRes.getString("master_categories_id") != "") {
                  feiId3 = productCateDesRes.getString("master_categories_id");
                  cateDesName3 = (String)cateDesMap.get(feiId3);
                  if (feiId3 != null && feiId3 != "") {
                     feiId2 = (String)map.get(feiId3);
                     cateDesName2 = (String)cateDesMap.get(feiId2);
                     if (feiId2 != null && feiId2 != "") {
                        feiId1 = (String)map.get(feiId2);
                        cateDesName1 = (String)cateDesMap.get(feiId1);
                     }
                  }
               }
            }

            feiId3 = "select `specials_new_products_price` from `specials` where `products_id` =" + rs5.getString("products_id");
            feiId2 = "";
            spPriceSta = conn.createStatement();

            for(spPriceRes = spPriceSta.executeQuery(feiId3); spPriceRes.next(); feiId2 = spPriceRes.getString("specials_new_products_price")) {
            }

            productOptionNameSql = "select `options_values_id` from `products_attributes` where `products_id` =" + rs5.getString("products_id");
            productOptionName = "";
            productOptionSta = conn.createStatement();
            productOptionRes = productOptionSta.executeQuery(productOptionNameSql);

            while(productOptionRes.next()) {
               productOptionValueSql = "select `products_options_values_name` from `products_options_values` where `products_options_values_id` = " + productOptionRes.getString("options_values_id");
               productOptionValueSta = conn.createStatement();

               for(productOptionValueRes = productOptionValueSta.executeQuery(productOptionValueSql); productOptionValueRes.next(); productOptionName = productOptionName + "|" + productOptionValueRes.getString("products_options_values_name")) {
               }
            }

            greatSeo.setProduct_id(rs5.getString("products_id"));
            greatSeo.setProduct_name(rs5.getString("products_name"));
            greatSeo.setProduct_model(products_model);
            String images[]=products_image.split(",");
            String tamp="";
            if (images.length>0){
               greatSeo.setProduct_main_img(images[0]);
               for (int i = 1; i <images.length ; i++) {
                  //<img src=\"https://static.mercdn.net/item/detail/orig/photos/m14795505943_1.jpg?1634299122\"><
                  tamp+="<img src=\""+images[i]+"\"><br>";
               }
            }
            greatSeo.setProduct_price(feiId2);
            greatSeo.setProduct_description(rs5.getString("products_description")+tamp);
            greatSeo.setProduct_name_two("");
            greatSeo.setProduct_url(rs5.getString("products_url"));
            greatSeo.setProduct_related("");
            greatSeo.setProduct_cate1(cateDesName1);
            greatSeo.setProduct_cate2(cateDesName2);
            greatSeo.setProduct_cate3(cateDesName3);
            greatSeo.setGoogle_desH(new ArrayList());
            greatSeo.setGoogle_desP(new ArrayList());
            productOptionValueSql = JSON.toJSONString(greatSeo);
            creatFlieAndWrite(library, rs5.getString("products_id"), productOptionValueSql, fileName, false);
         }

         rs5.close();
         st4.close();
         conn.close();
         System.gc();
      } catch (ClassNotFoundException var44) {
         var44.printStackTrace();
      } catch (SQLException var45) {
         var45.printStackTrace();
      }

   }

   public static Map<String, Object> readConfigWeb() {
      String path = "configweb.txt";
      File file = new File(path);
      Map<String, Object> map = new HashMap();
      new StringBuilder();

      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
         String s = null;

         while((s = br.readLine()) != null) {
            String[] library = s.split("=>");
            map.put(library[1], library[0]);
         }

         br.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return map;
   }

   public static void creatFlieAndWrite(String library, String p_id, String context, String siteFileName, boolean append) {
      File file1 = new File("../site/" + siteFileName + "/p" + p_id + ".txt");
      //File tempId = new File("tempId.txt");
      if (!file1.exists()) {
         file1.getParentFile().mkdirs();
      }

      try {
         file1.createNewFile();
        // FileUtils.write(tempId, map.get(siteFileName) + p_id + "\n", true);
         FileUtils.write(file1, context);
         System.out.println(siteFileName + "/p" + p_id + ".txt");
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public static Map<String, Object> readConfigSqlTxt() {
      Map<String, Object> map = new HashMap();
      File file = new File("configsql.txt");

      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         String s = null;

         while((s = br.readLine()) != null) {
            String[] library = s.split("=>|\\t");
            map.put(library[0], library[1]);
         }

         br.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return map;
   }

   public static boolean libraryLog(File file, String content) {
      try {
         FileUtils.write(file, content, true);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return true;
   }

   public static ArrayList filedata(String path) {
      BufferedReader br = null;
      ArrayList arrayList = new ArrayList();

      try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
         String str = null;

         for(int var4 = 0; (str = br.readLine()) != null; ++var4) {
            String[] v1 = str.trim().split("\\s+");
            arrayList.add(str);
         }
      } catch (IOException var14) {
         var14.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException var13) {
            var13.printStackTrace();
         }

      }

      return arrayList;
   }

   public static String getFileContent(String path, Boolean line) {
      File file = new File(path);
      if (!file.exists()) {
         return path + " not exit!";
      } else {
         try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String content = "";

            String st;
            while((st = br.readLine()) != null) {
               if (line) {
                  content = content + st + "\n";
               } else {
                  content = content + st;
               }
            }

            return content;
         } catch (IOException var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   public static String RandomText() {
      try {
         String uri = "tempId.txt";
         List<String> list = new ArrayList();
         File file = new File(uri);
         FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         String str = "";

         while((str = br.readLine()) != null) {
            list.add(str);
         }

         Collections.shuffle(list);
         FileUtils.write(file, "");

         for(int i = 0; i < list.size(); ++i) {
            FileUtils.write(file, (String)list.get(i) + "\n", true);
         }

         System.out.println("success random to tempId.txt");
      } catch (FileNotFoundException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      return "success random to tempId.txt";
   }

   public static int ASCIcount(String str) {
      str = str.replaceAll("http://|https://", "");
      int count = 0;

      for(int i = 0; i < str.length(); ++i) {
         count += str.charAt(i);
      }

      return count;
   }


   /**
    * 读取整个文件内容,true 按原内容读取,false 不换行
    * @param path
    * @return
    */
   public static ArrayList getFileContent1(String path, Boolean line){
      File file=new File(path);
      ArrayList list = new ArrayList<>();
      if (!file.exists()){
         return null;
      }else {
         try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String st;
            String content="";
            while ((st=br.readLine())!=null){
               if(line)
                  content+=st+"\n";
               else content+=st;
            }
            list.add(content);
            return list;
         } catch (IOException e) {
            e.printStackTrace();
         }

      }
      return null;
   }
   /**
    * 获取keywords
    * @param
    * @param
    */
   //0 数据库 1名字
   public static void getKeyWordstxt() {
      File file = new File("../site");
      if (!file.exists()) {
         file.mkdirs();
      }
      File file2=new File("keywords");
      if (!file2.exists())
         file2.mkdirs();
      int count;
      int filecount=1;
      long Unix = System.currentTimeMillis() / 1000L;
      count = 0;
      for (Object ob:file.listFiles()
      ) {
         try {
            String domain = ob.toString().replace("../site/", "");
            file = new File(ob.toString());
            for (Object ob1 : file.listFiles()
            ) {
               List list = FileUtils.readLines(new File(ob1.toString()));
               String filenameGoogleImage = "googleimage-" + filecount + "-" + Unix;
               String filenameYoutubeImage = "youtubeimage-" + filecount + "-" + Unix;
               String filenameYahookeyword = "yahookeywords-" + filecount + "-" + Unix;
               if (count > 150000) {
                  filecount++;
                  Unix = System.currentTimeMillis() / 1000L;
                  filenameGoogleImage = "googleimage-" + filecount + "-" + Unix;
                  filenameYoutubeImage = "youtubeimage-" + filecount + "-" + Unix;
                  filenameYahookeyword = "yahookeywords-" + filecount + "-" + Unix;
                  count = 0;
               }


               String urlencode = "";
               String id="";
               String products_name="";
               if (list.size()==0)
                  continue;
               try {
                  Map dataMap = (Map) JSON.parse((String) list.get(0));
                  id = (String) dataMap.get("product_id");
                  products_name= (String) dataMap.get("product_name");
                  urlencode = URLEncoder.encode(products_name, "utf-8");
               } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("faild-->"+ob1);
                  continue;
               }

               String yahoourl = "https://br.search.yahoo.com/search?p=" + urlencode + "#" + domain + "-" + id + "kkkk";
               String contentGoogleImage = domain + "-" + id + "-" + products_name;
               String youtubekeyword = "https://www.youtube.com/results?search_query=" + urlencode + "#" + domain + "-" + id + "kkkk";
               Textmysql.creatFlieAndWrite1("keywords/" + filenameGoogleImage + ".txt", contentGoogleImage + "\n", true);
               Textmysql.creatFlieAndWrite1("keywords/" + filenameYoutubeImage + ".txt", youtubekeyword + "\n", true);
               Textmysql.creatFlieAndWrite1("keywords/" + filenameYahookeyword + ".txt", yahoourl + "\n", true);
               //FileUtils.write(new File("keywords/" + filenameGoogleImage + ".txt"), contentGoogleImage + "\n", true);
               //FileUtils.write(new File("keywords/" + filenameYoutubeImage + ".txt"), youtubekeyword + "\n", true);
               //FileUtils.write(new File("keywords/" + filenameYahookeyword + ".txt"), yahoourl + "\n", true);
               count++;
            }

         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      System.out.println("Getkeys Done!!!!");
      //创建Statement，执行sql

   }
   /**
    * 创建文件夹写入文件 ,true 添加 多线程
    *
    * */
   public static void creatFlieAndWrite1(String path,String context,Boolean append){

      pools.schedule(new Runnable() {
         @Override
         public void run() {
            File file1 = new File(path);
            if(!file1.exists() && path.indexOf(".txt")==-1){
               file1.getParentFile().mkdirs();
            }else {
               try {
                  file1.createNewFile();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            try {
               FileWriter fw = new FileWriter(file1, append);
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
    *
    **/

   public static Map readConfigSqlTxt1(String path){
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
}
