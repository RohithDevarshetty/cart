package com.app.cart.service;

import com.app.cart.dto.UserDTO;

public interface UserService {
    void saveInitialBatch();

    UserDTO getUser(Long userId);
}
