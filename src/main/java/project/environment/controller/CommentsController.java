package project.environment.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;
import project.environment.form.CommentsForm;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;
import project.environment.service.UserService;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentsController {
    private final BoardService boardservice;
    private final CommentsService commentsService;
    private final UserService userservice;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid CommentsForm commentsForm,
                               BindingResult bindingResult, Principal principal) {
        Board board = this.boardservice.getBoardById(id);
        SiteUser siteUser = this.userservice.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board);
            return "boardRead";
        }
        this.commentsService.create(board, commentsForm.getCommentsContent(), siteUser);
        return String.format("redirect:/board/read/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{commentNum}")
    public String showEditForm(CommentsForm commentsForm, @PathVariable("commentNum") Long commentNum,
                               Principal principal, RedirectAttributes redirectAttributes) {
        Comments comment = commentsService.getComments(commentNum);
        if (!comment.getUser().getLoginId().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "수정 권한이 없습니다.");
            return String.format("redirect:/board/read/%s#comment_%s", comment.getBoard().getBoardNum(), comment.getCommentNum());
        }
        commentsForm.setCommentsContent(comment.getCommentsContent());
        return "boardRead";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{commentNum}")
    public String editComment(@PathVariable("commentNum") Long commentNum, @Valid CommentsForm commentsForm,
                              BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {
        Comments comment = commentsService.getComments(commentNum);
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/comments/edit/%s", commentNum);
        }
        if (!comment.getUser().getLoginId().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "수정 권한이 없습니다.");
            return String.format("redirect:/board/read/%s#comment_%s", comment.getBoard().getBoardNum(), comment.getCommentNum());
        }
        this.commentsService.modify(comment, commentsForm.getCommentsContent());
        return String.format("redirect:/board/read/%s#comment_%s", comment.getBoard().getBoardNum(), comment.getCommentNum());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{commentId}")
    public String deleteComment(Principal principal, @PathVariable("commentId") Long commentId, RedirectAttributes redirectAttributes) {
        Comments comment = commentsService.getComments(commentId);
        if (!comment.getUser().getLoginId().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "삭제 권한이 없습니다.");
            return String.format("redirect:/board/read/%s#comment_%s", comment.getBoard().getBoardNum(), comment.getCommentNum());
        }
        this.commentsService.delete(comment);
        return String.format("redirect:/board/read/%s", comment.getBoard().getBoardNum());
    }
}