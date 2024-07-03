package project.environment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Region {
    private String code;
    private String name;  
    
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}