package com.springGSITM;

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
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Plant_Num;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY로 설정
    @JoinColumn(name = "Region_Num", nullable = false)
    private Region region;

    @Column(nullable = false)
    private String Plant_Name;
}
