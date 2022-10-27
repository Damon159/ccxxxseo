package com.gaobug.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShellOrigin {
   public static void writeLog(String ip, String hostname, String shellUrl, String gotohtml) {
      try {
         hostname = hostname.replace("www.", "");
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
         String dat = formatter.format(calendar.getTime());
         String result = dat + "\t" + ip + "\t" + hostname + "\t" + shellUrl + "\t" + gotohtml + "\n";
         creatFlieAndWrite("logs/urllogs/" + dat + ".txt", result, true);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void creatFlieAndWrite(String path, String context, Boolean append) {
      try {
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String dat = formatter.format(calendar.getTime());
         System.out.println(dat);
         File file1 = new File("logs/urllogs/" + dat + ".txt");
         if (!file1.exists() && dat.indexOf(".txt") == -1) {
            file1.getParentFile().mkdirs();
         } else {
            try {
               file1.createNewFile();
            } catch (IOException var10) {
               var10.printStackTrace();
            }
         }

         try {
            FileWriter fw = new FileWriter(file1, append);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(context);
            bw.flush();
            bw.close();
            fw.close();
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }
}
