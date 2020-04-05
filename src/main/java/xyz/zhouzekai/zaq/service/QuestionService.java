package xyz.zhouzekai.zaq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import xyz.zhouzekai.zaq.dao.QuestionDAO;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.HostHolder;
import xyz.zhouzekai.zaq.model.Question;

import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    HostHolder hostHolder;

    // 添加问题
    // 添加问题成功则返回问题 ID
    public int addQuestion(String title, String content) throws RestException {
        Question question = new Question();
        // 未登录
        if(hostHolder.getUser() == null){
            throw new RestException(CODE.Unauthorized, "用户未登录");
        }
        question.setUserId(hostHolder.getUser().getId());
        question.setCreatedDate(new Date());
        // 过滤 html 标签, 转义
        question.setTitle(HtmlUtils.htmlEscape(title));
        question.setContent(HtmlUtils.htmlEscape(content));
        // 添加问题
        questionDAO.addQuestion(question);
        return question.getId();
    }
    public Question getById(int id) throws RestException {
        Question question = questionDAO.selectById(id);
        if(question == null){
            throw new RestException(CODE.NotFound, "问题不存在或已被删除");
        }
        return question;
    }
    public List<Question> getLatestQuestion(int offset, int limit){
        return questionDAO.selectLatestQuestions(offset, limit);
    }
}
