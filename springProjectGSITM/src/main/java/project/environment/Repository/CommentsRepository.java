package project.environment.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.environment.entity.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}