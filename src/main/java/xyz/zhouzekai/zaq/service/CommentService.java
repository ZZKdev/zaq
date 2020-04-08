package xyz.zhouzekai.zaq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import xyz.zhouzekai.zaq.dao.CommentDAO;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.Comment;
import xyz.zhouzekai.zaq.model.HostHolder;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    HostHolder hostHolder;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(int entityId, int entityType, String content) throws RestException {
        if(hostHolder.getUser() == null){
            throw new RestException(CODE.Unauthorized, "用户未登录");
        }
        Comment comment = new Comment();
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        comment.setCreatedDate(new Date());
        comment.setUserId(hostHolder.getUser().getId());
        // html 标签转义
        comment.setContent(HtmlUtils.htmlEscape(content));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.selectCommentCountByEntity(entityId,entityType);
    }
}
