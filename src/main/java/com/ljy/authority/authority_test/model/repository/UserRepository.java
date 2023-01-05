package com.ljy.authority.authority_test.model.repository;

import com.ljy.authority.authority_test.model.domain.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRepository {

    Users selectUser(String username);

    void insertUser(String username, String newPassword);

    List<Users> selectAll();
}
