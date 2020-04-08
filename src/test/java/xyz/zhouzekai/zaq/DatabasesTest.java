package xyz.zhouzekai.zaq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import xyz.zhouzekai.zaq.dao.CommentDAO;
import xyz.zhouzekai.zaq.dao.QuestionDAO;
import xyz.zhouzekai.zaq.dao.UserDAO;
import xyz.zhouzekai.zaq.model.Comment;
import xyz.zhouzekai.zaq.model.Question;
import xyz.zhouzekai.zaq.model.User;
import xyz.zhouzekai.zaq.service.UserService;
import xyz.zhouzekai.zaq.util.MD5Util;

import java.awt.image.ImageProducer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Sql("classpath:init-schema.sql")
public class DatabasesTest {
//    @Autowired
//    UserService userService;
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testDatabse() throws JsonProcessingException {
//        Question question = questionDAO.selectById(12);
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode root = mapper.createObjectNode();
//        root.put("code", 200);
//        ObjectNode data = mapper.valueToTree(question);
//        data.put("username", userDAO.selectById(question.getUserId()).getName());
//        root.set("data", data);
        List<Question> list = questionDAO.selectLatestQuestions(0, 2);
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode questions = objectMapper.createArrayNode();
        for(Question question : list){
            ObjectNode node = objectMapper.valueToTree(question);
            node.put("username", userDAO.selectById(question.getUserId()).getName());
            questions.add(node);
        }
        root.put("code", 200);
        root.set("data", questions);

//        ObjectNode root = mapper.valueToTree(question);
//        root.put("publisher", "zzk");
//        root.putPOJO("comments", comments);
//        root.put("code", 200);
//        root.put("data", )
        System.out.println(root.toString());
    }
    @Test
    public void testDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
        Question question = new Question();
        question.setUserId(2);
        question.setCreatedDate(new Date());
        question.setTitle("123");
        question.setContent("123");
        questionDAO.addQuestion(question);
        System.out.println(df.format(question.getCreatedDate()));
        question = questionDAO.selectById(question.getId());
        System.out.println(df.format(question.getCreatedDate()));
    }
}
