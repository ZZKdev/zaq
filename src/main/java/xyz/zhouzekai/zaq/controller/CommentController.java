package xyz.zhouzekai.zaq.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.Comment;
import xyz.zhouzekai.zaq.service.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

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

}
