package xyz.zhouzekai.zaq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.Comment;
import xyz.zhouzekai.zaq.service.CommentService;
import xyz.zhouzekai.zaq.service.LikeService;
import xyz.zhouzekai.zaq.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/comments")
    public Map<String, Object> addComment(@RequestParam("content") String content,
                                          @RequestParam("entityId") String entityId,
                                          @RequestParam("entityType") String entityType){
        Map<String, Object> map = new HashMap<>();
        try{
            commentService.addComment(Integer.valueOf(entityId), Integer.valueOf(entityType), content);
            map.put("code", 200);
        }catch (RestException e){
            map.put("code", e.getCode());
            map.put("msg", e.getMessage());
        }catch (Exception e){
            map.put("code", CODE.InternalServerError.getValue());
            logger.error("添加评论失败 " + e.getMessage());
        }
        return map;
    }

    @GetMapping("/comments/{entityId}")
    public String getComments(@PathVariable("entityId") String entityId, @RequestParam("entityType") String entityType){
        ObjectNode root = objectMapper.createObjectNode();
        try {
            List<Comment> comments = commentService.getCommentsByEntity(Integer.valueOf(entityId), Integer.valueOf(entityType));
            ArrayNode commentsNode = objectMapper.createArrayNode();
            for(Comment comment : comments){
                ObjectNode node = objectMapper.valueToTree(comment);
                node.put("likeCount", likeService.getLikeCount(comment.getId()));
                node.put("isLiked", likeService.getLikeStatus(comment.getId()));
                node.put("username", userService.getUser(comment.getUserId()).getName());
                commentsNode.add(node);
            }
            ObjectNode data = objectMapper.createObjectNode();
            data.set("comments", commentsNode);
            data.put("commentCount", commentService.getCommentCount(Integer.valueOf(entityId), Integer.valueOf(entityType)));
            root.set("data", data);
            root.put("code", CODE.OK.getValue());
        } catch (Exception e){
            root.put("code", CODE.InternalServerError.getValue());
        }
        return root.toString();
    }
}
