package project.environment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {
	
    private String uuid;
    private String fileName;
    private boolean img;
}