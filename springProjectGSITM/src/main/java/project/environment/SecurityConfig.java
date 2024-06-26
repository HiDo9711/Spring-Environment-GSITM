package project.environment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //시큐리티가 클래스를 인식할 수 있도록 함
@EnableWebSecurity   // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다. 웹시큐리티를 사용하겠다. 
@EnableMethodSecurity(prePostEnabled =true)
public class SecurityConfig {

   @Bean 
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
      http.authorizeHttpRequests((authorizeHttpRequests) ->
      authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
      //접근
      .csrf(csrf -> csrf //ignoringRequestMatchers를 이용, h2-console/로 시작하는 모든 URL은 CSRF 검증을 하지 않겠다. 
    		  .ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
      		  .headers(headers -> headers
              .addHeaderWriter(new XFrameOptionsHeaderWriter(
                 XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
      		.headers(headers -> headers
      	            .addHeaderWriter(new XFrameOptionsHeaderWriter(
      	               XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
  	       .formLogin(formLogin -> formLogin
	  	            .loginPage("/user/login")
	  	            .defaultSuccessUrl("/")) //로그인에 성공하면 루트로 이동
  	       .logout(logout -> logout
  	            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) //로그아웃 url
  	            .logoutSuccessUrl("/") //로그아웃 성공하면 루트로 가라
  	            .invalidateHttpSession(true)); //로그아웃하면, 로그인하며 생성된 사용자의 세션도 삭제해라.

      return http.build();
   }
   
   @Bean   // Ioc(Inversion of Control)로 등록.
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   
   @Bean //AuthenticationManager :UserSecurityService,passWoedEncoder를 내부적으로 사용해 인증과 권한 부여를 처리함
   AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
      return authenticationConfiguration.getAuthenticationManager();
   }


   

}
