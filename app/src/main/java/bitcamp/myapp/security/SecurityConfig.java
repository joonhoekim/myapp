package bitcamp.myapp.security;

import bitcamp.myapp.controller.AssignmentController;
import bitcamp.myapp.service.MemberService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final Log log = LogFactory.getLog(SecurityConfig.class);

  public SecurityConfig() {
    log.debug("SecurityConfig 객체 생성됨!");
  }

  // Spring Security를 처리할 필터 체인을 준비한다.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .mvcMatchers("member/form", "/member/add", "/img/*").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin(formLoginConfigurer -> {
          formLoginConfigurer
              .loginPage("/auth/form") // 로그인 폼을 제공하는 URL (페이지컨트롤러)
              .loginProcessingUrl("/auth/login") // 로그인을 처리하는 URL (이 URL로 요청 들어오면 내가 로그인 처리할 것이다. 이 URL에 대응하는 페이지 컨트롤러는 없어도 된다.)
              .usernameParameter("email") // 로그인 수행할 때 사용할 사용자 아이디 또는 이메일(principal) 파라미터 명
              .passwordParameter("password") // 로그인 수행할 때 사용할 사용자 암호(credential) 파라미터 명
              .defaultSuccessUrl("/home", true) // 로그인 성공 후 리다이렉트 할 URL
              .permitAll(); // 모든 권한 부여
        });
//    http.csrf().disable();
    return http.build();
  }

  // 사용자 인증을 처리할 서비스 객체를 준비한다.
  // 이 빈은 '사용자 정보를 리턴해 줄 객체'를 리턴할 것이다. (멤버서비스)
  @Bean
  public UserDetailsService userDetailsService(MemberService memberService) {
    //직접 만든 UserDetailsService 객체를 등록한다.
    return new MyUserDetailsService(memberService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new SimplePasswordEncoder();
  }
}
