package xyz.zhouzekai.zaq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zhouzekai.zaq.exception.CODE;
import xyz.zhouzekai.zaq.exception.RestException;
import xyz.zhouzekai.zaq.model.User;
import xyz.zhouzekai.zaq.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @PostMapping("/users")
    public Map<String, Object> register(@RequestParam("username") String username, @RequestParam("password") String password){
        Map<String, Object> map = new HashMap<>();
        try{
            String ticket = userService.register(username, password);
            map.put("ticket", ticket);
            map.put("code", CODE.OK.getValue());
        }catch (RestException e){
            map.put("msg", e.getMessage());
            map.put("code", e.getCode());
        }catch (Exception e){
            logger.error("注册异常", e);
        }
        return map;
    }

}
