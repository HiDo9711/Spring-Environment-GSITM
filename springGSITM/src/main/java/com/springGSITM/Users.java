package com.springGSITM;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_Num;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY로 설정
    @JoinColumn(name = "Region_Num", nullable = false)
    private Region region;

    @Column(nullable = false)
    private String Id;

    @Column(nullable = false)
    private String Email;

    @Column(nullable = false)
    private String Password;

    @Column(nullable = false)
    private String Name;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comments> commentsList;
}
