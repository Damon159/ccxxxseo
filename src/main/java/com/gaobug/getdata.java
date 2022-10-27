package com.gaobug;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.gaobug.utils.ShellOrigin.creatFlieAndWrite;

public class getdata {
    public static void main(String[] args) {
        //getsql();
        puttemid();
    }
    public static void getsql(){
        HashSet hashSet=new HashSet();
        try {
            FileUtils.write(new File("configweb.txt"),"",false);
            Map arrrymap = readConfigSqlTxt("configsql.txt");
            for (Object key:arrrymap.keySet()
            ) {
                String domain= (String) arrrymap.get(key);
                String rand=rand(4);
                Boolean flag=hashSet.add(rand);
                while (!flag){
                    rand=rand(4);
                    flag=hashSet.add(rand);
                }
                String sql=rand+"=>"+domain;
                FileUtils.write(new File("configweb.txt"),sql+"\n",true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void puttemid(){
        try {
            FileUtils.write(new File("tempId.txt"),"",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map arrrymap = readConfigSqlTxt("configweb.txt");
        List list=new ArrayList();
        for (Object key:arrrymap.keySet()
             ) {
            for (int i = 1; i <=20000 ; i++) {
                String str=key.toString()+i;
                list.add(str);
            }
        }
        Collections.shuffle(list);
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {

            creatFlieAndWrite("tempId.txt",list.get(i)+"\n",true);

        }
    }

    public static Map readConfigSqlTxt(String path){
        Map map=new HashMap();
        String[] library;
        File file =new File(path);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                library= s.split("=>|\\t");  //获取数据库名
                if(library[0]!="")
                    map.put(library[0],library[1]);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //return  arrayList;
        return map;
    }
    public static String rand(int num){
        String chars = "abcdefghizklmnopqrstuvwxyzabcdefghizklmnopqrstuvwxyz";
        char c ;
        String rand="";
        for (int i=0;i<num;i++){
            c=chars.charAt((int)(Math.random() * chars.length()));
            rand+=c;
        }
        return rand;
    }
    public static void creatFlieAndWrite(String path,String context,Boolean append){


                File file1 = new File(path);
                if(!file1.exists() && path.indexOf(".txt")==-1){
                    file1.getParentFile().mkdirs();
                }else {
                    try {
                        file1.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileWriter fw = new FileWriter(file1, append);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(context);
                    bw.flush();
                    bw.close();
                    fw.close();
                    //System.out.println(library+":"+"名字："+context);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


    }
