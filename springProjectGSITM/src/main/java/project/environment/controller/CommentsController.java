package project.environment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.service.BoardService;
import project.environment.service.CommentsService;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentsController {
	private final BoardService boardservice;
	private final CommentsService commentsService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
		Board board = this.boardservice.getBoardById(id);
		this.commentsService.create(board, content);
		return String.format("redirect:/board/read/%s", id);
	}
	
    // 댓글 수정 폼 보여주기
    @GetMapping("/edit/{commentNum}")
    public String showEditForm(Model model, @PathVariable("commentNum") Long commentNum) {
        Comments comment = commentsService.getCommentById(commentNum);
        if (comment == null) {
            // 댓글이 존재하지 않으면 예외 처리
            return "redirect:/error/404";
        }
        model.addAttribute("comment", comment); // 댓글 정보를 모델에 추가하여 수정 폼에서 사용할 수 있도록 함
        return "edit-comment"; // 수정 폼을 보여주는 Thymeleaf 템플릿 이름
    }

 // 댓글 수정 처리
    @PostMapping("/edit/{commentNum}")
    public String editComment(Model model, @PathVariable("commentNum") Long commentNum, @RequestParam("content") String content) {
        Comments comment = commentsService.getCommentById(commentNum);
        if (comment == null) {
            return "redirect:/error/404";
        }
        comment.setComments_Content(content);
        commentsService.update(comment); // 수정 메서드 호출
        return String.format("redirect:/board/read/%d", comment.getBoard().getBoard_Num());
    }
    
 // 댓글 삭제 처리
    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        Comments comment = commentsService.getCommentById(commentId);
        if (comment != null) {
            Integer boardId = comment.getBoard().getBoard_Num();
            commentsService.delete(commentId);
            return String.format("redirect:/board/read/%d", boardId);
        }
        return "redirect:/board/list";
    }
}
