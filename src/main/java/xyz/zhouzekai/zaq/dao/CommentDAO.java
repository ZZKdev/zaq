package xyz.zhouzekai.zaq.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.zhouzekai.zaq.model.Comment;

import java.util.List;

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = "Comment";
    String INSERT_FIELDS = "user_id, content, created_date, entity_id, entity_type, status";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")",
            " values(#{userId}, #{content}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
