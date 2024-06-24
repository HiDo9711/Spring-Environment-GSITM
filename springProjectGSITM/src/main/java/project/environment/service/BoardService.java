package project.environment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.environment.Repository.BoardRepository;
import project.environment.entity.Board;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getList() {
        return boardRepository.findAll();
    }
    
    public Board save(Board board) {
        board.setCreate_Date(LocalDateTime.now());
        board.setModify_Date(LocalDateTime.now());
        board.setHit_Count(0L); // 새로 작성된 글의 조회수 초기화
        return boardRepository.save(board);
    }
    
    public Board getBoardById(Integer id) {
        return boardRepository.findById(id)
                              .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }

    @Transactional
    public void editBoard(Integer id, String title, String boardContent) {
        Board board = boardRepository.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        board.setTitle(title);
        board.setBoard_Content(boardContent);
        // 수정 날짜 업데이트 등 필요한 로직 추가 가능
    }
    
    @Transactional
    public void deleteBoard(Integer id) {
        boardRepository.deleteById(id);
    }
}