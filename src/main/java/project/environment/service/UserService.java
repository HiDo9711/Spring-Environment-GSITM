package project.environment.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.environment.Repository.UserRepository;
import project.environment.entity.SiteUser;
import project.environment.exception.DataNotFoundException;
import project.environment.form.UserCreateForm;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

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
            userRepository.save(siteUser); 
        });
    }
	
	 private void updateIfNotEmpty(SiteUser siteUser, String email, String region, String newPassword) {
	        if (!email.isEmpty()) {
	            siteUser.setEmail(email);
	        }

	        if (!region.isEmpty()) {
	            siteUser.setRegion(region);
	        }

	        if (!newPassword.isEmpty()) {
	            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	            String encryptedPassword = passwordEncoder.encode(newPassword);
	            siteUser.setPassword(encryptedPassword);
	        }
	    }
	
	public void deleteUser(String loginId) {

	    Optional<SiteUser> siteUserOptional = userRepository.findByLoginId(loginId);
	    
	    if (siteUserOptional.isPresent()) {
	        SiteUser siteUser = siteUserOptional.get();
	        userRepository.delete(siteUser);
	    } else {
	        throw new DataNotFoundException("User not found with loginId: " + loginId);
	    }
	}
	
	

}