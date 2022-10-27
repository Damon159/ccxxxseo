package com.gaobug.utils;

import com.alibaba.fastjson.JSON;
import com.gaobug.controller.textController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class ReadFile {
   static textController te = new textController();
   static ProduceSitemap produceSitemap = new ProduceSitemap();

   public static String readText(File file, String yuMing, String yuMingCanShu) {
      boolean flag = file.exists();
      String contextJSon = null;
      if (flag) {
         try {
            contextJSon = FileUtils.readFileToString(file);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      } else {
         try {
            yuMingCanShu = yuMingCanShu.replace("/", "");

            while(contextJSon == null) {
               yuMingCanShu = ReadTemp.getIndex(yuMing, yuMingCanShu, 1);
               GetYumingTodo getYumingTodo = new GetYumingTodo();
               contextJSon = FileUtils.readFileToString(new File(GetYumingTodo.getSiteYuming(yuMingCanShu, yuMing)), "UTF-8");
               if (contextJSon != null) {
                  break;
               }
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      return contextJSon;
   }

   public static int getConfig(String siteCharacter) {
      if (siteCharacter == null) {
         siteCharacter = "1234561";
      }

      char[] cs = siteCharacter.toCharArray();
      int sum = 0;
      char[] var3 = cs;
      int var4 = cs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int i = var3[var5];
         sum += i;
      }

      return sum;
   }

   public static Map readConfig() {
      String contextJson = "";

      try {
         contextJson = FileUtils.readFileToString(new File("configjson.txt"));
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      Map mapJson = (Map)JSON.parseObject(contextJson, Map.class);
      return mapJson;
   }

   public static String readFile(File file) {
      String readContent = "";

      try {
         readContent = FileUtils.readFileToString(file);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return readContent;
   }

   public static String writeFile(File file, String content) {
      String writeContent = "";

      try {
         FileUtils.write(file, content);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return writeContent;
   }

   public static List readTempId() {
      List list = new ArrayList();
      File file = new File("tempId.txt");

      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         String s = null;

         while((s = br.readLine()) != null) {
            list.add(s);
         }

         br.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return list;
   }

   public static int getListReturnIndex(List list, String siteYuMing) {
      int indexSY = 0;

      for(int i = 0; i < list.size(); ++i) {
         if (list.get(i).equals(siteYuMing)) {
            indexSY = i;
            break;
         }
      }

      return indexSY;
   }

   public static Map<String, Object> getTempValue() {
      Map<String, Object> tempMap = new HashMap();
      File folder = new File("temp");
      File[] listFolder = folder.listFiles();
      File[] var3 = listFolder;
      int var4 = listFolder.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File file = var3[var5];
         if (file.isFile()) {
            try {
               String content = FileUtils.readFileToString(new File("temp/" + file.getName()));
               tempMap.put(file.getName(), content);
            } catch (IOException var8) {
               var8.printStackTrace();
            }
         }
      }

      return tempMap;
   }

   public static void init() {
      File tempId = new File("tempId.txt");
      if (!tempId.exists()) {
         try {
            tempId.createNewFile();
         } catch (IOException var14) {
            var14.printStackTrace();
         }
      }

      ReadTemp tempMap = new ReadTemp();
      ReadTemp.tempMap = getTempValue();
      File fileSite = new File("site");
      if (!fileSite.exists()) {
         fileSite.mkdir();
      }

      File fileDes = new File("title/googleDesCsv");
      if (!fileDes.exists()) {
         fileDes.mkdir();
      }

      File fileImg = new File("title/googleImgCsv");
      if (!fileImg.exists()) {
         fileImg.mkdir();
      }

      File fileYb = new File("title/googleImgCsv");
      if (!fileYb.exists()) {
         fileYb.mkdir();
      }

      File runlogs = new File("runlogs");
      if (!runlogs.exists()) {
         runlogs.mkdir();
      }

      File runlogsTxt = new File("runlogs/libraryLog.txt");
      if (!runlogsTxt.exists()) {
         try {
            runlogsTxt.createNewFile();
         } catch (IOException var13) {
            var13.printStackTrace();
         }
      }

      File sum_shell = new File("runlogs/ sum_shell.txt");
      if (!sum_shell.exists()) {
         try {
            sum_shell.createNewFile();
         } catch (IOException var12) {
            var12.printStackTrace();
         }
      }

      File runlogsGotoTxt = new File("runlogs/gotoHtml.txt");
      if (!runlogsGotoTxt.exists()) {
         try {
            runlogsGotoTxt.createNewFile();
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }

      File temp = new File("temp");
      if (!temp.exists()) {
         temp.mkdir();
      }

      ReadTemp.ReadTempReNameAll();
   }
}
