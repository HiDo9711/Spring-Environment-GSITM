package project.environment.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.environment.entity.Board;
import project.environment.entity.SiteUser;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
	Page<Board> findAll(Pageable pageable);
	List<Board> findByUser(SiteUser user);
	
	@Query(value = "SELECT b.* " +
            "FROM Board b " +
            "JOIN (SELECT BOARD_BOARD_NUM, COUNT(*) AS RECOMMEND_COUNT " +
            "      FROM BOARD_RECOMMENDER " +
            "      GROUP BY BOARD_BOARD_NUM " +
            "      ORDER BY RECOMMEND_COUNT DESC " +
            "      LIMIT 3) br ON b.board_num = br.BOARD_BOARD_NUM",
            nativeQuery = true)
    List<Board> findTop3Recommender();
	
	@Query(value = "SELECT u.region, COUNT(b.board_num) AS num_posts " +
            "FROM Board b " +
            "JOIN Site_User u ON b.user_id = u.id " +
            "GROUP BY u.region " +
            "ORDER BY num_posts DESC " +
            "LIMIT 3",
            nativeQuery = true)
    List<Object[]> findTop3Regions();
}