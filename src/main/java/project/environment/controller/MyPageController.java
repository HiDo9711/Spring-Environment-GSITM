// MyPageController.java
package project.environment.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;
import project.environment.service.UserService;

@Controller
public class MyPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String loginId = principal.getName(); // 현재 로그인한 사용자의 ID 가져오기
        SiteUser user = userService.getUser(loginId);
        
        // 사용자가 작성한 게시글 목록 가져오기
        List<Board> boards = boardService.getBoardsByUser(user);

        // 사용자가 작성한 댓글 목록 가져오기
        List<Comments> comments = commentsService.getBoardsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("boards", boards);
        model.addAttribute("comments", comments);

        return "mypage"; // 마이페이지 템플릿의 이름
    }
}
