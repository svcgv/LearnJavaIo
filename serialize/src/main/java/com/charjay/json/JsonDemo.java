package com.charjay.json;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.charjay.Person;

/**
 * 各种序列化效率比对
 */
public class JsonDemo {
    static int times=1000;
    //初始化
    private static Person init(){
        Person person=new Person();
        person.setName("mic");
        person.setAge(18);
        return person;
    }

    public static void main(String[] args) throws Exception {
        excuteWithJava();
        excuteWithJack();
        excuteWithFastJson();
        excuteWithProtoBuf();
        excuteWithHessian();
    }
    private static void excuteWithJava() throws Exception {
        Person person=init();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oo=new ObjectOutputStream(out);
        Long start=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            oo.writeObject(person);
        }
        System.out.println("java序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+out.toByteArray().length);
        Long start2=System.currentTimeMillis();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(out.toByteArray())));
        Person person1=(Person)ois.readObject();
        System.out.println("反序列化："+person1+"："+(System.currentTimeMillis()-start2)+"ms");
    }
    private static void excuteWithJack() throws IOException {
        Person person=init();

        ObjectMapper mapper=new ObjectMapper();
        byte[] writeBytes=null;
        Long start=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            writeBytes=mapper.writeValueAsBytes(person);
        }
        System.out.println("jackson序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+writeBytes.length);
        Long start2=System.currentTimeMillis();
        Person person1=mapper.readValue(writeBytes,Person.class);
        System.out.println("反序列化："+person1+"："+(System.currentTimeMillis()-start2)+"ms");
    }


    private static void excuteWithFastJson() throws IOException {
        Person person=init();
        String text=null;
        Long start=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            text=JSON.toJSONString(person);
        }
        System.out.println("fastjson序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+text.getBytes().length);
        Long start2=System.currentTimeMillis();
        Person person1=JSON.parseObject(text,Person.class);
        System.out.println("反序列化："+person1+"："+(System.currentTimeMillis()-start2)+"ms");
    }

    private static void excuteWithProtoBuf() throws IOException {
        Person person=init();
        Codec<Person> personCodec= ProtobufProxy.create(Person.class,false);

        byte[] bytes=null;
        Long start=System.currentTimeMillis();
        for(int i=0;i<times;i++){
             bytes=personCodec.encode(person);
        }
        System.out.println("protobuf序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+bytes.length);
        Long start2=System.currentTimeMillis();
        Person person1=personCodec.decode(bytes);
        System.out.println("反序列化："+person1+"："+(System.currentTimeMillis()-start2)+"ms");
    }

    private static void excuteWithHessian() throws IOException {
        Person person=init();

        ByteArrayOutputStream os=new ByteArrayOutputStream();
        HessianOutput ho=new HessianOutput(os);
        int len = 0;
        Long start=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            ho.writeObject(person);
            if(i==0){
                len=os.toByteArray().length;
            }
        }
        System.out.println("Hessian序列化："+(System.currentTimeMillis()-start)+"ms : " +
                "总大小->"+len);

        HessianInput hi=new HessianInput(new ByteArrayInputStream(os.toByteArray()));
        Long start2=System.currentTimeMillis();
        Person person1=(Person)hi.readObject();
        System.out.println("反序列化："+person1+"："+(System.currentTimeMillis()-start2)+"ms");
    }
}
