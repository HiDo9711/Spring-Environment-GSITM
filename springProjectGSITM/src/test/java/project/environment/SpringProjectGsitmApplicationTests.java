package project.environment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.environment.entity.Board;
import project.environment.service.BoardService;

import java.time.LocalDateTime;


@SpringBootTest
public class SpringProjectGsitmApplicationTests {

    @Autowired
    private BoardService boardService;

    @Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
        	Board board = new Board();
        	board.setTitle(i+"번째 게시글 제목");
        	board.setBoard_Content(i+"번째 게시글 제목");
        	board.setCreate_Date(LocalDateTime.now());
        	board.setModify_Date(LocalDateTime.now());
        	board.setHit_Count(0L);
            this.boardService.create(board);
        }
    }
}
