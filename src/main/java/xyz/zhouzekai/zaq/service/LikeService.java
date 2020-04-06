package xyz.zhouzekai.zaq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhouzekai.zaq.dao.LikeDAO;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.HostHolder;
import xyz.zhouzekai.zaq.model.User;

@Service
public class LikeService {
    @Autowired
    LikeDAO likeDAO;

    @Autowired
    HostHolder hostHolder;

    public long like(int commentId) throws RestException {
        User user = hostHolder.getUser();
        if(user == null){
            throw new RestException(CODE.Unauthorized, "用户未登录");
        }
        likeDAO.like(commentId, user.getId());
        return likeDAO.selectLikeCount(commentId);
    }

    public long dislike(int commentId) throws RestException {
        User user = hostHolder.getUser();
        if(user == null){
            throw new RestException(CODE.Unauthorized, "用户未登录");
        }
        likeDAO.dislike(commentId, user.getId());
        return likeDAO.selectLikeCount(commentId);
    }

    public long getLikeCount(int commentId){
        return likeDAO.selectLikeCount(commentId);
    }

    public boolean getLikeStatus(int commentId){
        User user = hostHolder.getUser();
        // 未登录默认没有点赞
        if(user == null) return false;
        return likeDAO.selectLikeStatus(commentId, user.getId());
    }
}
