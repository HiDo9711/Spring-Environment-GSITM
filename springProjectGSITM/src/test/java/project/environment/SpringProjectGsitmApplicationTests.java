package project.environment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import project.environment.entity.Board;
import project.environment.service.BoardService;

import java.time.LocalDateTime;


@SpringBootTest
public class SpringProjectGsitmApplicationTests {

    @Autowired
    private BoardService boardService;

    @Test
    void testJpa() throws Exception {
        for (int i = 1; i <= 300; i++) {
        	MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        	
        	Board board = new Board();
        	board.setTitle(i+"번째 게시글 제목");
        	board.setBoard_Content(i+"번째 게시글 제목");
        	board.setCreate_Date(LocalDateTime.now());
        	board.setModify_Date(LocalDateTime.now());
        	board.setHit_Count(0L);
            this.boardService.create(board,file);
        }
    }
}
