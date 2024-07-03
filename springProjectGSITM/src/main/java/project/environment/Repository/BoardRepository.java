package project.environment.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.environment.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
//	Board findByTitle(String title);
//    Board findByTitleAndBoard_Content(String title, String board_Content);
//    List<Board> findByTitleLike(String title);
	Page<Board> findAll(Pageable pageable);
	
}