package project.environment.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.environment.entity.Board;
import project.environment.entity.SiteUser;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
	Page<Board> findAll(Pageable pageable);
	List<Board> findByUser(SiteUser user);
	
}