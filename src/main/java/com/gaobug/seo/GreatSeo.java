package com.gaobug.seo;

import java.io.Serializable;
import java.util.List;

public class GreatSeo implements Serializable {
   private String product_id;
   private String product_name;
   private String product_description;
   private String product_name_two;
   private String product_url;
   private String product_related;
   private String product_price;
   private String product_main_img;
   private String product_cate1;
   private String product_cate2;
   private String product_cate3;
   private String product_option_name;
   private String product_model;
   private String google_img;
   private String google_img_sum;
   private List google_desH;
   private List google_desP;
   private String google_video;
   private String keytitles;

   public String getKeytitles() {
      return keytitles;
   }

   public void setKeytitles(String keytitles) {
      this.keytitles = keytitles;
   }

   public String getDestitle() {
      return destitle;
   }

   public void setDestitle(String destitle) {
      this.destitle = destitle;
   }

   private String destitle;
   private String remarks;
   private String ub;
   private String ubView;

   public String getProduct_id() {
      return this.product_id;
   }

   public void setProduct_id(String product_id) {
      this.product_id = product_id;
   }

   public String getProduct_name() {
      return this.product_name;
   }

   public void setProduct_name(String product_name) {
      this.product_name = product_name;
   }

   public String getProduct_description() {
      return this.product_description;
   }

   public void setProduct_description(String product_description) {
      this.product_description = product_description;
   }

   public String getProduct_name_two() {
      return this.product_name_two;
   }

   public void setProduct_name_two(String product_name_two) {
      this.product_name_two = product_name_two;
   }

   public String getProduct_url() {
      return this.product_url;
   }

   public void setProduct_url(String product_url) {
      this.product_url = product_url;
   }

   public String getProduct_related() {
      return this.product_related;
   }

   public void setProduct_related(String product_related) {
      this.product_related = product_related;
   }

   public String getProduct_price() {
      return this.product_price;
   }

   public void setProduct_price(String product_price) {
      this.product_price = product_price;
   }

   public String getProduct_main_img() {
      return this.product_main_img;
   }

   public void setProduct_main_img(String product_main_img) {
      this.product_main_img = product_main_img;
   }

   public String getProduct_cate1() {
      return this.product_cate1;
   }

   public void setProduct_cate1(String product_cate1) {
      this.product_cate1 = product_cate1;
   }

   public String getProduct_cate2() {
      return this.product_cate2;
   }

   public void setProduct_cate2(String product_cate2) {
      this.product_cate2 = product_cate2;
   }

   public String getProduct_cate3() {
      return this.product_cate3;
   }

   public void setProduct_cate3(String product_cate3) {
      this.product_cate3 = product_cate3;
   }

   public String getProduct_option_name() {
      return this.product_option_name;
   }

   public void setProduct_option_name(String product_option_name) {
      this.product_option_name = product_option_name;
   }

   public String getRemarks() {
      return this.remarks;
   }

   public void setRemarks(String remarks) {
      this.remarks = remarks;
   }

   public String getProduct_model() {
      return this.product_model;
   }

   public void setProduct_model(String product_model) {
      this.product_model = product_model;
   }

   public String getGoogle_img() {
      return this.google_img;
   }

   public void setGoogle_img(String google_img) {
      this.google_img = google_img;
   }

   public String getGoogle_video() {
      return this.google_video;
   }

   public void setGoogle_video(String google_video) {
      this.google_video = google_video;
   }

   public List getGoogle_desH() {
      return this.google_desH;
   }
   private String desimage;

   public String getDesimage() {
      return desimage;
   }

   public void setDesimage(String desimage) {
      this.desimage = desimage;
   }

   public String getYoutube_keywords() {
      return youtube_keywords;
   }

   public void setYoutube_keywords(String youtube_keywords) {
      this.youtube_keywords = youtube_keywords;
   }

   public String getGoogle_image_keyword() {
      return google_image_keyword;
   }

   public void setGoogle_image_keyword(String google_image_keyword) {
      this.google_image_keyword = google_image_keyword;
   }

   public String getProduct_review() {
      return product_review;
   }

   public void setProduct_review(String product_review) {
      this.product_review = product_review;
   }

   private String youtube_keywords;
   private String google_image_keyword;
   private String product_review;


   public void setGoogle_desH(List google_desH) {
      this.google_desH = google_desH;
   }

   public List getGoogle_desP() {
      return this.google_desP;
   }

   public void setGoogle_desP(List google_desP) {
      this.google_desP = google_desP;
   }

   public String getGoogle_img_sum() {
      return this.google_img_sum;
   }

   public void setGoogle_img_sum(String google_img_sum) {
      this.google_img_sum = google_img_sum;
   }

   public String getUb() {
      return this.ub;
   }

   public void setUb(String ub) {
      this.ub = ub;
   }

   public String getUbView() {
      return this.ubView;
   }

   public void setUbView(String ubView) {
      this.ubView = ubView;
   }
}
