package com.charjay.lua;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class LuaTest {

    public static void main(String[] args) {
        Jedis jedis = RedisManger.getJedis();
        String lua="local num=redis.call('incr',KEYS[1])\n" +
                "if tonumber(num)==1 then\n" +
                "   redis.call('expire',KEYS[1],ARGV[1])\n" +
                "   return 1\n" +
                "elseif tonumber(num)>tonumber(ARGV[2]) then\n" +
                "   return 0\n" +
                "else\n" +
                "   return 1\n" +
                "end";
        List<String> keys = new ArrayList<>();
        keys.add("ip:limit:127.0.0.1");
        List<String> argss = new ArrayList<>();
        argss.add("6000");
        argss.add("10");
//        Object obj = jedis.eval(lua, keys, argss);
        Object obj = jedis.evalsha(jedis.scriptLoad(lua), keys, argss);//把脚本缓存起来
        System.out.println(obj);

    }
}
