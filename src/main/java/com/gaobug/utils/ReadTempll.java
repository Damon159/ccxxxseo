package com.gaobug.utils;

import com.alibaba.fastjson.JSON;
import com.gaobug.controller.textController;
import com.gaobug.seo.GreatSeo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class ReadTempll {
   static ProduceSitemap produceSitemap = new ProduceSitemap();
   static ReadFile readFile = new ReadFile();
   static Map tempMap;
   static List hostList;
   static textController te;
   static Map mapConfig;
   static ProductLdJson productLdJson;

   public static String readTempWriteInto(GreatSeo greatSeo, String yuMing, String yuMingCanShu) {
      yuMingCanShu = yuMingCanShu.replace("/", "");
      String tempContext = "";
      Object mapGreatSeo = new HashMap();

      try {
         mapGreatSeo = convert(greatSeo);
      } catch (Exception var15) {
         var15.printStackTrace();
      }

      String tempYuming = yuMing + yuMingCanShu;
      tempYuming = replaceWwwOrIndex(tempYuming);
      int tempCount = getTempCount(tempYuming);
      tempContext = (String)tempMap.get(tempCount + ".html");
      if (tempContext == null) {
         int i = 0;

         while(tempContext == null) {
            ++i;
            tempCount = getTempCount(yuMing + i);
            tempContext = (String)tempMap.get(tempCount + ".html");
            if (tempContext != null) {
               break;
            }
         }
      }

      try {
         while(greatSeo == null) {
            yuMingCanShu = getIndex(yuMing, yuMingCanShu, 1);
            GetYumingTodo getYumingTodo = new GetYumingTodo();
             /*
            新增判断文件是否存在
             */
            String path =GetYumingTodo.getSiteYuming(yuMingCanShu, yuMing);
            File file=new File(path);
            if (file.exists()){
               String contextJson = FileUtils.readFileToString(new File(path), "UTF-8");
               greatSeo = (GreatSeo)JSON.parseObject(contextJson, GreatSeo.class);
            }
            if (greatSeo != null) {
               break;
            }

            try {
               mapGreatSeo = convert(greatSeo);
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }

         List google_desH_list = Collections.singletonList(((Map)mapGreatSeo).get("google_desH"));
         List google_desP_list = Collections.singletonList(((Map)mapGreatSeo).get("google_desP"));
         String[] productUlr = tempContext.split("\\{#products_url}");
         List productList = getSomeSiteUlr(yuMing, yuMingCanShu, productUlr.length + 1);
         productList = produceHtmlAUrl(productList, productUlr.length + 1, yuMing, yuMingCanShu);
         tempContext = spitTempProductUrl(tempContext, productList);

         try {
            tempContext = tempContext.replaceAll("\\{#href}", yuMingCanShu);
            tempContext = tempContext.replaceAll("\\{#href#}", yuMingCanShu);
            tempContext = tempContext.replaceAll("\\{#reviews}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_related") == null ? "" : ((Map)mapGreatSeo).get("product_related") + ""));
            tempContext = tempContext.replaceAll("\\{#class_name}", "servlet");
            tempContext = tempContext.replaceAll("\\{#class_name_1}", "servlet");
            tempContext = tempContext.replaceAll("\\{#products_image}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_main_img") == null ? "" : replaceImage(((Map)mapGreatSeo).get("product_main_img") + "", ((Map)mapGreatSeo).get("product_name") + "")));
            String tempdes = ((Map)mapGreatSeo).get("product_description") != null && ((Map)mapGreatSeo).get("product_description") != "" ? String.valueOf(((Map)mapGreatSeo).get("product_description")) : "";
            tempdes = tempdes.replaceAll("<h2>", "");
            tempdes = tempdes.replaceAll("</h2>", "");
            tempContext = tempContext.replaceAll("\\{#products_description}", Matcher.quoteReplacement(tempdes));
            tempContext = tempContext.replaceAll("\\{#breadcrumbs}", breadCrumbs(yuMingCanShu, (String)((Map)mapGreatSeo).get("product_cate1"), (String)((Map)mapGreatSeo).get("product_cate2"), (String)((Map)mapGreatSeo).get("product_cate3"), (String)((Map)mapGreatSeo).get("product_name"), yuMing));

            String breadcrumbsLdJson=Matcher.quoteReplacement(ProductLdJson.breadCrumbsLdJson(yuMingCanShu, (String)((Map)mapGreatSeo).get("product_cate1"), (String)((Map)mapGreatSeo).get("product_cate2"), (String)((Map)mapGreatSeo).get("product_cate3"), (String)((Map)mapGreatSeo).get("product_name"), yuMing));
            String productLdJson=Matcher.quoteReplacement(ProductLdJson.productJson(greatSeo, yuMing + "/?" + yuMingCanShu));

            //tempContext = tempContext.replace("{#json_data}", "<script type=\"application/ld+json\">\n"+breadcrumbsLdJson+"\n"+"</script>\n<script type=\"application/ld+json\">\n"+productLdJson+"\n</script>\n");

            tempContext=tempContext.replaceAll("\\{#json_data}","");

            tempContext=tempContext.replaceAll("\\{#remain_tag_1}","");
            tempContext=tempContext.replaceAll("\\{#remain_tag_2}","");
            tempContext = tempContext.replaceAll("\\{#breadcrumbsLdJson}", "");
            tempContext=tempContext.replaceAll("\\{#productLdJson}","");
            tempContext = tempContext.replaceAll("\\{#img_title}", (String)((Map)mapGreatSeo).get("product_name"));
            tempContext = tempContext.replaceAll("\\{#h1title\\d}", ((Map)mapGreatSeo).get("product_name") == null ? "" : (String)((Map)mapGreatSeo).get("product_name"));
            String[] hArray = tempContext.split("\\{#h\\dtitle\\d}");
            tempContext = replaceGoogleDes(hArray, (List)(((Map)mapGreatSeo).get("google_desH") != "" && ((Map)mapGreatSeo).get("google_desH") != null ? greatSeo.getGoogle_desH() : new ArrayList()), "0");
            String[] pArray = tempContext.split("\\{#googleDes}");
            tempContext = replaceGoogleDes(pArray, (List)(((Map)mapGreatSeo).get("google_desP") != "" && ((Map)mapGreatSeo).get("google_desP") != null ? greatSeo.getGoogle_desP() : new ArrayList()), "1");
            tempContext = tempContext.replaceAll("<h\\d>\\{#h\\dtitle\\d}</h\\d>", "");
            tempContext = tempContext.replaceAll("\\{#googleDes}", "");
            tempContext = tempContext.replaceAll("\\{#title}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_name") == null ? "" : (String)((Map)mapGreatSeo).get("product_name")));
            tempContext = tempContext.replaceAll("\\{#shell_link}", "");
            tempContext = tempContext.replaceAll("\\{#rand_title}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("google_img_sum") == null ? "" : (String)((Map)mapGreatSeo).get("google_img_sum")));
            tempContext = tempContext.replaceAll("\\{#img_title}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_name") == null ? "" : (String)((Map)mapGreatSeo).get("product_name")));
            tempContext = tempContext.replaceAll("\\{#meta_keywords}", Matcher.quoteReplacement((String)((Map)mapGreatSeo).get("product_cate3") + ((Map)mapGreatSeo).get("product_cate2") + ((Map)mapGreatSeo).get("product_name")));
            tempContext = tempContext.replaceAll("\\{#current_url}", yuMingCanShu + yuMing);
            tempContext = tempContext.replaceAll("\\{#products_image_url}", ((Map)mapGreatSeo).get("product_main_img") == null ? ((Map)mapGreatSeo).get("product_main_img") + "" : "");
            tempContext = tempContext.replaceAll("\\{#google_images}", Matcher.quoteReplacement(String.valueOf(((Map)mapGreatSeo).get("google_img"))));
            if (((Map)mapGreatSeo).get("google_desH") != "[]") {
               tempContext = tempContext.replaceAll("\\{#back_tag_1}", Matcher.quoteReplacement((String)((Map)mapGreatSeo).get("product_cate3") + ((Map)mapGreatSeo).get("product_name") + greatSeo.getGoogle_desH().get(0) + " " + ((Map)mapGreatSeo).get("product_cate2") + ((Map)mapGreatSeo).get("product_main_img") == null ? "" : replaceImage(((Map)mapGreatSeo).get("product_main_img") + "", ((Map)mapGreatSeo).get("product_name") + "")));
               tempContext = tempContext.replaceAll("\\{#meta_keywords}", Matcher.quoteReplacement((String)((Map)mapGreatSeo).get("product_name") + greatSeo.getGoogle_desH().get(0) + ((Map)mapGreatSeo).get("product_cate3")));
               tempContext = tempContext.replaceAll("\\{#meta_title}", Matcher.quoteReplacement((String)((Map)mapGreatSeo).get("product_cate3") + ((Map)mapGreatSeo).get("product_name") + greatSeo.getGoogle_desH().get(0) + " " + ((Map)mapGreatSeo).get("product_cate2")));
            } else {
               tempContext = tempContext.replaceAll("\\{#back_tag_1}", Matcher.quoteReplacement((String)((Map)mapGreatSeo).get("product_cate3") + ((Map)mapGreatSeo).get("product_name") + ((Map)mapGreatSeo).get("product_cate2")));
               tempContext = tempContext.replaceAll("\\{#meta_title}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_cate3") + " " + ((Map)mapGreatSeo).get("product_name") + ((Map)mapGreatSeo).get("product_cate2")));
               tempContext = tempContext.replaceAll("\\{#meta_keywords}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_cate3") + " " + ((Map)mapGreatSeo).get("product_name")));
            }

            if (((Map)mapGreatSeo).get("google_desP") != "[]") {
               tempContext = tempContext.replaceAll("\\{#meta_description}", Matcher.quoteReplacement(greatSeo.getGoogle_desP().get(0) + "" + ((Map)mapGreatSeo).get("product_name") + ((Map)mapGreatSeo).get("product_cate2")));
            } else {
               tempContext = tempContext.replaceAll("\\{#meta_description}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("product_cate3") + " " + greatSeo.getProduct_name() + ((Map)mapGreatSeo).get("product_cate2")));
            }

            tempContext = tempContext.replaceAll("\\{#h\\dtitle1\\d}", "");
            tempContext = tempContext.replaceAll("\\{#h\\dtitle2\\d}", "");
            tempContext = tempContext.replaceAll("\\{#h\\dtitle3\\d}", "");
            tempContext = tempContext.replaceAll("<h3></h3>", "");
            tempContext = tempContext.replaceAll("<h2></h2>", "");
            tempContext = tempContext.replaceAll("<h4></h4>", "");
            tempContext = tempContext.replaceAll("<h5></h5>", "");
            tempContext = tempContext.replaceAll("\\{#view}", Matcher.quoteReplacement(dealWithYouTobe(greatSeo.getUbView(), yuMing) == "" ? "" : dealWithYouTobe(greatSeo.getUbView(), yuMing)));
            tempContext = tempContext.replaceAll("\\{#youtube}", Matcher.quoteReplacement(((Map)mapGreatSeo).get("ub") == null ? "" : (String)((Map)mapGreatSeo).get("ub")));
         } catch (Exception var16) {
            System.out.println("报错readTempWriteInto：模板：" + tempCount + "路径：" + yuMing);
            var16.printStackTrace();
         }
      } catch (IOException var17) {
         System.out.println("报错readTempWriteInto-最后try：模板：" + tempCount + "路径：" + yuMing);
         var17.printStackTrace();
      }

      return tempContext;
   }

   public static Map<Object, Object> convert(Object object) throws Exception {
      Map<Object, Object> map = new HashMap();
      Class<?> clazz = object.getClass();
      Field[] var3 = clazz.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         field.setAccessible(true);
         String value = field.get(object) != null ? field.get(object).toString() : "";
         map.put(field.getName(), value);
      }

      return map;
   }

   public static String replaceGoogleDes(String[] desArr, List list, String des) {
      String returnHtml = "";

      for(int i = 0; i < desArr.length; ++i) {
         if (list.size() > i) {
            if (i == desArr.length - 1) {
               if (des.equals("0")) {
                  returnHtml = returnHtml + list.get(i) + desArr[i];
               } else if (i / 3 == 0) {
                  returnHtml = returnHtml + "<p>" + list.get(i) + "</p>" + desArr[i];
               } else {
                  returnHtml = returnHtml + "<span>" + list.get(i) + "</span>" + desArr[i];
               }
            } else if (des.equals("0")) {
               returnHtml = returnHtml + desArr[i] + list.get(i);
            } else if (i / 3 == 0) {
               returnHtml = returnHtml + desArr[i] + "<p>" + list.get(i) + "</p>";
            } else {
               returnHtml = returnHtml + desArr[i] + "<span>" + list.get(i) + "</span>";
            }
         } else {
            returnHtml = returnHtml + desArr[i];
         }
      }

      return returnHtml;
   }

   public static String replaceImage(String image, String productName) {
      String img = "<image src=\"" + image + "\" alt=\"" + productName + "\" title=\"" + productName + "\" />";
      return img;
   }

   public static int getTempCount(String siteString) {
      boolean var1 = false;

      int lastCountValues;
      try {
         lastCountValues = getConfig(siteString);
         String readTempCon = (String)tempMap.get("tempcount.txt");
         lastCountValues %= Integer.parseInt(readTempCon);
      } catch (Exception var3) {
         lastCountValues = 0;
      }

      return lastCountValues;
   }

   public static String breadCrumbs(String yuMingCanShu, String cate1, String cate2, String cate3, String productName, String yuMing) {
      String productUrlOne = GetOneSameUrl(yuMing, cate1, yuMingCanShu);
      String productUrlTwo = GetOneSameUrl(yuMing, cate2, yuMingCanShu);
      String productUrlThree = GetOneSameUrl(yuMing, cate3, yuMingCanShu);
      String generate = "<ol itemscope itemtype=\"https://schema.org/BreadcrumbList\"><li><a href=\"" + yuMing + "\"title=\"ホーム\">ホーム</a></li>";
      cate1 = cate1.trim();
      if (cate1 != null && cate1 != "") {
         generate = generate + "<li><a href=\"" + productUrlOne + "\"title=\"" + cate1 + "\">" + cate1 + "</a></li>";
      }

      if (cate2 != null && cate2 != "") {
         generate = generate + "<li><a href=\"" + productUrlTwo + "\"title=\"" + cate2 + "\">" + cate2 + "</a></li>";
      }

      if (cate3 != null && cate3 != "") {
         generate = generate + "<li><a href=\"" + productUrlThree + "\"title=\"" + cate3 + "\">" + cate3 + "</a></li>";
      }

      yuMingCanShu = yuMingCanShu.replaceAll("/", "");
      yuMingCanShu = yuMing + "?" + yuMingCanShu;
      if (productName != null) {
         generate = generate + "<li><a href=\"" + yuMingCanShu + "\"title=\"" + productName + "\">" + productName + "</a></li>";
      }

      generate = generate + "</ol>";
      return generate;
   }

   public static List getSomeSiteUlr(String yuMing, String yuMingCanShu, int count) {
      List list = new ArrayList();
      String webKey;
      if (!yuMingCanShu.equals("/") && yuMingCanShu.length() >= 3 && !yuMingCanShu.equals("/index.php")) {
         try {
            String startThree = ProduceSitemap.shellThree(yuMingCanShu);
            yuMingCanShu = yuMingCanShu.substring(3);
            yuMingCanShu = yuMingCanShu.substring(0, yuMingCanShu.length() - 5);
            webKey = "";
            Pattern pattern = Pattern.compile("([a-z]){4}");

            for(Matcher matcher = pattern.matcher(yuMingCanShu); matcher.find(); webKey = webKey + matcher.group()) {
            }

            String productId = "";
            Pattern patternId = Pattern.compile("[0-9]\\d{0,6}");

            for(Matcher matcherId = patternId.matcher(yuMingCanShu); matcherId.find(); productId = productId + matcherId.group()) {
            }

            textController var10000 = te;
            List list1 = textController.TempIdList;
            int index = ReadFile.getListReturnIndex(list1, webKey + productId);
            if (index == 0) {
               index = getConfig(yuMing + yuMingCanShu);
            }

            int suoying = getConfig(webKey + productId);

            for(int i = 1; i <= count; ++i) {
               suoying = index + suoying + i;
               if (suoying == list.size()) {
                  ++suoying;
               }

               if (suoying > list1.size()) {
                  suoying %= list1.size();
               }

               String zhi = (String)list1.get(suoying);
               ProduceSitemap var23 = produceSitemap;
               String md5GetWeb = ProduceSitemap.string2MD5(zhi);
               String tempUrl = "/?" + startThree + zhi + md5GetWeb;
               if (tempUrl.indexOf("/?") < 0) {
                  System.out.println("Not found /? !!!!!!");
                  tempUrl = "/?" + startThree + zhi + md5GetWeb;
               }

               String url = yuMing + tempUrl;
               list.add(url);
            }
         } catch (Exception var19) {
            for(int i = 1; i <= count; ++i) {
               String url = yuMing + "/?" + getIndex(yuMing, yuMingCanShu, i);
               list.add(url);
            }
         }
      } else {
         for(int i = 1; i <= count; ++i) {
            yuMingCanShu.replace("index.php", "");
            yuMingCanShu = replaceWwwOrIndex(yuMingCanShu);
            webKey = yuMing + "/?" + getIndex(yuMing, yuMingCanShu, i);
            list.add(webKey);
         }
      }

      return list;
   }

   public static String getIndex(String yuMing, String yuMingCanShu, int i) {
      yuMing = replaceWwwOrIndex(yuMing);
      textController var10000 = te;
      List list1 = textController.TempIdList;
      String startThree = ProduceSitemap.shellThree(yuMing + yuMingCanShu);
      //int newProductId = true;
      int newProductId = getConfig(yuMingCanShu) + i * 12 + getConfig(yuMingCanShu);
      newProductId %= list1.size();
      ProduceSitemap produceSitemap = new ProduceSitemap();
      String md5GetWeb = ProduceSitemap.string2MD5(getConfig(yuMingCanShu) + "");
      String url = startThree + list1.get(newProductId) + md5GetWeb;
      return url;
   }

   public static String GetOneSameUrl(String yuMing, String cateName, String yuMingCanShu) {
     // int newProductId = true;
      String[] keys = (String[])((String[])mapConfig.keySet().toArray(new String[0]));
      int tempCount = getConfig(cateName);
      int configCount = tempCount % mapConfig.size();
      String webKey = keys[configCount];
      int newProductId = tempCount * configCount + getConfig(cateName) + getConfig(cateName);
      if (newProductId > 300000) {
         newProductId %= 30000;
      }

      String yuMingTemp = replaceWwwOrIndex(yuMing);
      ProduceSitemap var10000 = produceSitemap;
      String md5GetWeb = ProduceSitemap.string2MD5(yuMingTemp + webKey + newProductId);
      String startThree = ProduceSitemap.shellThree(yuMingTemp + yuMingCanShu);
      String url = yuMing + "/?" + startThree + webKey + newProductId + md5GetWeb;
      return url;
   }

   public static String getDiffSiteUrl(String yuMing, String yuMingCanShu) {
      String webKey = "";
      yuMing = yuMing.substring(3);
      yuMing = yuMing.substring(0, yuMing.length() - 5);
      Pattern pattern = Pattern.compile("([a-z]){4}");

      for(Matcher matcher = pattern.matcher(yuMing); matcher.find(); webKey = webKey + matcher.group()) {
      }

      String productId = "";
      Pattern patternId = Pattern.compile("[0-9]\\d{0,6}");

      for(Matcher matcherId = patternId.matcher(yuMing); matcherId.find(); productId = productId + matcherId.group()) {
      }

      ProduceSitemap var10000 = ReadTemp.produceSitemap;
      Map mapConfig = ProduceSitemap.readConfigWeb();
      //int newProductId = true;
      String[] keys = (String[])((String[])mapConfig.keySet().toArray(new String[0]));
      int tempCount = getConfig(yuMing);
      int configCount = tempCount % mapConfig.size();
      webKey = keys[configCount];
      int newProductId = tempCount * getConfig(yuMing) + Integer.parseInt(productId) + getConfig(yuMing);
      if (newProductId > 30000) {
         newProductId %= 30000;
      }

      ProduceSitemap produceSitemap = new ProduceSitemap();
      String md5GetWeb = ProduceSitemap.string2MD5(webKey + newProductId);
      String shellThree = ProduceSitemap.shellThree(yuMing + yuMingCanShu);
      String url = shellThree + webKey + newProductId + md5GetWeb;
      return url;
   }

   public static List produceHtmlAUrl(List list, int count, String yuMing, String YuMingCanShu) {
      List<GreatSeo> greatSeoList = new ArrayList();
      GetYumingTodo getYumingTodo = new GetYumingTodo();
      List listA = new ArrayList();
      String lastsiteCharacter = "";
      byte j = 0;

      String pathTemp;
      String contextJson;
      ReadFile var10000;
      try {
         for(int i = 0; i < list.size(); ++i) {
            lastsiteCharacter = (String)list.get(i);
            lastsiteCharacter = lastsiteCharacter.substring(lastsiteCharacter.indexOf("?") + 1);
            pathTemp = GetYumingTodo.getSiteYuming(lastsiteCharacter, yuMing);
            var10000 = readFile;
            contextJson = ReadFile.readText(new File(pathTemp), yuMing, YuMingCanShu);
            GreatSeo greatSeo = (GreatSeo)JSON.parseObject(contextJson, GreatSeo.class);
            String a;
            if (greatSeo == null) {
               while(greatSeo == null) {
                  contextJson = "";
                  a = getIndex(yuMing, YuMingCanShu, 1);
                  pathTemp = GetYumingTodo.getSiteYuming(a, yuMing);
                  var10000 = readFile;
                  File file=new File(pathTemp);//新增文件判读是否存在
                  if (file.exists()) { //10.14修改内容
                     contextJson = ReadFile.readText(new File(pathTemp), yuMing, YuMingCanShu);
                     greatSeo = (GreatSeo) JSON.parseObject(contextJson, GreatSeo.class);
                     if (greatSeo != null) {
                        break;
                     }
                  }
               }
            }

            greatSeo.setRemarks((String)list.get(i));
            greatSeoList.add(greatSeo);
            a = "";
            if (!greatSeo.getProduct_name().equals((Object)null) || !greatSeo.getProduct_name().equals("")) {
               a = "\"" + list.get(i) + "\" title=\"" + greatSeo.getProduct_name() + "\" >" + greatSeo.getProduct_name()+"\"";
            }

            listA.add(a);
         }
      } catch (Exception var17) {
         try {
            System.out.println("produceHtmlAUrlErr:::::::::::" + list.get(j));
            pathTemp = getIndex(yuMing, YuMingCanShu, j);
            contextJson = GetYumingTodo.getSiteYuming(pathTemp, yuMing);
            var10000 = readFile;
             contextJson = ReadFile.readText(new File(contextJson), yuMing, YuMingCanShu);
            GreatSeo greatSeo1 = (GreatSeo)JSON.parseObject(contextJson, GreatSeo.class);
            greatSeo1.setRemarks((String)list.get(j));
            greatSeoList.add(greatSeo1);
            String a = "";
            if (!greatSeo1.getProduct_name().equals((Object)null) || !greatSeo1.getProduct_name().equals("")) {
               if (((String)list.get(j)).indexOf("/?") < -1) {
                  String tempa = "/?" + list.get(j);
                  a = "href=\"" + tempa + "\" title=\"" + greatSeo1.getProduct_name() + "\" >" + greatSeo1.getProduct_name() + "\"";
               } else {
                  a = "href=\"" + list.get(j) + "\" title=\"" + greatSeo1.getProduct_name() + "\" >" + greatSeo1.getProduct_name() + "\"";
               }
            }

            listA.add(a);
         } catch (Exception var16) {
            listA.add("");
         }
      }

      return listA;
   }

   public static int getConfig(String siteCharacter) {
      siteCharacter = replaceWwwOrIndex(siteCharacter);
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

   public static String spitTempProductUrl(String tempUrlContext, List productList) {
      String returnSumHtmlContent = "";

      try {
         String[] tempUrlArry = tempUrlContext.split("\\{#products_url}");

         for(int i = 0; i < tempUrlArry.length; ++i) {
            try {
               if (productList.size() <= i && Objects.isNull(productList.get(i))) {
                  returnSumHtmlContent = returnSumHtmlContent + tempUrlArry[i] + "";
               } else if (i == tempUrlArry.length - 1) {
                  returnSumHtmlContent = returnSumHtmlContent + productList.get(i) + tempUrlArry[i];
               } else {
                  returnSumHtmlContent = returnSumHtmlContent + tempUrlArry[i] + productList.get(i);
               }
            } catch (Exception var6) {
               returnSumHtmlContent = returnSumHtmlContent + tempUrlArry[i] + "";
               System.out.println("spitTempProductUrl:");
            }
         }

         return returnSumHtmlContent;
      } catch (Exception var7) {
         System.out.println("spitTempProductUrl111111:");
         return tempUrlContext;
      }
   }

   public static String ReadTempReNameAll() {
      File file = new File("temp");
      File[] array = file.listFiles();
      int count = 0;

      for(int i = 0; i < array.length; ++i) {
         count = i;
         if (array[i].isFile() && array[i].getName().indexOf(".html") > 0) {
            array[i].renameTo(new File("temp/" + i + ".html"));
         }
      }

      try {
         FileUtils.write(new File("temp/tempcount.txt"), String.valueOf(count));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return "";
   }

   public static String dealWithYouTobe(String youtobeString, String url) {
      String returnString = "";

      try {
         if (youtobeString != null && youtobeString != "") {
            returnString = "<iframe src=\"";
            String[] youtoArr = youtobeString.split("Φfenge");
            int ran = getConfig(url);
            int randomKey = ran % youtoArr.length;
            returnString = returnString + youtoArr[randomKey] + "\" width=\"853\" height=\"480\"></iframe>";
         }
      } catch (Exception var6) {
         returnString = "";
         var6.printStackTrace();
      }

      return returnString;
   }

   public static String replaceWwwOrIndex(String str) {
      str = str.replaceAll("www.", "");
      str = str.replaceAll("index.php", "");
      return str;
   }

   static {
      ReadFile var10000 = readFile;
      tempMap = ReadFile.getTempValue();
      hostList = HandleGoogle.host();
      te = new textController();
      ProduceSitemap var0 = produceSitemap;
      mapConfig = ProduceSitemap.readConfigWeb();
      productLdJson = new ProductLdJson();
   }
}
