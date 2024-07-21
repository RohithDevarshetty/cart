package com.app.cart.service.impl;

import com.app.cart.dto.UserDTO;
import com.app.cart.entity.User;
import com.app.cart.mapper.UserMapper;
import com.app.cart.repository.UserRepository;
import com.app.cart.service.UserService;
import com.app.cart.utils.ValidatorUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidatorUtil validatorUtil;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ValidatorUtil validatorUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validatorUtil = validatorUtil;
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveInitialBatch() {
        List<User> users = new ArrayList<>();
        users.add(new User("Rohith", "rohith@gmail.com"));
        users.add(new User("Anjali", "anjali.rao@example.com"));
        users.add(new User("Vikram", "vikram.singh@example.com"));
        users.add(new User("Sneha", "sneha.patel@example.com"));
        users.add(new User("Amit", "amit.sharma@example.com"));
        users.add(new User("Priya", "priya.verma@example.com"));
        users.add(new User("Rahul", "rahul.kumar@example.com"));
        users.add(new User("Sonia", "sonia.agarwal@example.com"));
        users.add(new User("Karan", "karan.jain@example.com"));
        users.add(new User("Pooja", "pooja.shah@example.com"));

        userRepository.saveAll(users);
    }

    public UserDTO getUser(Long userId){
        validatorUtil.validateUser(userId);
        Optional<User> userOptional = userRepository.findById(userId);
        return userMapper.toDto(userOptional.get());
    }


}
