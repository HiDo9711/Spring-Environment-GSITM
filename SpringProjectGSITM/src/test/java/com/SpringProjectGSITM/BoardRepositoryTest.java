package com.SpringProjectGSITM;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testSaveAndGetBoard() {
        // Given
        Board board = new Board();
        board.setTitle("Test Title");
        board.setBoardContent("Test Content");
        board.setCreateDate(LocalDateTime.now());
        board.setModifyDate(LocalDateTime.now());
        board.setHitCount(0L);

        // When
        Board savedBoard = boardRepository.save(board);
        List<Board> boards = boardRepository.findAll();

        // Then
        assertNotNull(savedBoard.getBoardNum()); // 저장된 게시물은 ID가 있어야 함
        assertEquals(1, boards.size()); // 저장된 게시물은 1개여야 함

        Board retrievedBoard = boards.get(0);
        assertEquals("Test Title", retrievedBoard.getTitle());
        assertEquals("Test Content", retrievedBoard.getBoardContent());
        assertNotNull(retrievedBoard.getCreateDate());
        assertNotNull(retrievedBoard.getModifyDate());
        assertEquals(0L, retrievedBoard.getHitCount());
    }
}
