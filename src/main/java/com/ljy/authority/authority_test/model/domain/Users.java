package com.ljy.authority.authority_test.model.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@Alias("users")
public class Users {
    private Long id;
    private String username;
    private String password;
    private String roles;

    /**
     * roles컬럼에 ADMIN,USER등 여러개가 들어있을때 콤마를 기준으로 하여 가져오는 메서드
     * 만약 리스트가 없을경우에는 빈 ArrayList를 리턴한다
     * @return
     */
    public List<String> getRoleList(){
        if(this.roles.length()>0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
