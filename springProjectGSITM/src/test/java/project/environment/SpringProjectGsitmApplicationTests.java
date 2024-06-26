package project.environment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.service.BoardService;
import project.environment.service.UserService;

import java.time.LocalDateTime;


@SpringBootTest
public class SpringProjectGsitmApplicationTests {

    @Autowired
    private BoardService boardService;
    
    @Autowired
    private UserService userService;

//    @Test
//    void testJpa() throws Exception {
//        for (int i = 1; i <= 300; i++) {
//        	MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
//        	
//        	Board board = new Board();
//        	board.setTitle(i+"번째 게시글 제목");
//        	board.setBoard_Content(i+"번째 게시글 제목");
//        	board.setCreate_Date(LocalDateTime.now());
//        	board.setModify_Date(LocalDateTime.now());
//        	board.setHit_Count(0L);
//            this.boardService.create(,file);
//        }
//    }
    
    // Siteuser 데이터 더미
    @Test
    void testSiteUser() throws Exception {
        for (int i = 1; i <= 20; i++) {      	
        	SiteUser user = new SiteUser();
        	
        	user.setLoginId("user"+i);
    		user.setName("user"+i);
    		user.setEmail("user"+i+"@gmail.com");
    		user.setPassword("user"+i);
    		user.setRegionNum("gwangju");
    		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    		user.setPassword(passwordEncoder.encode(user.getPassword())); 
            this.userService.createUser(user.getLoginId(),user.getName(),user.getEmail(),user.getPassword(),user.getRegionNum());
        }
    }
    
}
