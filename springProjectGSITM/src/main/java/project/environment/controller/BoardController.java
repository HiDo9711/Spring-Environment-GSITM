package project.environment.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.form.BoardForm;
import project.environment.service.BoardService;
import project.environment.service.UserService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    // 모든 권한 접근
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
    	Page<Board> paging = this.boardService.getList(page);
    	model.addAttribute("paging", paging);
        return "boardList";
    }
    
    // 모든 권한 접근
    @GetMapping("/read/{id}")
    public String readBoard(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardAndIncreaseHitCount(id); // 수정된 메서드 사용
        model.addAttribute("board", board);
        return "boardRead";
    }
    
    // 작성시 boardform 을 통해 html로 전달하도록 설정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWriteForm(BoardForm boardForm) {
        return "boardWrite";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWriteForm(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal, @RequestParam(name="file", required=false) MultipartFile file, @RequestParam(name = "noticeFlag", defaultValue = "false") boolean noticeFlag) throws Exception {
        if (bindingResult.hasErrors()) {
            return "board/write";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
		this.boardService.create(boardForm.getTitle(), boardForm.getBoard_Content(), siteUser, file, noticeFlag);
        return "redirect:/board/list";
    }

    // 게시글 수정 폼 - get
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{id}")
    public String editBoardForm(BoardForm boardForm, @PathVariable("id") Integer id, Principal principal, Model model) {
        Board board = boardService.getBoardById(id);
        if (!board.getUser().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardForm.setTitle(board.getTitle());
        boardForm.setBoard_Content(board.getBoard_Content());
        model.addAttribute("boardForm", boardForm);
        return "boardEdit";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{id}")
    public String editBoard(@Valid BoardForm boardForm, BindingResult bindingResult, 
    		Principal principal, RedirectAttributes redirectAttributes, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "/board/edit/{id}";
        }
        Board board = boardService.getBoardById(id);
        if (!board.getUser().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        board.setTitle(boardForm.getTitle());
        board.setBoard_Content(boardForm.getBoard_Content());
        boardService.edit(id, board.getTitle(), board.getBoard_Content());
        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/board/list";
    }

//	  // 관리자가 공지유무인 noticeFlag를 변경할 수 있도록 하는 edit코드 -> 수정 필요 
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/edit/{id}")
//    public String editBoard(@Valid BoardForm boardForm, BindingResult bindingResult, 
//    		Principal principal, RedirectAttributes redirectAttributes, 
//          @PathVariable("id") Integer id) {
//        if (bindingResult.hasErrors()) {
//            return "/board/edit/{id}";
//        }
//        Board board = boardService.getBoardById(id);
//        if (!board.getUser().getName().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        board.setTitle(boardForm.getTitle());
//        board.setBoard_Content(boardForm.getBoard_Content());
//        boardService.edit(id, board.getTitle(), board.getBoard_Content());
//        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
//        return "redirect:/board/list";
//    }
    

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String deleteBoard(Principal principal, @PathVariable("id") Integer id) {
    	Board board = this.boardService.getBoardById(id);
		if (!board.getUser().getName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.boardService.delete(board.getBoard_Num());
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