package com.springGSITM;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RegionNum;

    private String RegionName;

    @OneToMany(mappedBy = "region", cascade = CascadeType.REMOVE)
    private List<Board> boardList;

    @OneToMany(mappedBy = "region", cascade = CascadeType.REMOVE)
    private List<Users> userList;

    @OneToMany(mappedBy = "region", cascade = CascadeType.REMOVE)
    private List<Plant> plantList;

}