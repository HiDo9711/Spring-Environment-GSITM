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

//    @Test
//    public void createMultipleDummyData() throws Exception {
//        MockitoAnnotations.initMocks(this);
//
//        for (int i = 1; i <= 120; i++) {
//            String title = "테스트 제목 " + i;
//            String content = "테스트 내용입니다. " + i;
//
//            SiteUser user = new SiteUser();
//            user.setId((long) i); // 예시로 간단하게 ID 설정
//            user.setRegionNum("paju");
//            user.setLoginId("testuser" + i);
//            user.setEmail("testuser" + i + "@gmail.com");
//            user.setPassword("testuser" + i);
//            user.setName("testuser" + i);
//
//            // MockMultipartFile을 사용하여 가짜 파일 생성
//            MultipartFile mockFile = new MockMultipartFile("file", "testfile_" + i + ".txt", "text/plain", ("test file content " + i).getBytes());
//
//            // create 메서드 호출
//            boardService.create(title, content, user, mockFile, false);
//        }
//    }
}
