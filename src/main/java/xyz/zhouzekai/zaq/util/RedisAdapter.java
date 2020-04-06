package xyz.zhouzekai.zaq.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisAdapter {

    private static StatefulRedisConnection<String, String> connection = RedisClient.create("redis://localhost").connect();

    public static RedisCommands<String, String> getCommand(){
        return connection.sync();
    }

}
