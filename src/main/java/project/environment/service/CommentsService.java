package project.environment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.environment.DataNotFoundException;
import project.environment.Repository.CommentsRepository;
import project.environment.entity.Board;
import project.environment.entity.Comments;
import project.environment.entity.SiteUser;

@RequiredArgsConstructor
@Service
public class CommentsService {

	private final CommentsRepository commentsRepository;

	public void create(Board board, String content, SiteUser user) {
		Comments comments = new Comments();
		comments.setCommentsContent(content);
		comments.setCreateDate(LocalDateTime.now());
		comments.setModifyDate(null);
		comments.setBoard(board);
		comments.setUser(user);
		this.commentsRepository.save(comments);
	}

	// 댓글 수정
	@Transactional
	public Comments getComments(Long Comment_Num) {
		Optional<Comments> comments = this.commentsRepository.findById(Comment_Num);
		if (comments.isPresent()) {
			return comments.get();
		} else {
			throw new DataNotFoundException("answer not found");
		}
	}

	public void modify(Comments comments, String content) {
		comments.setCommentsContent(content);
		comments.setCreateDate(LocalDateTime.now());
		this.commentsRepository.save(comments);
	}

	public void delete(Comments comments) {
		this.commentsRepository.delete(comments);
	}
	
	@Transactional
	public List<Comments> getBoardsByUser(SiteUser user) {
		return commentsRepository.findByUser(user);
	}
}
