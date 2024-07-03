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
    private Long commentNum;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String commentsContent;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = true)
    private LocalDateTime modifyDate;
    
    @ManyToOne
    private Board board;
    
    @ManyToMany
    private Set<SiteUser> recommender;
    
    @ManyToOne
	private SiteUser user;
    

}