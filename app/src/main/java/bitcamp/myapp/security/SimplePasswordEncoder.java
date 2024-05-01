package bitcamp.myapp.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SimplePasswordEncoder implements PasswordEncoder {

  //값을 비교
  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    //DB보관한 암호와 사용자가 입력한 암호가 같은지 비교
    if(rawPassword == encodedPassword) return true;
    return false;
  }

  //값을 암호화
  @Override
  public String encode(CharSequence rawPassword) {
    //사용자 입력을 암호화하여 리턴함
    try{
      Encoder encoder = Base64.getEncoder();
      //BASE64는 그냥 바이너리를 문자열로 인코딩한 것. 암호화와 무관함.
      return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
