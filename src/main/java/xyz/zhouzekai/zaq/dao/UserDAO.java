package xyz.zhouzekai.zaq.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.zhouzekai.zaq.model.User;

@Mapper
@Repository
public interface UserDAO {
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert({"insert into Users(name, password, salt) values(#{name}, #{password}, #{salt})"})
    int addUser(User user);

    @Select({"select id, name, password, salt from Users where name=#{name}"})
    User selectByName(String name);

    @Select({"select id, name, password, salt from Users where id=#{id}"})
    User selectById(int id);
}
