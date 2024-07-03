package project.environment.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRequestParams {
    private String stdt = LocalDate.now().minusDays(1).toString();
    private String eddt = LocalDate.now().minusDays(1).toString();
    private String sgcCd = "48310";
}
