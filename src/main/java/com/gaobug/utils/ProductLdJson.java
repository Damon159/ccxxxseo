package com.gaobug.utils;

import com.alibaba.fastjson.JSONObject;
import com.gaobug.seo.GreatSeo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class ProductLdJson {
   public static String productJson(GreatSeo greatSeo, String hostSite) {
      Random r = new Random();
      Map<String, Object> productMap = new TreeMap();
      Map reviewMap = new TreeMap();
      Map reviewRatingMap = new TreeMap();
      productMap.put("@context", "http://schema.org");
      productMap.put("@type", "Product");
      if (greatSeo.getProduct_main_img() != null && greatSeo.getProduct_main_img() != "") {
         List images = new ArrayList();
         images.add(greatSeo.getProduct_main_img());
         productMap.put("image", images);
      }

      String description = greatSeo.getProduct_description().replaceAll("\\<.*?>", "");
      productMap.put("name", greatSeo.getProduct_cate3());
      productMap.put("description", description);
      if (greatSeo.getProduct_name() != null && greatSeo.getProduct_name() != "") {
         Map bandMap = new TreeMap();
         bandMap.put("@type", "Brand");
         String band = "";
         String[] productName = greatSeo.getProduct_name().split("　");
         if (productName.length < 1) {
            band = greatSeo.getProduct_cate1() + greatSeo.getProduct_cate2() + greatSeo.getProduct_cate3();
         } else if (productName.length > 1 && productName.length < 4) {
            band = greatSeo.getProduct_cate3() + greatSeo.getProduct_cate1() + greatSeo.getProduct_cate2();
         } else {
            band = greatSeo.getProduct_cate3() + productName[0];
         }

         band = band.replaceAll("null", "");
         band = band.replaceAll(" ", "");
         bandMap.put("@name", band);
         productMap.put("brand", bandMap);
      }

      reviewMap.put("@type", "Review");
      reviewRatingMap.put("@type", "Rating");
      double bestRating = (double)r.nextFloat() + 4.7D;
      if (bestRating > 5.0D) {
         bestRating = 5.0D;
      }

      double ratingValue = (double)r.nextFloat();
      ratingValue = bestRating - ratingValue;
      DecimalFormat decimalFormat = new DecimalFormat(".0");
      String Best = decimalFormat.format(bestRating);
      String rating = decimalFormat.format(ratingValue);
      reviewRatingMap.put("ratingValue", rating);
      reviewRatingMap.put("bestRating", Best);
      productMap.put("review", reviewRatingMap);
      Map aggregateRatingMap = new TreeMap();
      aggregateRatingMap.put("@type", "AggregateRating");
      float average = (float)bestRating + (float)ratingValue;
      int number = r.nextInt(50);
      aggregateRatingMap.put("ratingValue", decimalFormat.format((double)(average / 2.0F)));
      aggregateRatingMap.put("reviewCount", number);
      productMap.put("aggregateRating", aggregateRatingMap);
      Map offersMap = new TreeMap();
      offersMap.put("@type", "Offer");
      offersMap.put("url", hostSite);
      offersMap.put("availability", "http://schema.org/InStock");
      DecimalFormat decimalFormatP = new DecimalFormat(".00");
      offersMap.put("price", decimalFormatP.format(Float.valueOf(greatSeo.getProduct_price())));
      offersMap.put("priceCurrency", "JPY");
      productMap.put("offers", offersMap);
      String jsonToReturn = JSONObject.toJSONString(productMap);
      return jsonToReturn;
   }

   public static String breadCrumbsLdJson(String yuMingCanShu, String cate1, String cate2, String cate3, String productName, String yuMing) {
      String breadCrumbs = "";
      Map<String, Object> breadCrumbsMap = new TreeMap();
      breadCrumbsMap.put("@context", "https://schema.org");
      breadCrumbsMap.put("@type", "BreadcrumbList");
      ArrayList breadCrumbsList = new ArrayList();
      new ProduceSitemap();
      String productUrlOne = ReadTemp.GetOneSameUrl(yuMing, cate1, yuMingCanShu);
      String productUrlTwo = ReadTemp.GetOneSameUrl(yuMing, cate2, yuMingCanShu);
      String productUrlThree = ReadTemp.GetOneSameUrl(yuMing, cate3, yuMingCanShu);
      TreeMap productNameMap;
      if (yuMing != null && yuMing != "") {
         productNameMap = new TreeMap();
         productNameMap.put("@type", "ListItem");
         productNameMap.put("position", "1");
         productNameMap.put("name", "ホーム");
         productNameMap.put("item", yuMing);
         breadCrumbsList.add(productNameMap);
      }

      if (cate1 != null && cate1 != "") {
         productNameMap = new TreeMap();
         productNameMap.put("@type", "ListItem");
         productNameMap.put("position", "2");
         productNameMap.put("name", cate1);
         productNameMap.put("item", productUrlOne);
         breadCrumbsList.add(productNameMap);
      }

      if (cate2 != null && cate2 != "") {
         productNameMap = new TreeMap();
         productNameMap.put("@type", "ListItem");
         productNameMap.put("position", "3");
         productNameMap.put("name", cate2);
         productNameMap.put("item", productUrlTwo);
         breadCrumbsList.add(productNameMap);
      }

      if (cate3 != null && cate3 != "") {
         productNameMap = new TreeMap();
         productNameMap.put("@type", "ListItem");
         productNameMap.put("position", "4");
         productNameMap.put("name", cate3);
         productNameMap.put("item", productUrlThree);
         breadCrumbsList.add(productNameMap);
      }

      if (productName != null && productName != "") {
         productNameMap = new TreeMap();
         yuMingCanShu = yuMingCanShu.replaceAll("/", "");
         yuMingCanShu = yuMing + "/?" + yuMingCanShu;
         productNameMap.put("@type", "ListItem");
         productNameMap.put("position", "5");
         productNameMap.put("name", productName);
         productNameMap.put("item", yuMingCanShu);
         breadCrumbsList.add(productNameMap);
      }

      breadCrumbsMap.put("itemListElement", breadCrumbsList);
      String jsonToReturn = JSONObject.toJSONString(breadCrumbsMap);
      return jsonToReturn;
   }
}
