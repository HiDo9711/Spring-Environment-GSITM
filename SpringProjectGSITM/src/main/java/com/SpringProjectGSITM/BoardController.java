package com.SpringProjectGSITM;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "boardList"; // boardList.html로 이동
    }
}