package project.environment.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

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

}
