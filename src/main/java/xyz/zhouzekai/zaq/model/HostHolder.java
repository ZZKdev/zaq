package xyz.zhouzekai.zaq.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<User> user = new ThreadLocal<>();

    public User getUser(){
        return user.get();
    }
    public void setUser(User user){
        this.user.set(user);
    }
    public void clear(){
        user.remove();
    }
}
