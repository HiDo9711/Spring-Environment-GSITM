package project.environment.entity;


import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
//import jakarta.persistence.JoinColumn;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String Comments_Content;

    @Column(nullable = false)
    private LocalDateTime Create_Date;

    @Column(nullable = true)
    private LocalDateTime Modify_Date;
    
    @ManyToOne
    private Board board;
    
    @ManyToMany
    private Set<SiteUser> recommender;
    
//    @ManyToOne
//    @JoinColumn(name = "Id_Num", nullable = false)
//    private Users user;



}