package com.SpringProjectGSITM;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/list")
    public String getAllBoards(Model model) {
        List<Board> boards = boardService.getList();
        model.addAttribute("boards", boards);
        return "boardList";
    }
    
    @GetMapping("/board/write")
    public String getWriteForm(Model model) {
        model.addAttribute("board", new Board());
        return "boardWrite";
    }
    
    @PostMapping("/board/write")
    public String postWriteForm(Board board) {
        boardService.save(board);
        return "redirect:/board/list";
    }
    
    // 게시글 수정 폼으로 이동
    @GetMapping("/board/edit")
    public String editBoardForm(@RequestParam("id") Integer id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "boardEdit";
    }

    // 게시글 수정 처리
    @PostMapping("/board/edit")
    public String editBoard(@RequestParam("id") Integer id, 
                            @RequestParam("title") String title, 
                            @RequestParam("boardContent") String boardContent,
                            RedirectAttributes redirectAttributes) {
        boardService.editBoard(id, title, boardContent);
        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/board/list";
    }
    
    @GetMapping("/board/read/{id}")
    public String readBoard(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "boardRead";
    }
    
    @PostMapping("/board/delete")
    public String deleteBoard(@RequestParam("id") Integer id) {
        boardService.deleteBoard(id);
        return "redirect:/board/list";
    }
}