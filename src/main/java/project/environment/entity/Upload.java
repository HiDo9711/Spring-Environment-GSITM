package project.environment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.environment.dto.UploadDTO;

@Entity
@Getter
@Setter
@ToString
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String uuid;

    @Column
    private String fileName;

    @Column
    private boolean img;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public static Upload fromDTO(UploadDTO uploadDTO) {
        Upload upload = new Upload();
        upload.setFileName(uploadDTO.getFileName());
        upload.setUuid(uploadDTO.getUuid());
        upload.setImg(uploadDTO.isImg());
        return upload;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}