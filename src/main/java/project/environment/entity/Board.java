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
    private Integer boardNum;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String boardContent;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = true)
    private LocalDateTime modifyDate;

    @Column(nullable = false)
    private Long hitCount;
    
    @Column(length = 150)
    private String fileName;
    
    @Column(length = 300)
    private String filePath;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Comments> commentsList;
    
    @ManyToOne
    private SiteUser user;
    
    @Column(nullable = true)
    private boolean noticeFlag;
    
    // 추천 기능을 위한 매칭
    @ManyToMany
    private Set<SiteUser> recommender;

//    @ManyToOne
//    @JoinColumn(name = "region_id")
//    private Region region;

}