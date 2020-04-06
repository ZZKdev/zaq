package xyz.zhouzekai.zaq.dao;

import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Repository;
import xyz.zhouzekai.zaq.util.RedisAdapter;

@Repository
public class LoginTicketDAO {
    // Redis session key 前缀
    private final String SESSION_PREFIX = "zaq:session";
    // 分隔符
    private final String SPLIT = ":";

    public void addTicket(String ticket, int userId, int expires){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        command.setex(SESSION_PREFIX + SPLIT +  ticket, expires, String.valueOf(userId));
    }

    public int selectByTicket(String ticket){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        String userId = command.get(SESSION_PREFIX + SPLIT  + ticket);
        if(userId == null) return -1;
        return Integer.valueOf(userId);
    }

    public void deleteByTicket(String ticket){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        command.del(SESSION_PREFIX + SPLIT + ticket);
    }
}
