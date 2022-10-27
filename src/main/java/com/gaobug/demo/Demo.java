package com.gaobug.demo;

import com.gaobug.utils.RedisUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class Demo {
   @Resource
   private RedisUtils redisUtils;

   public void demo() {
      this.redisUtils.set("key", "value");
   }
}
