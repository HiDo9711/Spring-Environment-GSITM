package project.environment;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
    
    
    @Test
    public void testCreateSiteUser() throws Exception {
    	for (int i = 1; i <= 200; i++) { 
    		String loginId = "user"+i;
    		String name =  "user"+i;
    		String email = "user"+i+"gmail.com"; 
    		String password = "user"+i; 
    		String regionNum = "동두천시";
    		
    		userService.createUser(loginId, name, email, password, regionNum);
    		
    	}
    }

    @Test
    public void createMultipleDummyData() throws Exception {
        MockitoAnnotations.initMocks(this);

        for (int i = 1; i <= 200; i++) {
        	String title = i+"번째 게시글 제목";
        	String content = i+"번째 게시글 내용";
        	String username = "user"+i;
        	// MockMultipartFile을 사용하여 가짜 파일 생성
            MultipartFile mockFile = new MockMultipartFile("file", "testfile_" + i + ".txt", "text/plain", ("test file content " + i).getBytes());

            // create 메서드 호출
            boardService.create(title, content, userService.getUser(username), mockFile, false);
        }
    }
}
