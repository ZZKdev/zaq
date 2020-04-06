package xyz.zhouzekai.zaq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.service.LikeService;

@RestController
@RequestMapping("/api/v1")
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @PutMapping("/comments/{id}/like")
    public String like(@PathVariable("id") String id){
        ObjectNode root = objectMapper.createObjectNode();
        try {
            long likeCount = likeService.like(Integer.valueOf(id));
            root.put("data", likeCount);
            root.put("code", CODE.OK.getValue());
        } catch (RestException e) {
            root.put("code", e.getCode());
            root.put("msg", e.getMessage());
        } catch (Exception e){
            root.put("code", CODE.InternalServerError.getValue());
            logger.error("点赞异常 " + e.getMessage());
        }
        return root.toString();
    }

    @DeleteMapping("/comments/{id}/like")
    public String dislike(@PathVariable("id") String id){
        ObjectNode root = objectMapper.createObjectNode();
        try {
            long likeCount = likeService.dislike(Integer.valueOf(id));
            root.put("data", likeCount);
            root.put("code", CODE.OK.getValue());
        } catch (RestException e) {
            root.put("code", e.getCode());
            root.put("msg", e.getMessage());
        } catch (Exception e){
            root.put("code", CODE.InternalServerError.getValue());
            logger.error("取消点赞异常 " + e.getMessage());
        }
        return root.toString();
    }

}
