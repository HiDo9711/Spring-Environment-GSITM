package project.environment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.environment.Repository.UserRepository;
import project.environment.entity.User;
import project.environment.entity.UserRole;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String loginid) throws UsernameNotFoundException  {

      Optional<User> _user  = this.userRepository.findByLoginId(loginid);
      //사용자 정보 없을 시
      if (_user.isEmpty()) {
         throw new UsernameNotFoundException ("사용자를 찾을수 없습니다.");
      }

      User user = _user.get(); //get으로 가지고 옴

      List<GrantedAuthority> authorities = new ArrayList<>(); 
      if ("admin".equals(loginid)) {
    	  //true이면 ADMIN의 getValue값 가지고 옴(UserRole에서)
         authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
      } else {
         authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
      }
      //user객체 생성, 이 객체는 스프링 시큐리티에서 사용하며 user생성자에는 id,pw,권한 리스트가 전달된다. 
      return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(), authorities);

   }
}