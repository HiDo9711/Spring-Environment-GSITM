package project.environment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ApiRequestParams {
    private String stdt; // 시작 날짜
    private String eddt; // 종료 날짜
    private String sgcCd; // 지역 코드
}
