package project.environment.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import project.environment.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	Optional<SiteUser> findByLoginId(String loginId);
}