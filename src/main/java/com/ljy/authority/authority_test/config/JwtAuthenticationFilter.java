package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.model.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//우리가 formlogin을 안쓰기때문에 /login요청을 처리하는 필터를 만드는것
//스프링시큐리티에서 UsernamePasswordAuthenticationFilter있음
// /login요청해서 username, password 을 post로 전송하면
// UsernamePasswordAuthenticationFilter 동작함
//시큐리티에 addFilter 해서 넣어주기
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserRepository userRepository;

   // private final AuthenticationManager authenticationManager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        System.out.println("로그인시도");
//1. username password 받아서
        //2. 정상인지 로그인 시도 authenticationManager이걸로 로그인시도를 하면 PrincipalDetailsService가 호출
        //그러면 loadByUsername()함수가 자동으로 실행됨

        //3.PrincipalDetails를 세션에 담고

        //qna q: 세션을 특정 서버의 메모리에 담게 되는 순간 jwt 쓰는 좋은 이유가 하나 사라지는게 아닌가?
        // a: 세션에 담는이유 권한관리를 위해 아닙니다. 세션을 request와 response 되는 그 시점까지 잠시 임시로 권한처리를 위해 쓰는 거라
        //세션 유지기간이 길지 않고, 세션을 공유할 필요도 없습니다.

        //4.JWT토큰을 만들어서 응답해주면 된다.
    }

}
