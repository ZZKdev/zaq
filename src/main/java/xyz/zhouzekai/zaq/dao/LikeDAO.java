package xyz.zhouzekai.zaq.dao;

import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Repository;
import xyz.zhouzekai.zaq.util.RedisAdapter;

@Repository
public class LikeDAO {
    private final String LIKE_PREFIX = "zaq:comment:like";
    private final String SPLIT = ":";

    public void like(int commentId, int userId){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        command.sadd(LIKE_PREFIX + SPLIT + commentId, String.valueOf(userId));
    }

    public void dislike(int commentId, int userId){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        command.srem(LIKE_PREFIX + SPLIT + commentId, String.valueOf(userId));
    }

    public long selectLikeCount(int commentId){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        return command.scard(LIKE_PREFIX + SPLIT + commentId);
    }

    public boolean selectLikeStatus(int commentId, int userId){
        RedisCommands<String, String> command = RedisAdapter.getCommand();
        return command.sismember(LIKE_PREFIX + SPLIT + commentId, String.valueOf(userId));
    }
}
