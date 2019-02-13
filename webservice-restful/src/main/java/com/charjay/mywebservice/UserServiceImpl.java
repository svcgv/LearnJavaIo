package com.charjay.mywebservice;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class UserServiceImpl implements UserService{

    public List<User> getUsers() {
        return Storage.users;
    }

    public Response delete(int id) { //0 1
//        Storage.users.remove(id);
        Response response=new Response();
        response.setCode("00");
        response.setMsg("succes");
        return response;
    }

    public User getUser(int id) {
        return Storage.users.get(id);
    }

    public Response insert(User user) {
        return null;
    }

    public Response update(User user) {
        return null;
    }
}
