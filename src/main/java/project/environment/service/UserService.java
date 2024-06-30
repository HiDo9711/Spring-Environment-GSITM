package project.environment.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.environment.DataNotFoundException;
import project.environment.Repository.CommentsRepository;
import project.environment.Repository.UserRepository;
import project.environment.entity.SiteUser;
import project.environment.form.UserCreateForm;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final CommentsRepository commentsRepository;

	public SiteUser createUser(String loginId, String name, String email, String password, String region) {
		SiteUser siteUser = new SiteUser();
		siteUser.setLoginId(loginId);
		siteUser.setName(name);
		siteUser.setEmail(email);
		siteUser.setPassword(password);
		siteUser.setRegion(region);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		siteUser.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(siteUser);
		return siteUser;
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

	public void updateUser(UserCreateForm userCreateForm, String loginId) {
        Optional<SiteUser> optionalSiteUser = userRepository.findByLoginId(loginId);
        
        optionalSiteUser.ifPresent(siteUser -> {
            updateIfNotEmpty(siteUser, userCreateForm.getEmail(), userCreateForm.getRegion(), userCreateForm.getPassword1());
            userRepository.save(siteUser); // 변경된 정보 저장
        });
    }
	
	//수정 시 공란이라면 업데이트 되지 않음
	 private void updateIfNotEmpty(SiteUser siteUser, String email, String region, String newPassword) {
	        if (!email.isEmpty()) {
	            siteUser.setEmail(email);
	        }

	        if (!region.isEmpty()) {
	            siteUser.setRegion(region);
	        }

	        if (!newPassword.isEmpty()) {
	            // 비밀번호가 비어 있지 않은 경우, 새로운 비밀번호로 업데이트하고 암호화하여 저장
	            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	            String encryptedPassword = passwordEncoder.encode(newPassword);
	            siteUser.setPassword(encryptedPassword);
	        }
	    }
	
	public void deleteUser(String loginId) {
	    // loginId로 사용자를 찾기
	    Optional<SiteUser> siteUserOptional = userRepository.findByLoginId(loginId);
	    
	    if (siteUserOptional.isPresent()) {
	        SiteUser siteUser = siteUserOptional.get();
	        userRepository.delete(siteUser);
	    } else {
	        throw new DataNotFoundException("User not found with loginId: " + loginId);
	    }
	}
	
	

}