package com.springGSITM;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Comment_Num;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY로 설정
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY로 설정
    @JoinColumn(name = "Id_Num", nullable = false)
    private Users user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String Comments_Content;

    @Column(nullable = false)
    private LocalDateTime Create_Date;

    @Column(nullable = false)
    private LocalDateTime Modify_Date;
}
