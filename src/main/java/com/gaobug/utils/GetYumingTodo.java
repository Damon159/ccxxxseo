package com.gaobug.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetYumingTodo {
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

   public static String getSiteYuming(String yuMingCanShu, String yuMing) {
      boolean i = false;

      try {
         if (yuMingCanShu.equals("/") || yuMingCanShu.length() < 7 || yuMingCanShu.equals("/index.php")) {
            ReadTemp readTemp = new ReadTemp();
            yuMingCanShu = ReadTemp.getIndex(yuMingCanShu, yuMing, 1);
            i = true;
         }

         yuMingCanShu = yuMingCanShu.substring(0, yuMingCanShu.length() - 5);
         if (!i) {
            if (yuMingCanShu.indexOf("?") > 0) {
               yuMingCanShu = yuMingCanShu.substring(yuMingCanShu.indexOf("?") + 4);
            } else if (yuMingCanShu.length() > 4) {
               yuMingCanShu = yuMingCanShu.replace("/", "");
               yuMingCanShu = yuMingCanShu.substring(3);
            }
         }
      } catch (Exception var13) {
         System.out.println("getSiteYumingerr!!!");
         yuMingCanShu = yuMingCanShu.replace("/", "");
         yuMingCanShu = ReadTemp.getIndex(yuMing, yuMingCanShu, 1);
         getSiteYuming(yuMingCanShu, yuMing);
      }

      String webKey = "";
      Pattern pattern = Pattern.compile("([a-z]){4}");

      for(Matcher matcher = pattern.matcher(yuMingCanShu); matcher.find(); webKey = webKey + matcher.group()) {
      }

      String productId = "";
      Pattern patternId = Pattern.compile("[0-9]\\d{0,6}");

      for(Matcher matcherId = patternId.matcher(yuMingCanShu); matcherId.find(); productId = productId + matcherId.group()) {
      }

      Map map = readConfigWeb();
      String value = "";
      Iterator var11 = map.keySet().iterator();

      while(var11.hasNext()) {
         Object key = var11.next();
         if (key.equals(webKey)) {
            value = (String)map.get(key);
            break;
         }
      }

      yuMingCanShu = "../site/" + value + "/p" + productId + ".txt";
      return yuMingCanShu;
   }

   public String gotoHtml(String yuMing, String yuMingCanShu) {
      String value = "";
      String productId = "";
      String yuMingCanShuTemp = "";

      try {
         yuMingCanShuTemp = yuMingCanShu.replace("/", "");
         if (yuMingCanShuTemp.indexOf("/") > 0) {
            yuMingCanShuTemp = yuMingCanShuTemp.substring(4);
         } else {
            yuMingCanShuTemp = yuMingCanShuTemp.substring(3);
         }

         yuMingCanShuTemp = yuMingCanShuTemp.substring(0, yuMingCanShuTemp.length() - 5);
         Map map = readConfigWeb();
         String webKey = "";
         Pattern pattern = Pattern.compile("([a-z]){4}");

         for(Matcher matcher = pattern.matcher(yuMingCanShuTemp); matcher.find(); webKey = webKey + matcher.group()) {
         }

         Pattern patternId = Pattern.compile("[0-9]\\d{0,6}");

         for(Matcher matcherId = patternId.matcher(yuMingCanShuTemp); matcherId.find(); productId = productId + matcherId.group()) {
         }

         Iterator var12 = map.keySet().iterator();

         while(var12.hasNext()) {
            Object key = var12.next();
            if (key.equals(webKey)) {
               value = (String)map.get(key);
               break;
            }
         }
      } catch (Exception var14) {
         ReadTemp readTemp = new ReadTemp();
         yuMingCanShu = ReadTemp.getIndex(yuMing, yuMingCanShu, 1);
         this.gotoHtml(yuMing, yuMingCanShu);
      }

      String html = "https://" + value + "/index.php?main_page=product_info&products_id=" + productId;
      return html;
   }
}
