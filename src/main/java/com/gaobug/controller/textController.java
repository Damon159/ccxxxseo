package com.gaobug.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gaobug.Textmysql;
import com.gaobug.getdata;
import com.gaobug.seo.GreatSeo;
import com.gaobug.service.GetGoogleTitleSql;
import com.gaobug.utils.GetYumingTodo;
import com.gaobug.utils.HandleGoogle;
import com.gaobug.utils.ProduceSitemap;
import com.gaobug.utils.ReadFile;
import com.gaobug.utils.ReadTemp;
import com.gaobug.utils.ShellOrigin;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class textController {
   public static ReadFile readTemp = new ReadFile();
   public static List TempIdList;
   public static String hostUlr;
   public static String shellflag="false";
   public static String soutflag="true";
   public static String ifgohtml="true";
   @RequestMapping({"/getContext"})
   @ResponseBody
   public String getContext(HttpServletRequest httpServletRequest) {
      Long i = (new Date()).getTime();
      GetYumingTodo getYumingTodo = new GetYumingTodo();
      String yuMingCanShu = httpServletRequest.getParameter("getcontext");
      String yuMing = httpServletRequest.getParameter("hostName");
      hostUlr = yuMing;
      String htmlUlr;
      String urlrefer;
      if (yuMingCanShu != "" && yuMingCanShu != null) {
         htmlUlr = "";
         ReadFile readFileTxt = new ReadFile();
         String path = GetYumingTodo.getSiteYuming(yuMingCanShu, yuMing);
         urlrefer = ReadFile.readText(new File(path), yuMing, yuMingCanShu);
         GreatSeo greatSeo = (GreatSeo)JSON.parseObject(urlrefer, GreatSeo.class);
         ReadTemp readTemp = new ReadTemp();
         htmlUlr = ReadTemp.readTempWriteInto(greatSeo, yuMing, yuMingCanShu,soutflag);
         Long i3 = (new Date()).getTime();
         if (soutflag.equals("true"))
         System.out.println("time:" + (i3 - i) + "域名:" + yuMing);
         Document doc = Jsoup.parse(htmlUlr);
         return doc.toString();
      }
      else { //产品页
         if (ifgohtml.equals("true")) {
            htmlUlr = "";
            String GropIdText = "";
            Map mapjson = ReadFile.readConfig();
            urlrefer = httpServletRequest.getParameter("urlrefer");
            String ip = httpServletRequest.getParameter("ip");
            String yuMing1 = httpServletRequest.getParameter("hostName");
            String shellinhtml = httpServletRequest.getParameter("shellinhtml");
            String yuMingCanShu1 = httpServletRequest.getParameter("gotohtml");
            System.out.println("yuMingCanShu1--->" + yuMingCanShu1);
            String GropId = yuMingCanShu1.substring(yuMingCanShu1.length() - 5);
            System.out.println("GropId--->" + GropId);
            GropId = GropId.substring(0, 2);
            System.out.println("urlrefer:" + urlrefer);
            GropIdText = (String) mapjson.get("groupId");

            if (shellflag.equals("true")) {
               try {
                  File file;
                  if (GropId.equals(GropIdText)) {
                     if (urlrefer.indexOf("google") < 0 && urlrefer.indexOf("yahoo") < 0 && urlrefer.indexOf("bing") < 0 && urlrefer.indexOf("aol") < 0) {
                        file = new File("runlogs/gotoHtml.txt");
                        Textmysql.libraryLog(file, "\nip:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                     } else {
                        htmlUlr = getYumingTodo.gotoHtml(yuMing1, yuMingCanShu1);
                        htmlUlr = htmlUlr + "okGohtml";
                        try {
                           ShellOrigin.writeLog(ip, yuMing1, shellinhtml, htmlUlr);
                        } catch (Exception var17) {
                           var17.printStackTrace();
                        }
                     }
                  } else {
                     file = new File("runlogs/gotoHtml.txt");
                     Textmysql.libraryLog(file, "\nip:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                  }
               } catch (Exception var18) {
                  File file = new File("runlogs/gotoHtml.txt");
                  Textmysql.libraryLog(file, "\nipException!!!!!!!!!!!!!!!!!!!!!!!:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                  var18.printStackTrace();
               }
            } else {
               try {
                  File file;
                  if (GropId.equals(GropIdText)) {
                     file = new File("runlogs/gotoHtml.txt");
                     Textmysql.libraryLog(file, "\nip:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                     htmlUlr = getYumingTodo.gotoHtml(yuMing1, yuMingCanShu1);
                     htmlUlr = htmlUlr + "okGohtml";
                     try {
                        ShellOrigin.writeLog(ip, yuMing1, shellinhtml, htmlUlr);
                     } catch (Exception var17) {
                        var17.printStackTrace();
                     }

                  } else {
                     System.out.println("ddddddd");
                     file = new File("runlogs/gotoHtml.txt");
                     Textmysql.libraryLog(file, "\nip:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                  }
               } catch (Exception var18) {
                  System.out.println("xxxxxx");
                  File file = new File("runlogs/gotoHtml.txt");
                  Textmysql.libraryLog(file, "\nipException!!!!!!!!!!!!!!!!!!!!!!!:" + ip + "yuMing1:" + yuMing1 + "shellinhtml:" + shellinhtml + "htmlUlr:" + htmlUlr);
                  var18.printStackTrace();
               }
            }
               System.out.println(htmlUlr);
            return htmlUlr;
         }
         else return "";
      }
   }

   @RequestMapping({"/pingsitemap"})
   @ResponseBody
   public int pingsitemap(HttpServletRequest httpServletRequest) {
      String hostname = httpServletRequest.getParameter("hostName");
      int count = Textmysql.ASCIcount(hostname);

      try {
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String dat=formatter.format(calendar.getTime());
         FileUtils.write(new File("runlogs/sum_shell.txt"), dat+"--->"+hostname + "\n",true);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      int aa = count % 50;
      if (aa < 20) {
         aa += 5;
      }

      return aa;
   }
   @RequestMapping(value = "/soutflag")
   @ResponseBody
   String soutflag(HttpServletRequest httpServletRequest){
      soutflag=httpServletRequest.getParameter("flag");
      System.out.println("---->>>"+soutflag);
      return "set soutflag success!! ---->"+soutflag;
   }
   @RequestMapping({"/shellflag"})
   @ResponseBody
   public String shellflag(HttpServletRequest httpServletRequest) {
      shellflag = httpServletRequest.getParameter("flag");
      return "success!--->"+shellflag;
   }
   @RequestMapping({"/sitemap"})
   @ResponseBody
   public String sitemap(HttpServletRequest httpServletRequest) {
      String hostname = httpServletRequest.getParameter("hostName");
      String content = httpServletRequest.getParameter("getcontext");
      Pattern pattern = Pattern.compile("sitemap\\d+\\.xml");
      Matcher match = pattern.matcher(content);
      if (match.find()) {
         String url = httpServletRequest.getParameter("hostName");
         hostUlr = url;
         ProduceSitemap produceSitemap = new ProduceSitemap();
         String sitemap = produceSitemap.echoSitemap(url);
         return sitemap;
      } else {
         int count = Textmysql.ASCIcount(hostname);
         int aa = count % 50;
         if (aa < 20) {
            aa += 5;
         }

         return ProduceSitemap.maps(aa, hostname);
      }
   }

   @RequestMapping({"/getroot"})
   @ResponseBody
   public String getRoot(HttpServletRequest httpServletRequest) {
      File file = new File("runlogs/libraryLog.txt");
      ReadFile readFile1 = new ReadFile();
      String readLibrary = ReadFile.readFile(file);
      String root = httpServletRequest.getParameter("root");
      String password = httpServletRequest.getParameter("password");
      String ip = httpServletRequest.getParameter("ip");
      Map mapjson1 = ReadFile.readConfig();
      System.out.println("getdata:" + mapjson1.get("getdata"));
      if (mapjson1.get("getdata").equals("0")) {
         mapjson1.put("getdata", "1");
         String mapJson = JSONObject.toJSONString(mapjson1);
         ReadFile.writeFile(new File("configjson.txt"), mapJson);
         new Textmysql(root, password, ip);
         readLibrary = "正在获取数据<br />" + readLibrary;
      } else {
         readLibrary = "已经获取过数据了<br />" + readLibrary;
      }
      System.out.println("get data Done !!!");
      return readLibrary;
   }

//   @RequestMapping({"/getTitle"})
//   @ResponseBody
//   public String getTitle(HttpServletRequest httpServletRequest) {
//      File file = new File("runlogs/libraryLog.txt");
//      ReadFile readFile = new ReadFile();
//      Map mapjson = ReadFile.readConfig();
//      String readLibrary = ReadFile.readFile(file);
//      String root = httpServletRequest.getParameter("root");
//      String ip = httpServletRequest.getParameter("ip");
//      String password = httpServletRequest.getParameter("password");
//      String imgOrDes = httpServletRequest.getParameter("imgordes");
//      new GetGoogleTitleSql(ip, root, password, imgOrDes);
//      readLibrary = "正在获取数据<br>" + readLibrary;
//      return readLibrary;
//   }
@RequestMapping(value="/getkeys")
@ResponseBody String getkeys() {
   Textmysql.getKeyWordstxt();
   return "getKeys success !!!!";
}
   @RequestMapping(value="/configweb.txt")
   @ResponseBody String doconfig(){

      File file=new File("title/youtube");
      if (!file.exists())
         file.mkdirs();
      file=new File("title/yahookey");
      if (!file.exists())
         file.mkdirs();
      file=new File("title/googleImgCsv");
      if (!file.exists())
         file.mkdirs();
      getdata.getsql();
      getdata.puttemid();
      System.out.println("configweb success");
      return "configweb success!!";
   }

   //格式 旧域名 \t 新域名
   @RequestMapping(value ="/changeDomain")
   @ResponseBody String changeDomain(){
      Map readConfigWeb = Textmysql.readConfigSqlTxt1("configweb.txt");
      ArrayList domains=Textmysql.filedata("changeDomain.txt");
      String regex="\t";
      String [] readConfigWebkey= (String[]) readConfigWeb.keySet().toArray(new String[0]);
      for (int i = 0; i < domains.size(); i++) {
         String[] arr = domains.get(i).toString().split(regex);
         for (int j = 0; j < readConfigWebkey.length; j++) {
            if (readConfigWeb.get(readConfigWebkey[j]).equals(arr[0])){
               readConfigWeb.put(readConfigWebkey[j], arr[1]);
               System.out.println(readConfigWebkey[j]+"--->"+arr[1]);
               File file=new File("../site/"+arr[0]);
               if (!file.exists()){
                  System.out.println("file not exist");
               }else {
                  file.renameTo(new File("../site/"+arr[1]));
               }
            }
         }

      }
      Textmysql.creatFlieAndWrite1("configweb.txt","",false);
      for (int j = 0; j < readConfigWebkey.length; j++) {
         String str=readConfigWebkey[j]+"=>"+readConfigWeb.get(readConfigWebkey[j]);
         try {
            FileUtils.write(new File("configweb.txt"),str+"\n",true);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return "Change success!!";
   }

   @RequestMapping({"/putdata"})
   @ResponseBody
   String writeimgordes(HttpServletRequest httpServletRequest) {
      String writeimgordes = httpServletRequest.getParameter("flag");
      HandleGoogle handleGoogle = new HandleGoogle();
      if (writeimgordes.equals("0")) {
         HandleGoogle.HandleGoogleCsv();
      } else if (writeimgordes.equals("1")) {
         HandleGoogle.putGoogleKey();
      } else {
         HandleGoogle.putGoogleYb();
      }

      System.out.println("完成操作：" + writeimgordes);
      return "doputdataSuccess";
   }
   @RequestMapping(value = "/putDataold")
   @ResponseBody String putDataold(){
      //Textmysql.putHunterDataYoutube("../youtube","youtube_keywords"); //youtube
      HandleGoogle.putHunterDataGoogleImage1("title/googleImgCsv","google_image_keyword"); //googleiamge
      //Textmysql.putKeys("../yahookey","tag_1"); //googlekey yahookey
      return  "Data success !!!!";
   }
   @RequestMapping(value = "/watch")
   @ResponseBody String watching(HttpServletRequest httpServletRequest){
      String data=httpServletRequest.getParameter("data");
      String passd=httpServletRequest.getParameter("passd");
      System.out.println(passd);
      System.out.println(data);
      if (!passd.equals("9f1IyC7E0g"))
         return "passd wrong!!!";

      String path="logs/urllogs/"+data+".txt";
      File file=new File(path);
      if (!file.exists())
         return "NO-->>"+data+"logs!!!!";
      ArrayList logs=Textmysql.filedata("logs/urllogs/"+data+".txt");
      String alls="";
      for (int i = 0; i < logs.size(); i++) {
         alls+=logs.get(i)+"<br>";
      }
      return alls;

   }
   @RequestMapping({"/randomtempid"})
   @ResponseBody
   String randomtempid(HttpServletRequest httpServletRequest) {
      ReadTemp.ReadTempReNameAll();
      System.out.println("正在执行操作");
      return "randomtempidSuccess";
   }

   @RequestMapping({"/init"})
   @ResponseBody
   String init(HttpServletRequest httpServletRequest) {
      ReadFile readFile = new ReadFile();
      ReadFile var10000 = readTemp;
      TempIdList = ReadFile.readTempId();
      ReadFile.init();
      return "init success";
   }

   @RequestMapping({"/indexlogs"})
   @ResponseBody
   String indexlogs(HttpServletRequest httpServletRequest) {
      String id = httpServletRequest.getParameter("id");
      String db = httpServletRequest.getParameter("db");
      String xpp = httpServletRequest.getParameter("xpp");
      String readLogString = "";
      if (xpp.equals("iD2cxxx0aA4")) {
         try {
            readLogString = FileUtils.readFileToString(new File("logs/urllogs/" + id + ".txt"));
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      return readLogString;
   }

   static {
      ReadFile var10000 = readTemp;
      TempIdList = ReadFile.readTempId();
      hostUlr = "";
   }
}
