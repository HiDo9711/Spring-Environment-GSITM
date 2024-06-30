package project.environment.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ApiRequestParams {
    private String stdt = LocalDate.now().minusDays(7).toString(); // 시작 날짜
    private String eddt = LocalDate.now().minusDays(7).toString();
    private String sgcCd = "48310"; // 지역 코드
}
