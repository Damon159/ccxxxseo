package com.gaobug.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gaobug.Textmysql;
import com.gaobug.seo.GreatSeo;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HandleGoogle {
   public static void main(String[] args) {
      HandleGoogleCsv();
   }
   public static void putGoogleYb() {
      Connection conn = null;
      File file = new File("title/youtube");

      try {
         Class.forName("org.sqlite.JDBC");
         if (!file.exists()) {
            file.mkdir();
         }

         File[] filenames = file.listFiles();
         File[] var4 = filenames;
         int var5 = filenames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object filepath = var4[var6];
             filepath = filepath.toString().replace("\\", "/");
            System.out.println(filepath);
            String dburl = "jdbc:sqlite:" + filepath;
            conn = DriverManager.getConnection(dburl);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select domainid,keywords,details from content where domainid is not null;");

            while(resultSet.next()) {
               if (!resultSet.getString("domainid").equals("") && resultSet.getString("domainid") != null) {
                  String domainid = resultSet.getString("domainid");
                  String keywords = resultSet.getString("keywords");
                  String details = resultSet.getString("details");
                  String[] titleIdArr = domainid.split("-");
                  String sitePath = "../site/" + titleIdArr[0] + "/p" + titleIdArr[1] + ".txt";
                  String siteJson = "";

                  try {
                     siteJson = FileUtils.readFileToString(new File(sitePath.trim()));
                  } catch (IOException var20) {
                     var20.printStackTrace();
                  }

                  GreatSeo greatSeo = (GreatSeo)JSONObject.parseObject(siteJson, GreatSeo.class);
                  keywords = keywords.replaceAll("Φfenge", " ");
                  Random random = new Random();
                  int randomKey = random.nextInt(5);
                  if (randomKey == 1) {
                     keywords = "<p>" + keywords + "</p>";
                  } else if (randomKey == 2) {
                     keywords = "<span style='text-align:center'>" + keywords + "</span>";
                  } else if (randomKey == 3) {
                     keywords = "<span><p>" + keywords + "</p></span>";
                  } else if (randomKey == 4) {
                     keywords = "<div><p>" + keywords + "</p></div>";
                  } else {
                     keywords = "<div><p style='text-align:center'>" + keywords + "</p></div>";
                  }

                  greatSeo.setUb(keywords);
                  System.out.println("写入文件youtub" + domainid);
                  String stringJSON = JSON.toJSONString(greatSeo);
                  ReadFile.writeFile(new File(sitePath), stringJSON);
               }
            }
         }
      } catch (Exception var21) {
         System.out.println("youtobebaocuo");
         var21.printStackTrace();
      }

   }

   public static void putGoogleKey() {
      Connection conn = null;
      File file = new File("title/yahookey");
      try {
         Class.forName("org.sqlite.JDBC");
         if (!file.exists()) {
            file.mkdir();
         }

         File[] filenames = file.listFiles();
         File[] var4 = filenames;
         int var5 = filenames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object filepath = var4[var6];
             filepath = filepath.toString().replace("\\", "/");
            String dburl = "jdbc:sqlite:" + filepath;
            conn = DriverManager.getConnection(dburl);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select domainid,keywords,details from content where keywords is not null;");

            while(resultSet.next()) {
               try {
                  if (!resultSet.getString("domainid").equals("") && resultSet.getString("domainid") != null) {
                     String domainid = resultSet.getString("domainid");
                     String keywords = resultSet.getString("keywords");
                     String details = resultSet.getString("details");

//                     String[] nameId = domainid.split("#gl=");
//                     nameId[1] = nameId[1].replaceAll("kkkkkk", "");
//                     String[] titleIdArr = nameId[1].split("-");
//                     String sitePath = "../site/" + titleIdArr[0] + "/p" + titleIdArr[1] + ".txt";
                     String[] titleIdArr = domainid.split("-");
                     String sitePath = "../site/" + titleIdArr[0] + "/p" + titleIdArr[1] + ".txt";
                     String siteJson = "";

                     try {
                        siteJson = FileUtils.readFileToString(new File(sitePath.trim()));
                     } catch (IOException var23) {
                        var23.printStackTrace();
                     }

                     GreatSeo greatSeo = (GreatSeo)JSONObject.parseObject(siteJson, GreatSeo.class);
                     keywords = keywords.replace("...", "");
                     details = details.replace("...", "");
                     String[] keywordsArr = keywords.split("Φfenge");
                     String[] detailsArr = details.split("Φfenge");
                     List<String> keywordsList = Arrays.asList(keywordsArr);
                     List<String> detailsList = Arrays.asList(detailsArr);
                     greatSeo.setGoogle_desH(keywordsList);
                     greatSeo.setGoogle_desP(detailsList);
                     String stringJSON = JSON.toJSONString(greatSeo);
                     System.out.println("putGoogleKey:" + titleIdArr[0]+"-"+titleIdArr[1]);
                     ReadFile.writeFile(new File(sitePath), stringJSON);
                  }
               } catch (Exception var24) {
                  var24.printStackTrace();
               }
            }

            System.out.println("writeyahookey:" + file.getName());
         }
      } catch (Exception var25) {
         var25.printStackTrace();
      }
      System.out.println("yahookeyDone!!!");
   }

   public static void HandleGoogleCsv() {
      try {
         File file = new File("title/googleImgCsv");
         String[] fileName = file.list();

         for(int i = 0; i < fileName.length; i++) {
            try {
               String csvFile = "title/googleImgCsv/" + fileName[i];
               System.out.println("filename--"+csvFile);
               String line = "";
               String cvsSplitBy = ",";
               //int count = false;
               String[] Line = new String[1];
               ArrayList bikeDataList = new ArrayList();

               try {
                  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "utf-8"));
                  Throwable var10 = null;
                  try {
                     while((line = br.readLine()) != null) {
                        line = line.trim();
                        Line = line.split(cvsSplitBy);
                        bikeDataList.add(Line);
                     }
                     writeDownTOSite(bikeDataList);
                  } catch (Throwable var22) {
                     var10 = var22;
                     throw var22;
                  } finally {
                     if (br != null) {
                        if (var10 != null) {
                           try {
                              br.close();
                           } catch (Throwable var21) {
                              var10.addSuppressed(var21);
                           }
                        } else {
                           br.close();
                        }
                     }

                  }
               } catch (IOException var24) {
                  var24.printStackTrace();
               }
            } catch (Exception var25) {
               var25.printStackTrace();
            }
         }
      } catch (Exception var26) {
         var26.printStackTrace();
      }
      System.out.println("googleImagedone!!!");
   }

   public static void writeDownTOSite(List<String[]> list) {
      try {
         String sumTitle = "";
         for(int i = 0; i < list.size(); i++) {
            try {
               System.out.println("ii-->"+i);
               String titleId = ((String[])list.get(i))[0];
               //System.out.println(titleId);
               titleId = titleId.replaceAll("\\s+", "");
               String[] titleIdArr = titleId.split("-");
               titleIdArr[0] = titleIdArr[0].replaceAll("\ufeff\"", "");
               String a = "../site/" + titleIdArr[0].replace("\"", "");
               String a1 = titleIdArr[1].replace("\"", "");
               String sitePath = a + "/p" + a1 + ".txt";
               String siteJson = null;

               try {
                  siteJson = FileUtils.readFileToString(new File(sitePath.trim()));
               } catch (IOException var20) {
                  var20.printStackTrace();
               }

               GreatSeo greatSeo = (GreatSeo)JSONObject.parseObject(siteJson, GreatSeo.class);
               if (greatSeo != null) {
                  String titleTitle = ((String[])list.get(i))[1];
                  String titleGoogle = ((String[])list.get(i))[1];
                  String newTitleGoogle = "<ul>";
                  String[] titleGoogleArr = titleGoogle.split("<br /");
                  List<String> googleList = new ArrayList();

                  int sumTitleP;

                  for(sumTitleP = 0; sumTitleP < titleGoogleArr.length; ++sumTitleP) {
                     titleGoogleArr[sumTitleP] = titleGoogleArr[sumTitleP].replaceAll("\"\"", "\"");
                     if (titleGoogleArr[sumTitleP].indexOf(" />") < 0) {
                        titleGoogleArr[sumTitleP] = titleGoogleArr[sumTitleP] + "/>";
                     }

                     int indexTitleGoogle = titleGoogleArr[sumTitleP].indexOf("title");
                     if (indexTitleGoogle != -1) {
                        String titleStart = titleGoogleArr[sumTitleP].substring(indexTitleGoogle + 5);
                        String titleEnd = titleStart.substring(titleStart.indexOf("=\"") + 2, titleStart.indexOf("\" alt") == -1 ? titleStart.length() : titleStart.indexOf("\" alt"));
                        if (greatSeo.getProduct_cate3() != null && greatSeo.getProduct_cate3() != "" && greatSeo.getProduct_cate2() != null && greatSeo.getProduct_cate2() != "") {
                           googleList.add(titleGoogleArr[sumTitleP].substring(1) + "<li><p>" + greatSeo.getProduct_cate3() + greatSeo.getProduct_cate2() + titleEnd + "</p><li>");
                           int num = (int)(Math.random() * 10.0D);
                           if (num < 5) {
                              sumTitle = sumTitle.replace("...", "");
                              sumTitle = sumTitle + titleEnd + greatSeo.getProduct_cate3();
                           }
                        } else {
                           googleList.add("<li>" + titleGoogleArr[sumTitleP].substring(1) + "<p>" + titleEnd + "</p></li>");
                        }
                     }
                  }

                  Collections.sort(googleList);



                  String stringJSON;
                  for(Iterator var23 = googleList.iterator(); var23.hasNext(); newTitleGoogle = newTitleGoogle + stringJSON) {
                     stringJSON = (String)var23.next();
                  }

                  sumTitleP = (int)(Math.random() * 10.0D);
                  if (sumTitleP == 1 || sumTitleP == 5) {
                     sumTitle = "<p>" + sumTitle + "</p>";
                  }

                  else if (sumTitleP == 2 || sumTitleP == 3) {
                     sumTitle = "<span>" + sumTitle + "</span>";
                  }

                  else if (sumTitleP == 4 || sumTitleP == 6) {
                     sumTitle = "<div>" + sumTitle + "</div>";
                  }

                  else if (sumTitleP == 8) {
                     sumTitle = "<strong>" + sumTitle + "</strong>";
                  }

                  newTitleGoogle = newTitleGoogle + "</ul>";
                  newTitleGoogle = newTitleGoogle.replace("...", "");
                  greatSeo.setGoogle_img(newTitleGoogle);
                     greatSeo.setGoogle_img_sum("");
                  //System.out.println("path:" + sitePath);
                  stringJSON = JSON.toJSONString(greatSeo);
                  //eadFile.writeFile(new File(sitePath), stringJSON);
               }
            } catch (Exception var21) {
               var21.printStackTrace();
            }
         }
      } catch (Exception var22) {
         System.out.println("writeDownTOSite 执行出错");
         var22.printStackTrace();
      }

   }

   public static List host() {
      ArrayList list = new ArrayList();

      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("host.txt")), "UTF-8"));
         String s = null;

         while((s = br.readLine()) != null) {
            list.add(s);
         }

         br.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return list;
   }

   public static int[] getSequence(int array) {
      int[] sequence = new int[array];

      for(int i = 0; i < array; sequence[i] = i++) {
      }

      Random random = new Random();

      for(int i = 0; i < array; ++i) {
         int p = random.nextInt(array);
         int tmp = sequence[i];
         sequence[i] = sequence[p];
         sequence[p] = tmp;
      }

      random = null;
      return sequence;
   }

   public static void putHunterDataGoogleImage1(String pathfile,String mapkey){
      File file =new File(pathfile);
      if (!file.exists()){
         file.mkdir();
      }
      File [] filenames=file.listFiles();
      if (filenames.length>0) {
         for (Object filepath : filenames
         ) {
            System.out.println(filepath);
            ArrayList arrayList = filedata1(filepath.toString());
            for (Object line : arrayList
            ) {
               try{
                  String[] contents = line.toString().split("\",\"");
                  if (contents.length < 2) {
                     continue;
                  }
                  if (contents[0].length() < 1) {
                     System.out.println("contents");
                     continue;
                  }
                  String content = contents[0].replace("\"", "");
                  content=content.replaceFirst("-","Φfenge");
                  content=content.replaceFirst("-","Φfenge");
                  String domainidname []= content.split("Φfenge");
                  //System.out.println(content);
                  if (domainidname.length < 2) {
                     System.out.println("domainId-->"+content);
                     continue;
                  }
                  String path = "../site/" + domainidname[0] + "/p" + domainidname[1] + ".txt";
                  File file2 = new File(path);
                  if (!file2.exists()) {
                     System.out.println("files");
                     continue;
                  }
                  path = path.replace("\uFEFF", "");
                  ArrayList arraycontent = getFileContent1(path, false);
                  if (arraycontent == null)
                     continue;
                  Map dataMap = (Map) JSON.parse((String) arraycontent.get(0));
                  if (dataMap == null)
                     continue;
                  //System.out.println("contt------>>>>>" + contents[0]);
                  contents[1] = contents[1].replace("/>\"", "/>");
                  contents[1] = contents[1].replace("\"\"", "\"");
                  String[] imms = contents[1].split("<br />");
                  Set sett = new HashSet();
                  for (String tp : imms
                  ) {
                     sett.add(tp);
                  }
                  imms = (String[]) sett.toArray(new String[0]);
                  String title = "";
                  Set set = new HashSet();
                  Element temp;
                  String all = "";
                  String titles = "";
                  String str1="";
                  for (Object key : imms
                  ) {

                     Document doc = Jsoup.parse(key.toString());
                     Elements elements = doc.select("img");
                     Elements element1 = doc.getElementsByAttributeStarting("title");
                     if (element1.first() == null)
                        continue;
                     title = elements.first().attr("title");
                     if (str1=="")
                        str1=title;
                     title=title.replace("...","");
                     all+=key.toString()+"<li><p>"+title+"</p><li>";
//                     set.add(title);
//                     Iterator value = set.iterator();
//                     while (value.hasNext()) {
//                        titles += value.next();
//                     }
//                     set.clear();
//
//                     all += elements.first().toString();
                  }
                  String cat3="";
                  if (dataMap.get("product_cate3")!=null||dataMap.get("product_cate3")!="")
                     cat3= (String) dataMap.get("product_cate3");

                  String  sumTitle=str1+dataMap.get(" ")+cat3;

                  int sumTitleP = (int)(Math.random() * 10.0D);
                  if (sumTitleP == 1 || sumTitleP == 5) {
                     sumTitle = "<p>" + sumTitle + "</p>";
                  }

                  else if (sumTitleP == 2 || sumTitleP == 3) {
                     sumTitle = "<span>" + sumTitle + "</span>";
                  }

                  else if (sumTitleP == 4 || sumTitleP == 6) {
                     sumTitle = "<div>" + sumTitle + "</div>";
                  }

                  else if (sumTitleP == 8) {
                     sumTitle = "<strong>" + sumTitle + "</strong>";
                  }


                  all = "<ul>" + all + "</ul>";
                  dataMap.put("google_img", all);
                  dataMap.put("google_img_sum", sumTitle);
                  String stringJSON = JSON.toJSONString(dataMap);
                  FileUtils.write(new File(path), stringJSON, "UTF-8");
               }catch (Exception e){
                  e.printStackTrace();
                  continue;
               }
            }

         }
         System.out.println("GoogleimageDone!!!");
      }
   }
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
    * 读取文件返回内容按行读取
    */
   public static ArrayList filedata1(String path){
      BufferedReader br = null;
      ArrayList arrayList=new ArrayList();
      try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream((path)), "UTF-8"));//UTF-8
         String str = null;
         int i = 0;
         while ((str = br.readLine()) != null) {
            String[] v1 = str.trim().split("\\s+"); //剔除调前、后、中间所有的空格
            arrayList.add(str);
            i++;
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            br.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return arrayList;
   }
}
