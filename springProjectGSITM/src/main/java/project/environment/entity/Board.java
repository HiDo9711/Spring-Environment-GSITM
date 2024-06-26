package project.environment.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Board_Num;

    @Column(length = 200, nullable = false)
    private String Title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String Board_Content;

    @Column(nullable = false)
    private LocalDateTime Create_Date;

    @Column(nullable = true)
    private LocalDateTime Modify_Date;

    @Column(nullable = false)
    private Long Hit_Count;
    
    @Column(length = 150)
    private String Filename;
    
    @Column(length = 300)
    private String Filepath;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Comments> commentsList;
    
    @ManyToMany
    private Set<SiteUser> recommender;
    
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users user;
//
//    @ManyToOne
//    @JoinColumn(name = "region_id")
//    private Region region;

}