package com.tvd12.properties.file.testing.model.impl;

import com.tvd12.properties.file.testing.model.UserActionHandler;

public class UserBettingHandler implements UserActionHandler {

    @Override
    public void handle(long money) {
        System.out.println("User betted $" + money);
    }
}
