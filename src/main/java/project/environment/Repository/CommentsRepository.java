package project.environment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
	List<Comments> findByUser(SiteUser user);
}