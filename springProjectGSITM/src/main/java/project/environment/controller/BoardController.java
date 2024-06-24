package project.environment.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.service.BoardService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
    	Page<Board> paging = this.boardService.getList(page);
    	model.addAttribute("paging", paging);
        return "boardList";
    }
    
    @GetMapping("/write")
    public String getWriteForm(Model model) {
        model.addAttribute("board", new Board());
        return "boardWrite";
    }
    
    @PostMapping("/write")
    public String postWriteForm(Board board) {
        boardService.create(board);
        return "redirect:/board/list";
    }
    
    // 게시글 수정 폼으로 이동
    @GetMapping("/edit")
    public String editBoardForm(@RequestParam("id") Integer id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "boardEdit";
    }

    // 게시글 수정 처리
    @PostMapping("/edit")
    public String editBoard(@RequestParam("id") Integer id, 
                            @RequestParam("title") String title, 
                            @RequestParam("boardContent") String boardContent,
                            RedirectAttributes redirectAttributes) {
        boardService.edit(id, title, boardContent);
        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/board/list";
    }
    
    @GetMapping("/read/{id}")
    public String readBoard(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardAndIncreaseHitCount(id); // 수정된 메서드 사용
        model.addAttribute("board", board);
        return "boardRead";
    }
    
    
    @PostMapping("/delete")
    public String deleteBoard(@RequestParam("id") Integer id) {
        boardService.delete(id);
        return "redirect:/board/list";
    }
}