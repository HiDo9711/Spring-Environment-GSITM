package project.environment.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.environment.Repository.CommentsRepository;
import project.environment.entity.Board;
import project.environment.entity.Comments;

@RequiredArgsConstructor
@Service
public class CommentsService {
	
	private final CommentsRepository commentsRepository;
	
	public void create(Board board,String content) {
		Comments comments = new Comments();
		comments.setComments_Content(content);
		comments.setCreate_Date(LocalDateTime.now());
		comments.setModify_Date(null);
		comments.setBoard(board);
		this.commentsRepository.save(comments);
	}
	
    // 댓글 수정
	@Transactional
	public void update(Comments comments) {
	    comments.setModify_Date(LocalDateTime.now()); // 수정 시간 설정
	    commentsRepository.save(comments); // 변경 사항 저장
	}
    
    // 댓글 번호로 댓글 조회
    public Comments getCommentById(Long commentId) {
        return commentsRepository.findById(commentId).orElse(null);
    }
    
    // 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        this.commentsRepository.deleteById(commentId);
    }
	

	

}
