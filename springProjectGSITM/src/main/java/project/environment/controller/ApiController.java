package project.environment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import project.environment.entity.ApiRequestParams;
import project.environment.entity.Region;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Controller
public class ApiController {
    
    // 지역 리스트를 하드코딩하여 사용
	private final List<Region> regions = Arrays.asList(
		    new Region("48310", "거제시"),
		    new Region("47830", "고령군"),
		    new Region("41610", "광주시"),
		    new Region("43800", "단양군"),
		    new Region("41250", "동두천시"),
		    new Region("47920", "봉화군"),
		    new Region("48240", "사천시"),
		    new Region("41630", "양주시"),
		    new Region("47900", "예천군"),
		    new Region("46890", "완도군"),
		    new Region("46900", "진도군"),
		    new Region("47750", "청송군"),
		    new Region("41480", "파주시"),
		    new Region("46890", "완도군"),
		    new Region("46860", "함평군")
		);

    @GetMapping("/main")
    public String showSelectForm(Model model) {
        model.addAttribute("regions", regions); // 지역 리스트를 모델에 추가
        model.addAttribute("requestParams", new ApiRequestParams()); // ApiRequestParams 객체를 모델에 추가
        return "select";
    }

    @PostMapping("/main")
    public String submitSelectForm(@ModelAttribute ApiRequestParams requestParams, Model model) {
        // 시작 날짜와 종료 날짜를 동일하게 설정
        requestParams.setEddt(requestParams.getStdt());

        // API 호출을 위한 URL 생성
        String apiUrl = "http://opendata.kwater.or.kr/openapi-data/service/pubd/waterinfos/waterquality/daywater/list?" +
                        "servicekey=0yknVZHJ5V4d1L6TO9qIKVxU5jF3hqmPaEt%2FfiGXoddQEfQwRC56sRqh0dmcpj6U8Jphw4kG2EE%2FilHisQc8tA%3D%3D" +
                        "&_type=json" +
                        "&numOfRows=1" +
                        "&pageNo=1" +
                        "&sgccd=" + requestParams.getSgcCd() +         
                        "&stdt=" + requestParams.getStdt() +
                        "&eddt=" + requestParams.getEddt(); // 시작날짜와 종료날짜 설정
        
        // REST 템플릿을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // JSON 파싱 및 필요한 데이터 추출
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode bodyNode = rootNode.path("response").path("body");
            JsonNode itemNode = bodyNode.path("items").path("item");

            // 데이터 추출
            String pH = itemNode.path("data4").asText();
            String turbidity = itemNode.path("data5").asText();
            String residualChlorine = itemNode.path("data6").asText();

            // 모델에 데이터 추가
            model.addAttribute("pH", pH);
            model.addAttribute("turbidity", turbidity);
            model.addAttribute("residualChlorine", residualChlorine);
            model.addAttribute("requestParams", requestParams);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        model.addAttribute("regions", regions); // 지역 리스트도 모델에 추가

        return "select"; // 다시 select.html로 돌아감
    }
}
