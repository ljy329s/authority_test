package com.ljy.authority.authority_test.model.repository;

import com.ljy.authority.authority_test.model.domain.User;

public interface UserRepository {

    User selectUserName(String username);
}
