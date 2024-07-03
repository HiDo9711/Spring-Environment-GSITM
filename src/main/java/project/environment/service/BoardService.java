package project.environment.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import project.environment.Repository.BoardRepository;
import project.environment.dto.UploadDTO;
import project.environment.entity.Board;
import project.environment.entity.SiteUser;
import project.environment.entity.Upload;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UploadService uploadService;

    public Page<Board> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        return this.boardRepository.findAll(pageable);
    }
    

    public Board getBoardAndIncreaseHitCount(Integer id) {
        Board board = boardRepository.findById(id)
                                      .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        Long currentHitCount = board.getHitCount();
        board.setHitCount(currentHitCount + 1); 
        return boardRepository.save(board); 
    }
    
    public void create(String title, String boardContent, SiteUser user, boolean noticeFlag, List<UploadDTO> uploadDTOs) {
        Board board = new Board();
        board.setTitle(title);
        board.setBoardContent(boardContent);
        board.setUser(user);
        board.setNoticeFlag(noticeFlag);
        board.setHitCount(0L);
        board.setCreateDate(LocalDateTime.now());

        List<Upload> uploads = uploadDTOs.stream()
                                         .map(uploadDTO -> {
                                             Upload upload = Upload.fromDTO(uploadDTO);
                                             upload.setBoard(board);
                                             return upload;
                                         })
                                         .collect(Collectors.toList());
        board.setUploads(uploads);

        boardRepository.save(board);
    }
    
    
    public Board getBoardById(Integer id) {
        return boardRepository.findById(id)
                              .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }
     

    public Board edit(Integer id, String title, String boardContent, List<UploadDTO> newUploadDTOs) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        board.setTitle(title);
        board.setBoardContent(boardContent);
        board.setModifyDate(LocalDateTime.now());

        if (newUploadDTOs != null && !newUploadDTOs.isEmpty()) {

            List<String> existingFileNames = board.getUploads().stream()
                    .map(Upload::getFileName)
                    .collect(Collectors.toList());

            List<Upload> newUploads = newUploadDTOs.stream()
                    .map(uploadDTO -> {
                        Upload upload = new Upload();
                        upload.setUuid(uploadDTO.getUuid());
                        upload.setFileName(uploadDTO.getFileName());
                        upload.setImg(uploadDTO.isImg());
                        upload.setBoard(board);
                        return upload;
                    })
                    .collect(Collectors.toList());
            board.setUploads(newUploads);

            existingFileNames.forEach(fileName -> {
                try {
                    uploadService.removeImage(fileName);
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            });
        }

        return boardRepository.save(board);
    }

    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }
    

    public void recommend(Board board, SiteUser siteUser) {
        Set<SiteUser> recommenders = board.getRecommender();
        if (recommenders.contains(siteUser)) {
            recommenders.remove(siteUser);
        } else {
            recommenders.add(siteUser);
        }
        this.boardRepository.save(board);
    }

    public List<Board> getBoardsByUser(SiteUser user) {
        return boardRepository.findByUser(user);
    }
    

    public List<Board> findTop3RecommendedBoards() {
    	return boardRepository.findTop3Recommender();
    }

    public List<Object[]> findTop3ActiveRegions() {
        return boardRepository.findTop3Regions();
    }
    
    
}