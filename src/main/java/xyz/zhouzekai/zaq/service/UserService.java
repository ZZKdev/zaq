package xyz.zhouzekai.zaq.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhouzekai.zaq.dao.LoginTicketDAO;
import xyz.zhouzekai.zaq.dao.UserDAO;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.User;
import xyz.zhouzekai.zaq.util.MD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public String register(String username, String password) throws RestException {
        if(StringUtils.isBlank(username)){
            throw new RestException(CODE.BadRequest, "用户名不能为空");
        }
        if(StringUtils.isBlank(password)){
            throw new RestException(CODE.BadRequest, "密码不能为空");
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            throw new RestException(CODE.Forbidden, "用户名已经被注册");
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(MD5Util.MD5(password + user.getSalt()));
        userDAO.addUser(user);
        String ticket = getLoginTicket(user.getId());
        return ticket;
    }

    public String login(String username, String password) throws RestException {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            throw new RestException(CODE.BadRequest, "用户名不能为空");
        }
        if(StringUtils.isBlank(password)){
            throw new RestException(CODE.BadRequest, "密码不能为空");
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            throw new RestException(CODE.Unauthorized, "用户名或者密码错误");
        }
        if(!MD5Util.MD5(password + user.getSalt()).equals(user.getPassword())){
            throw new RestException(CODE.Unauthorized, "用户名或者密码错误");
        }
        String ticket = getLoginTicket(user.getId());
        return ticket;
    }


    private String getLoginTicket(int userId){
        String ticket = UUID.randomUUID().toString().replaceAll("-", "");
        // ticket 有效期为 1 天
        loginTicketDAO.addTicket(ticket, 3600 * 24, userId);
        return ticket;
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
