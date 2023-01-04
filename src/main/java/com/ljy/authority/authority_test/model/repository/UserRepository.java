package com.ljy.authority.authority_test.model.repository;

import com.ljy.authority.authority_test.model.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRepository {

    User selectUserName(String username);
}
