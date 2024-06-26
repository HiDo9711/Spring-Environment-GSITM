package project.environment.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.service.BoardService;
import project.environment.service.UserService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

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
    public String postWriteForm(Board board,Model model, @RequestParam(name="file", required=false) MultipartFile file) throws Exception{
        boardService.create(board,file);
 
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
    
    @GetMapping("/recommend/{id}")
    public String boardRecommend(Principal principal, @PathVariable("id") Integer id) {
                // 파라미터로 받은 id를 조회하여 질문을 저장
    	        Board board = this.boardService.getBoardById(id);
                // 현재 객체의 id를 조회하여 사용자를 저장
    	        SiteUser siteUser = this.userService.getUser(principal.getName());
                // 서비스의 vote 메소드 사용
    	        this.boardService.recommend(board, siteUser);
                // id페이지로 리다이렉트
    	        return String.format("redirect:/board/read/%s", id);
    }
    
    
}