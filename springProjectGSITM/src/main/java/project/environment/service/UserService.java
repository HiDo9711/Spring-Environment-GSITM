package project.environment.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.environment.DataNotFoundException;
import project.environment.Repository.UserRepository;
import project.environment.entity.SiteUser;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public SiteUser createUser(String loginId, String name, String email, String password, String regionNum) {
		SiteUser user = new SiteUser();
		user.setLoginId(loginId);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRegionNum(regionNum);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(password)); 
		this.userRepository.save(user);
		return user;
	}

	//userName 알아오기
	public SiteUser getUser(String loginId) {
		Optional<SiteUser> siteUser = this.userRepository.findByLoginId(loginId);

		if(siteUser.isPresent()) {
			return siteUser.get();
		}else{
			throw new DataNotFoundException("siteuser not found");

		}
	}

}