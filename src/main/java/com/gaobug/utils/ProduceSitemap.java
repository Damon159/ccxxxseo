package com.gaobug.utils;

import com.gaobug.controller.textController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProduceSitemap {
   public static String maps(int count, String hostname) {
      LocalDateTime localDateTime = LocalDateTime.now();
      String data = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      String tempurl = "";
      String sitemap_header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n                <urlset\n        xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n        xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n        http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">";
      String sitemap_content = "";

      for(int i = 1; i <= count; ++i) {
         String sitemapName = hostname + "/?sitemap" + i + ".xml";
         sitemap_content = sitemap_content + "<url><loc>" + sitemapName + "</loc> <lastmod>" + data + "</lastmod><changefreq>daily</changefreq>\n    <priority>0.1</priority>\n  </url>";
      }

      String lastSitemap = "</urlset>";
      return sitemap_header + sitemap_content + lastSitemap;
   }

   public String echoSitemap(String sitemapName) {
      LocalDateTime localDateTime = LocalDateTime.now();
      String data = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      String sitemap_header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>               <urlset      xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"        xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n        http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">";
      sitemap_header = sitemap_header + "<url><loc>" + sitemapName + "</loc> <lastmod>" + data + "</lastmod><changefreq>daily</changefreq>\n    <priority>0.1</priority>\n  </url>";
      String lastSitemap = "</urlset>";
      String url = randGetUrl(2000, data, sitemapName);
      sitemap_header = sitemap_header + url + lastSitemap;
      return sitemap_header;
   }

   public static String randGetUrl(int count, String data, String sitemapName) {
      String sumUlr = "";

      for(int i = 0; i < count; ++i) {
         String url = "<url><loc>" + sitemapName + "/?" + getSiteWeb(sitemapName) + "</loc><lastmod>" + data + "</lastmod><priority>0.8</priority><changefreq>hourly</changefreq></url>";
         sumUlr = sumUlr + url;
      }

      return sumUlr;
   }

   public static String getSiteWeb(String sitemapName) {
      sitemapName = sitemapName.replaceAll("www.", "");
      String startThree = shellThree(sitemapName);
      Map<String, Object> map = readConfigWeb();
      String[] keys = (String[])map.keySet().toArray(new String[0]);
      Random random = new Random();
      String randomKey = keys[random.nextInt(keys.length)];
      int num = (int)(Math.random() * 20000.0D) + 1;
      String md5GetWeb = string2MD5(randomKey + num);
      String url = startThree + randomKey + num + md5GetWeb;
      return url;
   }

   public static String shellThree(String inStr) {
      MessageDigest md5 = null;
      inStr = textController.hostUlr;

      try {
         md5 = MessageDigest.getInstance("MD5");
      } catch (Exception var8) {
         System.out.println(var8.toString());
         var8.printStackTrace();
         return "";
      }

      char[] charArray = inStr.toCharArray();
      byte[] byteArray = new byte[charArray.length];

      for(int i = 0; i < charArray.length; ++i) {
         byteArray[i] = (byte)charArray[i];
      }

      byte[] md5Bytes = md5.digest(byteArray);
      StringBuffer hexValue = new StringBuffer();

      for(int i = 0; i < md5Bytes.length; ++i) {
         int val = md5Bytes[i] & 255;
         if (val < 16) {
            hexValue.append("0");
         }

         hexValue.append(Integer.toHexString(val));
      }

      String lastString = hexValue.toString().substring(3, 6);
      return lastString;
   }

   public static String GetOneUrl(String sitemapName) {
      String url = sitemapName + "/index.php?" + getSiteWeb(sitemapName);
      return url;
   }

   public static String GetOneSameUrl(String sitemapName) {
      String url = sitemapName + "/index.php?" + getSiteWeb(sitemapName);
      return url;
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
            map.put(library[0], library[1]);
         }

         br.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return map;
   }

   public static String string2MD5(String inStr) {
      MessageDigest md5 = null;
      inStr = textController.hostUlr;

      try {
         md5 = MessageDigest.getInstance("MD5");
      } catch (Exception var10) {
         System.out.println(var10.toString());
         var10.printStackTrace();
         return "";
      }

      char[] charArray = inStr.toCharArray();
      byte[] byteArray = new byte[charArray.length];

      for(int i = 0; i < charArray.length; ++i) {
         byteArray[i] = (byte)charArray[i];
      }

      byte[] md5Bytes = md5.digest(byteArray);
      StringBuffer hexValue = new StringBuffer();

      for(int i = 0; i < md5Bytes.length; ++i) {
         int val = md5Bytes[i] & 255;
         if (val < 16) {
            hexValue.append("0");
         }

         hexValue.append(Integer.toHexString(val));
      }

      String gropId = "";
      ReadFile readFile = new ReadFile();
      Map mapjson = ReadFile.readConfig();
      gropId = (String)mapjson.get("groupId");
      String lastString = gropId + hexValue.toString().substring(0, 3);
      return lastString;
   }
}
