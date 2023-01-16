package com.ljy.authority.authority_test.model.repository;

import com.ljy.authority.authority_test.model.domain.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    Users selectUser(String username);
    
}
