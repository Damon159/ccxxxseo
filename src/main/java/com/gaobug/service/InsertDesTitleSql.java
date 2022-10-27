package com.gaobug.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertDesTitleSql {
   public static Connection connection = null;

   public static void main(String[] args) {
      new InsertDesTitleSql();
      BufferedReader br = null;

      try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("title/des/zc3.com1662366226499.txt")), "UTF-8"));
         String str = null;
         boolean var4 = false;

         while((str = br.readLine()) != null) {
            String[] siteTemp = str.split("\\.gz,");
            String[] lastTemp = siteTemp[0].split("-");
            String site = lastTemp[0];
            String productId = lastTemp[1];
            String productDes = siteTemp[1];
            PreparedStatement statement = null;

            try {
               String sql = "INSERT INTO google_des_title (product_id,site,product_title,status) VALUES (?,?,?,?) ; ";
               statement = connection.prepareStatement(sql);
               statement.setString(1, productId);
               statement.setString(2, site);
               statement.setString(3, productDes);
               statement.setString(4, "0");
               statement.executeUpdate();
               statement.close();
            } catch (SQLException var21) {
               var21.printStackTrace();
            }
         }
      } catch (IOException var22) {
         var22.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException var20) {
            var20.printStackTrace();
         }

      }

   }

   public static ArrayList filedata(String path) {
      ArrayList arrayList = new ArrayList();
      return arrayList;
   }

   public InsertDesTitleSql() {
      String driver = "com.mysql.cj.jdbc.Driver";
      String url = "jdbc:mysql://localhost:3306/content?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong";
      String user = "root";
      String password = "123456";

      try {
         Class.forName(driver);
      } catch (ClassNotFoundException var8) {
         var8.printStackTrace();
      }

      System.out.println("driver success");

      try {
         connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException var7) {
         var7.printStackTrace();
      }

      try {
         if (!connection.isClosed()) {
            System.out.println("\n\t\t成功以 " + user + " 身份连接到数据库！！！");
         }
      } catch (SQLException var6) {
         var6.printStackTrace();
      }

   }
}
