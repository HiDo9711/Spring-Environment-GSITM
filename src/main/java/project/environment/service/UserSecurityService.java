package project.environment.service;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import project.environment.Repository.UserRepository;
import project.environment.entity.SiteUser;
import project.environment.entity.UserRole;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String loginid) throws UsernameNotFoundException  {

      Optional<SiteUser> _user  = this.userRepository.findByLoginId(loginid);

      if (_user.isEmpty()) {
         throw new UsernameNotFoundException ("사용자를 찾을수 없습니다.");
      }

      SiteUser siteUser = _user.get(); 

      List<GrantedAuthority> authorities = new ArrayList<>(); 
      if ("admin".equals(loginid)) {
    	  
         authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
      } else {
         authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
      }
      
      return new org.springframework.security.core.userdetails.User(siteUser.getLoginId(), siteUser.getPassword(), authorities);

   }
}