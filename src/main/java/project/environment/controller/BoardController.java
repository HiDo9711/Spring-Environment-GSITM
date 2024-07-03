package project.environment.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.environment.dto.UploadDTO;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.form.BoardForm;
import project.environment.service.BoardService;
import project.environment.service.UploadService;
import project.environment.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final UploadService uploadService;
    
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
    	Page<Board> paging = this.boardService.getList(page);
    	model.addAttribute("paging", paging);
        return "boardList";
    }

    @GetMapping("/read/{id}")
    public String readBoard(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardAndIncreaseHitCount(id); 
        model.addAttribute("board", board);
        return "boardRead";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String getWriteForm(BoardForm boardForm) {
        return "boardWrite";
    }

    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWriteForm(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal,
                                @RequestParam(name = "noticeFlag", defaultValue = "false") boolean noticeFlag,
                                @RequestParam("files") MultipartFile[] files,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "boardWrite";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());

        try {
            List<UploadDTO> uploadDTOs = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    UploadDTO uploadDTO = this.uploadService.uploads(file);
                    uploadDTOs.add(uploadDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생하였습니다.");
                    return "redirect:/board/list";
                }
            }

            boardService.create(boardForm.getTitle(), boardForm.getBoardContent(), siteUser, noticeFlag, uploadDTOs);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 작성 중 오류가 발생하였습니다.");
            return "redirect:/board/list";
        }

        return "redirect:/board/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{id}")
    public String editBoardForm(BoardForm boardForm, @PathVariable("id") Integer id, Principal principal, Model model, 
    		RedirectAttributes redirectAttributes) {
        Board board = boardService.getBoardById(id);
        if (!board.getUser().getName().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "수정 권한이 없습니다.");
            return String.format("redirect:/board/read/%d", id);
        }
        boardForm.setTitle(board.getTitle());
        boardForm.setBoardContent(board.getBoardContent());
        model.addAttribute("boardForm", boardForm);
        model.addAttribute("board", board);
        return "boardEdit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{id}")
    public String editBoard(@Valid BoardForm boardForm, BindingResult bindingResult,
                            Principal principal, RedirectAttributes redirectAttributes, 
                            @PathVariable("id") Integer id,
                            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        if (bindingResult.hasErrors()) {
            return "boardEdit";
        }
        
        Board board = boardService.getBoardById(id);
        if (!board.getUser().getName().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "수정 권한이 없습니다.");
            return String.format("redirect:/board/read/%d", id);
        }

        board.setTitle(boardForm.getTitle());
        board.setBoardContent(boardForm.getBoardContent());
        board.setModifyDate(LocalDateTime.now());

        try {
            if (files != null && files.length > 0) {
                List<UploadDTO> uploadDTOs = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        UploadDTO uploadDTO = uploadService.updateUploads(file, file.getOriginalFilename());
                        uploadDTOs.add(uploadDTO);
                    }
                }
                boardService.edit(id, boardForm.getTitle(), boardForm.getBoardContent(), uploadDTOs);
            } else {
                boardService.edit(id, boardForm.getTitle(), boardForm.getBoardContent(), Collections.emptyList());
            }

            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생하였습니다.");
            return String.format("redirect:/board/edit/%d", id);
        }

        return String.format("redirect:/board/read/%d", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String deleteBoard(Principal principal, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Board board = boardService.getBoardById(id);
        if (!board.getUser().getName().equals(principal.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "삭제 권한이 없습니다.");
            return String.format("redirect:/board/read/%d", id);
        }
        boardService.delete(board.getBoardNum());
        return "redirect:/board/list";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recommend/{id}")
    public String boardRecommend(Principal principal, @PathVariable("id") Integer id) {
    	        Board board = this.boardService.getBoardById(id);
    	        SiteUser siteUser = this.userService.getUser(principal.getName());
    	        this.boardService.recommend(board, siteUser);
    	        return String.format("redirect:/board/read/%s", id);
    }

    

    
}