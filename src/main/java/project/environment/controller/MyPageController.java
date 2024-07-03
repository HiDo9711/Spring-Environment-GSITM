package project.environment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;
import project.environment.service.UserService;

@RequiredArgsConstructor
@Controller
public class MyPageController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentsService commentsService;

    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        String loginId = principal.getName();
        SiteUser user = userService.getUser(loginId);
        
        List<Board> boards = boardService.getBoardsByUser(user);

        List<Comments> comments = commentsService.getBoardsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("boards", boards);
        model.addAttribute("comments", comments);

        return "mypage"; 
    }
}
