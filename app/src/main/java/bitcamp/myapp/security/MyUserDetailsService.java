package bitcamp.myapp.security;

import bitcamp.myapp.service.MemberService;
import bitcamp.myapp.vo.Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(new Object() {
  }.getClass().getEnclosingClass());
  //DBMS에서 사용자 정보를 찾기 위해 기존 서비스 객체를 쓰자.
  MemberService memberService;

  public MyUserDetailsService(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberService.get(username);
    if (member == null) {
      throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
    }
    return User.withDefaultPasswordEncoder().username(member.getName()).password(member.getPassword()).authorities("good").build();
  }
}
