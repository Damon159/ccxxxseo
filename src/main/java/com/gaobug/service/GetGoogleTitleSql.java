package com.gaobug.service;

import com.gaobug.Textmysql;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class GetGoogleTitleSql {
   public static int countTitle = 0;
   public static String titleSite = "siteTitle.txt";

   public GetGoogleTitleSql(String ip, String root, String password, String imgOrDes) {
      File folder1 = new File("title/ub/");
      if (!folder1.exists() && !folder1.isDirectory()) {
         folder1.mkdirs();
      }

      Map arrrymap = Textmysql.readConfigSqlTxt();
      File file = new File("runlogs/libraryLog.txt");
      Textmysql.libraryLog(file, "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n！！！！！！GetDataStartGOOGLE\n");
      Iterator var8 = arrrymap.keySet().iterator();

      while(var8.hasNext()) {
         Object key = var8.next();
         ConnectionMysql(ip, (String)key, root, password, (String)arrrymap.get(key), imgOrDes);
      }

   }

   public static void ConnectionMysql(String ip, String library, String root, String password, String fileName, String imgOrDes) {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         String url = "jdbc:mysql://" + ip + ":3306/" + library + "?useSSL=false&serverTimezone=UTC";
         Connection conn = DriverManager.getConnection(url, root, password);
         Statement st4 = conn.createStatement();
         String fileImgOrDes = "";
         if (imgOrDes.equals("0")) {
            fileImgOrDes = "title/img/";
         } else {
            fileImgOrDes = "title/des/";
         }

         ResultSet rs5 = st4.executeQuery("SELECT `products_id`,`products_name`,`products_description`,`products_name_two`,`products_url`,`products_related` FROM `products_description`");

         File folder;
         while(rs5.next()) {
            if (countTitle > 300000) {
               titleSite = fileName + (new Date()).getTime() + ".txt";
               countTitle = 0;
            }

            ++countTitle;
            folder = new File(fileImgOrDes);
            if (!folder.exists() && !folder.isDirectory()) {
               folder.mkdirs();
            }

            File file = new File(fileImgOrDes + titleSite);
            if (!file.exists()) {
               try {
                  file.createNewFile();
               } catch (IOException var20) {
                  var20.printStackTrace();
               }
            }

            try {
               if (imgOrDes.equals("0")) {
                  System.out.println("开始img写入文件" + fileImgOrDes + titleSite);
                  FileUtils.write(file, fileName + "-" + rs5.getString("products_id") + "," + rs5.getString("products_name") + "\n", true);
               } else {
                  String urlencode = URLEncoder.encode(rs5.getString("products_name"));
                  String domaindomain = fileName + "-" + rs5.getString("products_id");
                  String googleurl = "https://br.search.yahoo.com/search?p=" + urlencode + "#gl=" + domaindomain + "kkkkkk\n";
                  FileUtils.write(file, googleurl, true);
                  String youtubekeyword = "https://www.youtube.com/results?search_query=" + urlencode + "#" + domaindomain + "kkkk\n";
                  FileUtils.write(new File("title/yb/" + titleSite), youtubekeyword, true);
               }
            } catch (IOException var19) {
               var19.printStackTrace();
            }
         }

         rs5.close();
         st4.close();
         conn.close();
         folder = new File("runlogs/libraryLog.txt");
         Textmysql.libraryLog(folder, fileName + "获取标题结束<br /> \n");
      } catch (ClassNotFoundException var21) {
         var21.printStackTrace();
      } catch (SQLException var22) {
         var22.printStackTrace();
      }

   }
}
