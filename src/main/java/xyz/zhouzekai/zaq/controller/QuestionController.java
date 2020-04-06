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
import xyz.zhouzekai.zaq.model.EntityType;
import xyz.zhouzekai.zaq.model.Question;
import xyz.zhouzekai.zaq.service.CommentService;
import xyz.zhouzekai.zaq.service.LikeService;
import xyz.zhouzekai.zaq.service.QuestionService;
import xyz.zhouzekai.zaq.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/questions")
    public Map<String, Object> addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        Map<String, Object> map = new HashMap<>();
        try {
            int questionId = questionService.addQuestion(title, content);
            map.put("code", CODE.OK.getValue());
            map.put("questionId", questionId);
        } catch (RestException e) {               // 可以捕获的已知异常
            map.put("code", e.getCode());
            map.put("msg", e.getMessage());
        } catch (Exception e){                   // 未知异常, 直接返回 500
            map.put("code", CODE.InternalServerError.getValue());
            logger.error("问题添加异常" + e.getMessage());
        }
        return map;
    }

    @GetMapping("/questions/{id}")
    public String getQuestionDetail(@PathVariable("id") String id){
        ObjectNode root = objectMapper.createObjectNode();
        try{
            ObjectNode data = objectMapper.createObjectNode();
            // 获取问题信息和发布者
            Question question = questionService.getById(Integer.valueOf(id));
            ObjectNode questionNode = objectMapper.valueToTree(question);
            questionNode.put("publisher", userService.getUser(question.getUserId()).getName());
            data.set("question", questionNode);
            // 获取评论和评论者用户名
            List<Comment> comments = commentService.getCommentsByEntity(question.getId(), EntityType.QUESTION);
            ArrayNode commentsNode = objectMapper.createArrayNode();
            for(Comment comment : comments){
                ObjectNode node = objectMapper.valueToTree(comment);
                node.put("username", userService.getUser(comment.getUserId()).getName());
                node.put("likeCount", likeService.getLikeCount(comment.getId()));
                node.put("likeStatus", likeService.getLikeStatus(comment.getId()));
                commentsNode.add(node);
            }
            data.set("comments", commentsNode);

            root.set("data", data);
            root.put("code", CODE.OK.getValue());
        }catch (RestException e){               // 可以捕获的已知异常
            root.put("code", e.getCode());
            root.put("msg", e.getMessage());
        }catch (Exception e){                   // 未知异常, 直接返回 500
            root.put("code", CODE.InternalServerError.getValue());
            logger.error("获取问题详情异常 " + e.getMessage());
        }
        return root.toString();
    }

    @GetMapping("/questions")
    public String getQuestionList(@RequestParam("offset") String offset, @RequestParam("limit") String limit){
        ObjectNode root = objectMapper.createObjectNode();
        try{
            List<Question> questions = questionService.getLatestQuestion(Integer.valueOf(offset), Integer.valueOf(limit));
            ArrayNode questionsNode = objectMapper.createArrayNode();
            for(Question question : questions){
                ObjectNode node = objectMapper.valueToTree(question);
                // 获取用户名
                node.put("username", userService.getUser(question.getUserId()).getName());
                questionsNode.add(node);
            }
            root.put("code", 200);
            root.set("data", questionsNode);
        }catch (Exception e){                   // 未知异常, 直接返回 500
            root.put("code", CODE.InternalServerError.getValue());
            logger.error("获取问题列表失败 " +  e.getMessage());
        }
        return root.toString();
    }
}
