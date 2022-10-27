package com.gaobug.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisUtils {
   @Resource
   private RedisTemplate<Serializable, Object> redisTemplate;

   public static void main(String[] args) {
   }

   public String down(String href) throws InterruptedException {
      long begin_time = (new Date()).getTime();
      URL url = null;

      try {
         url = new URL(href);
      } catch (MalformedURLException var26) {
         var26.printStackTrace();
      }

      URLConnection conn = null;

      try {
         conn = url.openConnection();
      } catch (IOException var25) {
         var25.printStackTrace();
      }

       String fileName = url.getFile();
      fileName = fileName.substring(fileName.lastIndexOf("/"));
      System.out.println("开始下载>>>");
       int fileSize = conn.getContentLength();
      System.out.println("文件总共大小：" + fileSize + "字节");
       int blockSize = 1048576;
       int blockNum = fileSize / blockSize;
      if (fileSize % blockSize != 0) {
         ++blockNum;
      }

      System.out.println("分块数->线程数：" + blockNum);
      Thread[] threads = new Thread[blockNum];

      for( int i = 0; i < blockNum; ++i) {
         int finalI = i;
         int finalBlockNum = blockNum;
         String finalFileName = fileName;
         threads[i] = new Thread() {
            public void run() {
               URL url = null;

               try {
                  url = new URL(href);
               } catch (MalformedURLException var11) {
                  var11.printStackTrace();
               }

               try {
                  URLConnection conn = url.openConnection();
                  InputStream in = conn.getInputStream();
                  Boolean beginPointx = false;
                  Boolean endPointx = false;
                  System.out.print("第" + (finalI + 1) + "块文件：");
                  int beginPoint = finalI * blockSize;
                  int endPoint;
                  if (finalI < finalBlockNum - 1) {
                     endPoint = beginPoint + blockSize;
                  } else {
                     endPoint = fileSize;
                  }

                  System.out.println("起始字节数：" + beginPoint + ",结束字节数：" + endPoint);
                  File filePath = new File("temp");
                  if (!filePath.exists()) {
                     filePath.mkdirs();
                  }

                  FileOutputStream fos = new FileOutputStream(new File("temp", finalFileName + "_" + (finalI + 1)));
                  in.skip((long)beginPoint);
                  byte[] buffer = new byte[1024];

                  int count;
                  for(int process = beginPoint; process < endPoint; fos.write(buffer, 0, count)) {
                     count = in.read(buffer);
                     if (process + count >= endPoint) {
                        count = endPoint - process;
                        process = endPoint;
                     } else {
                        process += count;
                     }
                  }

                  fos.close();
                  in.close();
               } catch (Exception var12) {
                  var12.printStackTrace();
               }

            }
         };
         threads[i].start();
      }

      Thread[] var28 = threads;
      int var12 = threads.length;

      int i;
      for(i = 0; i < var12; ++i) {
         Thread t = var28[i];
         t.join();
      }

      File filePath = new File("temp/temp-rainy/");
      if (!filePath.exists()) {
         filePath.mkdirs();
      }

      FileOutputStream fos = null;

      try {
         fos = new FileOutputStream("temp/temp-rainy/" + fileName);
      } catch (FileNotFoundException var24) {
         var24.printStackTrace();
      }

      for(i = 0; i < blockNum; ++i) {
         FileInputStream fis = null;

         try {
            fis = new FileInputStream("D:\\work\\project\\filedown\\temp_file\\" + fileName + "_" + (i + 1));
         } catch (FileNotFoundException var23) {
            var23.printStackTrace();
         }

         byte[] buffer = new byte[1024];

         int count;
         try {
            while((count = fis.read(buffer)) > 0) {
               fos.write(buffer, 0, count);
            }
         } catch (IOException var27) {
            var27.printStackTrace();
         }

         try {
            fis.close();
         } catch (IOException var22) {
            var22.printStackTrace();
         }
      }

      try {
         fos.close();
      } catch (IOException var21) {
         var21.printStackTrace();
      }

      long end_time = (new Date()).getTime();
      long seconds = (end_time - begin_time) / 1000L;
      long minutes = seconds / 60L;
      long second = seconds % 60L;
      System.out.println("下载完成,用时：" + minutes + "分" + second + "秒");
      return "temp-rainy/" + fileName;
   }

   public boolean set( String key, Object value) {
      boolean result = false;

      try {
         ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
         operations.set(key, value);
         result = true;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return result;
   }

   public boolean set( String key, Object value, Long expireTime, TimeUnit timeUnit) {
      boolean result = false;

      try {
         ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
         operations.set(key, value);
         this.redisTemplate.expire(key, expireTime, timeUnit);
         result = true;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return result;
   }

   public void remove( String... keys) {
      String[] var2 = keys;
      int var3 = keys.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String key = var2[var4];
         this.remove(key);
      }

   }

   public void removePattern( String pattern) {
      Set<Serializable> keys = this.redisTemplate.keys(pattern);
      if (keys.size() > 0) {
         this.redisTemplate.delete(keys);
      }

   }

   public void remove( String key) {
      if (this.exists(key)) {
         this.redisTemplate.delete(key);
      }

   }

   public boolean exists( String key) {
      return this.redisTemplate.hasKey(key);
   }

   public Object get( String key) {
      Object result = null;
      ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
      result = operations.get(key);
      return result;
   }

   public Object hGet(String key, String item) {
      return this.redisTemplate.opsForHash().get(key, item);
   }

   public Map<Object, Object> hmGet(String key) {
      return this.redisTemplate.opsForHash().entries(key);
   }

   public boolean hmSet(String key, Map<String, Object> map) {
      try {
         this.redisTemplate.opsForHash().putAll(key, map);
         return true;
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public boolean hmSet(String key, Map<String, Object> map, long time) {
      try {
         this.redisTemplate.opsForHash().putAll(key, map);
         if (time > 0L) {
            this.expire(key, time);
         }

         return true;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   public boolean hSet(String key, String item, Object value) {
      try {
         this.redisTemplate.opsForHash().put(key, item, value);
         return true;
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }

   public boolean hSet(String key, String item, Object value, long time) {
      try {
         this.redisTemplate.opsForHash().put(key, item, value);
         if (time > 0L) {
            this.expire(key, time);
         }

         return true;
      } catch (Exception var7) {
         var7.printStackTrace();
         return false;
      }
   }

   public void hDel(String key, Object... item) {
      this.redisTemplate.opsForHash().delete(key, item);
   }

   public boolean hHasKey(String key, String item) {
      return this.redisTemplate.opsForHash().hasKey(key, item);
   }

   public double hincr(String key, String item, double by) {
      return this.redisTemplate.opsForHash().increment(key, item, by);
   }

   public double hdecr(String key, String item, double by) {
      return this.redisTemplate.opsForHash().increment(key, item, -by);
   }

   public void hmSet(String key, Object hashKey, Object value) {
      HashOperations<Serializable, Object, Object> hash = this.redisTemplate.opsForHash();
      hash.put(key, hashKey, value);
   }

   public Object hmGet(String key, Object hashKey) {
      HashOperations<Serializable, Object, Object> hash = this.redisTemplate.opsForHash();
      return hash.get(key, hashKey);
   }

   public Set<Object> sGet(String key) {
      return this.redisTemplate.opsForSet().members(key);
   }

   public boolean sHashKey(String key, Object value) {
      return this.redisTemplate.opsForSet().isMember(key, value);
   }

   public long sSet(String key, Object... values) {
      try {
         return this.redisTemplate.opsForSet().add(key, values);
      } catch (Exception var4) {
         var4.printStackTrace();
         return 0L;
      }
   }

   public long sSet(String key, Long time, Object... values) {
      try {
         Long count = this.redisTemplate.opsForSet().add(key, values);
         if (time > 0L) {
            this.expire(key, time);
         }

         return count;
      } catch (Exception var5) {
         var5.printStackTrace();
         return 0L;
      }
   }

   public long sSize(String key) {
      try {
         return this.redisTemplate.opsForSet().size(key);
      } catch (Exception var3) {
         var3.printStackTrace();
         return 0L;
      }
   }

   public long sRemove(String key, Object... values) {
      try {
         return this.redisTemplate.opsForSet().remove(key, values);
      } catch (Exception var4) {
         var4.printStackTrace();
         return 0L;
      }
   }

   public List<Object> lGet(String key, long start, long end) {
      try {
         return this.redisTemplate.opsForList().range(key, start, end);
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public long lSize(String key) {
      try {
         return this.redisTemplate.opsForList().size(key);
      } catch (Exception var3) {
         var3.printStackTrace();
         return 0L;
      }
   }

   public Object lIndex(String key, long index) {
      try {
         return this.redisTemplate.opsForList().index(key, index);
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public boolean lSet(String key, Object value) {
      try {
         this.redisTemplate.opsForList().leftPush(key, value);
         return true;
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public boolean lSet(String key, long time, Object value) {
      try {
         this.redisTemplate.opsForList().leftPush(key, value);
         if (time > 0L) {
            this.expire(key, time);
         }

         return true;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   public boolean lUpdate(String key, Long index, Object value) {
      try {
         this.redisTemplate.opsForList().set(key, index, value);
         return true;
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }

   public long lRomve(String key, Long count, Object value) {
      try {
         long remove = this.redisTemplate.opsForList().remove(key, count, value);
         return remove;
      } catch (Exception var6) {
         var6.printStackTrace();
         return 0L;
      }
   }

   public boolean expire(String key, long time) {
      try {
         if (time > 0L) {
            this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
         }

         return true;
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }
}
