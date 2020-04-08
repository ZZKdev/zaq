package xyz.zhouzekai.zaq.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.zhouzekai.zaq.model.Question;

import java.util.List;

@Mapper
@Repository
public interface QuestionDAO {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({"insert into Question(title, content, created_date, user_id)",
            "values(#{title}, #{content}, #{createdDate}, #{userId})"})
    int addQuestion(Question question);

    @Select({"select id, title, created_date, user_id from Question",
            "order by id desc limit #{offset}, #{limit}"})
    List<Question> selectLatestQuestions(@Param("offset") int offset, @Param("limit") int limit);

//    @Select({"select id, title, comment_count, created_date, user_id from Question",
//            "order by id desc limit #{offset}, #{limit}"})
//    List<Question> selectLatestQuestionsWithoutContent(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"select id, title, content, created_date, user_id from Question where id=#{id}"})
    Question selectById(int id);

//    int updateComment(@Param("id") int id, @Param("commentCount") int commentCount);
}
