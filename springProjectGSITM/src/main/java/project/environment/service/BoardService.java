package project.environment.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

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

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.boardRepository.findAll(pageable);
    }
    
    public Board create(Board board, @RequestParam(name="file", required=false) MultipartFile file) throws Exception {
    	
    	String projectPath = System.getProperty("user.dir") // 프로젝트 경로를 가져옴
                + "\\src\\main\\resources\\static\\files";  // 파일이 저장될 폴더의 경로
    	
    	UUID uuid = UUID.randomUUID();
    	
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	File saveFile = new File(projectPath, fileName);
    	
    	file.transferTo(saveFile);
    	
    	board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
        board.setCreate_Date(LocalDateTime.now());
        board.setModify_Date(null); // 초기 생성 시 수정 일자는 null로 설정
        board.setHit_Count(0L); // 새로 작성된 글의 조회수 초기화

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
        Long currentHitCount = board.getHit_Count();
        board.setHit_Count(currentHitCount + 1); // 조회수 증가
        return boardRepository.save(board); // 수정된 엔티티 반환
    }


    @Transactional
    public void edit(Integer id, String title, String boardContent) {
        Board board = boardRepository.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        board.setTitle(title);
        board.setBoard_Content(boardContent);
        board.setModify_Date(LocalDateTime.now());
        // 게시글 수정 시 수정일자 업데이트
    }

    
    @Transactional
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }
    
    
    
    
}