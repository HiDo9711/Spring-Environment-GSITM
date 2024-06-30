package project.environment.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.environment.Repository.BoardRepository;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.form.BoardForm;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        return this.boardRepository.findAll(pageable);
    }
    
    public Board create(String title, String boardContent, SiteUser user, @RequestParam(name="file", required=false) MultipartFile file,@RequestParam(name = "noticeFlag", defaultValue = "false") boolean noticeFlag) throws Exception {
    	Board board = new Board();
    	
    	String projectPath = System.getProperty("user.dir") // 프로젝트 경로를 가져옴
                + "\\src\\main\\resources\\static\\files";  // 파일이 저장될 폴더의 경로
    	
    	UUID uuid = UUID.randomUUID();
    	
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	File saveFile = new File(projectPath, fileName);
    	
    	file.transferTo(saveFile);
    	
    	board.setFileName(fileName);
        board.setFilePath("/files/" + fileName);
        board.setCreateDate(LocalDateTime.now());
        board.setTitle(title);
		board.setBoardContent(boardContent);
		board.setUser(user);
		board.setNoticeFlag(noticeFlag);
        board.setModifyDate(null); // 초기 생성 시 수정 일자는 null로 설정
        board.setHitCount(0L); // 새로 작성된 글의 조회수 초기화

        return boardRepository.save(board);
    }
    
    public Board getBoardById(Integer id) {
        return boardRepository.findById(id)
                              .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }
     
    @Transactional
    public Board getBoardAndIncreaseHitCount(Integer id) {
        Board board = boardRepository.findById(id)
                                      .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        Long currentHitCount = board.getHitCount();
        board.setHitCount(currentHitCount + 1); // 조회수 증가
        return boardRepository.save(board); // 수정된 엔티티 반환
    }

    @Transactional
    public Board edit(Integer id, String title, String boardContent) {
        Board board = boardRepository.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        board.setTitle(title);
        board.setBoardContent(boardContent);
        board.setModifyDate(LocalDateTime.now());
        return boardRepository.save(board);
    }

    @Transactional
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }
    
    @Transactional
    public void recommend(Board board, SiteUser siteUser) {
        // 현재 추천 리스트를 가져옴
        Set<SiteUser> recommenders = board.getRecommender();

        // 이미 추천한 사용자인지 확인
        if (recommenders.contains(siteUser)) {
            // 이미 추천한 경우 추천 취소 (리스트에서 제거)
            recommenders.remove(siteUser);
        } else {
            // 추천 리스트에 추가
            recommenders.add(siteUser);
        }

        // 변경된 보드 엔티티를 저장
        this.boardRepository.save(board);
    }
    
    @Transactional
    public List<Board> getBoardsByUser(SiteUser user) {
        return boardRepository.findByUser(user);
    }
    
    
}