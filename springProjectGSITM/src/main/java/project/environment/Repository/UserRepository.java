package project.environment.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import project.environment.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long>{
	//DB에서 사용자를 조회 : Optional -> null값을 error로 처리하지 않음. "없구나"
	Optional<SiteUser> findByLoginId(String loginId);
}