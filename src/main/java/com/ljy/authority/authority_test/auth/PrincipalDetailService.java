package com.ljy.authority.authority_test.auth;

import com.ljy.authority.authority_test.model.domain.Users;
import com.ljy.authority.authority_test.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 반환하는 UserDetailService
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username이 loginId이다.
        Users user = userRepository.selectUser(username);
        if(user.getUsername() == null){
            System.out.println("존재하지 않는 아이디입니다.");
        }
            return new PrincipalDetails(user);
       
    }
}
