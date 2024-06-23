package com.SpringProjectGSITM;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardNum;  // 변수명을 카멜 케이스로 변경

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String boardContent;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime modifyDate;

    @Column(nullable = false)
    private Long hitCount;
}
