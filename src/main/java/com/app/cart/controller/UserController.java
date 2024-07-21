package com.app.cart.controller;

import com.app.cart.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(method = RequestMethod.GET,value ="/initial-save", produces = "application/json")
    public void saveInitialBatch(){
        userService.saveInitialBatch();
    }
}
