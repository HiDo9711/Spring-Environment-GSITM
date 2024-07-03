package project.environment.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;
import project.environment.service.UserService;

@Controller
@RequiredArgsConstructor
public class WriterPageController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentsService commentsService;

    @GetMapping("/board/writer/{userId}")
    public String myPage(Model model, Principal principal,@PathVariable("userId") String userId) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        SiteUser user = userService.getUser(userId);

        List<Board> boards = boardService.getBoardsByUser(user);

        List<Comments> comments = commentsService.getBoardsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("boards", boards);
        model.addAttribute("comments", comments);

        return "writerPage";
    }
}
