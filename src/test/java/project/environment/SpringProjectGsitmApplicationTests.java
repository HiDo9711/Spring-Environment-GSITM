package project.environment;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.environment.Repository.BoardRepository;
import project.environment.Repository.UserRepository;
import project.environment.dto.UploadDTO;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.entity.Upload;
import project.environment.service.BoardService;
import project.environment.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
public class SpringProjectGsitmApplicationTests {

	 @Autowired
	   private UserService userService;

	   @Autowired
	   private UserRepository userRepository;

	   @Autowired
	   private BoardRepository boardRepository;

	   @Test
	   public void create100Boards() {

	      String[] regions = { "거제시", "고령군", "광주시", "단양군", "동두천시", "봉화군", "사천시", "양주시", "예천군", "완도군", "진도군", "청송군", "통영시",
	            "파주시", "함평군" };

	      for (int i = 1; i <= 10; i++) {
	         String loginId = "user" + i;
	         String name = "user" + i;
	         String email = "user" + i + "@gmail.com";
	         String password = "user" + i;
	         String regionNum = regions[(i - 1) % regions.length];

	         userService.createUser(loginId, name, email, password, regionNum);
	      }

	      Random random = new Random();

	      for (int i = 1; i <= 100; i++) {
	         Board board = new Board();
	         board.setTitle("우리동네 정수장 수질 현황 - " + i);
	         board.setBoardContent("저희 동네 정수장 수질 상태가 궁금해요 - " + i);
	         board.setCreateDate(LocalDateTime.now());
	         board.setModifyDate(null);
	         board.setHitCount((long) random.nextInt(10));
	         board.setNoticeFlag(false);

	         String[] imagePaths = { "water_1.jpg", "water_2.jpg", "water_3.jpg", "water_4.jpg", "water_5.jpg",
	               "water_6.jpg", "water_7.jpg" };

	         List<Upload> uploads = new ArrayList<>();
	         for (int imgIndex = 0; imgIndex < 6; imgIndex++) {
	            int randomImageIndex = random.nextInt(imagePaths.length);
	            Upload upload = new Upload();
	            upload.setFileName(imagePaths[randomImageIndex]);
	            upload.setImg(true);
	            upload.setBoard(board);
	            uploads.add(upload);
	         }

	         board.setUploads(uploads);

	         String userName = "user" + ((i - 1) % 10 + 1);
	         Optional<SiteUser> siteUser = userRepository.findByLoginId(userName);

	         siteUser.ifPresent(user -> board.setUser(user));

	         Set<SiteUser> recommenders = new HashSet<>();
	         int numRecommenders = random.nextInt(5) + 1;
	         List<SiteUser> allUsers = userRepository.findAll();
	         for (int j = 0; j < numRecommenders; j++) {
	            int randomIndex = random.nextInt(allUsers.size());
	            recommenders.add(allUsers.get(randomIndex));
	         }
	         board.setRecommender(recommenders);

	         boardRepository.save(board);
	      }
	   }


}
