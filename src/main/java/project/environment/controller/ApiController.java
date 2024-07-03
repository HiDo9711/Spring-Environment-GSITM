package project.environment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import project.environment.entity.ApiRequestParams;
import project.environment.entity.Board;
import project.environment.entity.Region;
import project.environment.service.BoardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Controller
@RequiredArgsConstructor
public class ApiController {

	 @Value("${api.key}")
	    private String apiKey;
	 
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
		    new Region("48220", "통영시"),
		    new Region("41480", "파주시"),
		    new Region("46860", "함평군")
		);
	
	@Autowired
	private BoardService boardService;

    @GetMapping("/main")
    public String showSelectForm(Model model) {
        model.addAttribute("regions", regions);
        model.addAttribute("requestParams", new ApiRequestParams()); 
        
        List<Board> top3RecommendedBoards = boardService.findTop3RecommendedBoards();
        model.addAttribute("top3RecommendedBoards", top3RecommendedBoards);
        List<Object[]> top3ActiveRegions = boardService.findTop3ActiveRegions();
        List<String> activeRegions = new ArrayList<>();
        List<Long> numPosts = new ArrayList<>();

        for (Object[] result : top3ActiveRegions) {
            String regiona = (String) result[0];
            Long counta = (Long) result[1];
            activeRegions.add(regiona);
            numPosts.add(counta);
        }

        model.addAttribute("activeRegions", activeRegions);
        model.addAttribute("numPosts", numPosts);
        
        return "select";
    }

    @PostMapping("/main")
    public String submitSelectForm(@ModelAttribute ApiRequestParams requestParams, Model model) {
    	System.out.println("post");
        requestParams.setEddt(requestParams.getStdt());

        String apiUrl = "http://opendata.kwater.or.kr/openapi-data/service/pubd/waterinfos/waterquality/daywater/list?" +
                        "servicekey=" + apiKey +
                        "&_type=json" +
                        "&numOfRows=1" +
                        "&pageNo=1" +
                        "&sgccd=" + requestParams.getSgcCd() +         
                        "&stdt=" + requestParams.getStdt() +
                        "&eddt=" + requestParams.getEddt();
        
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode bodyNode = rootNode.path("response").path("body");
            JsonNode itemNode = bodyNode.path("items").path("item");

            String pH = itemNode.path("data4").asText();
            String turbidity = itemNode.path("data5").asText();
            String residualChlorine = itemNode.path("data6").asText();

            model.addAttribute("pH", pH);
            model.addAttribute("turbidity", turbidity);
            model.addAttribute("residualChlorine", residualChlorine);
            model.addAttribute("requestParams", requestParams);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        model.addAttribute("regions", regions); 
        
        List<Board> top3RecommendedBoards = boardService.findTop3RecommendedBoards();
        model.addAttribute("top3RecommendedBoards", top3RecommendedBoards);

        List<Object[]> top3ActiveRegions = boardService.findTop3ActiveRegions();
        List<String> activeRegions = new ArrayList<>();
        List<Long> numPosts = new ArrayList<>();

        for (Object[] result : top3ActiveRegions) {
            String regiona = (String) result[0];
            Long counta = (Long) result[1];
            activeRegions.add(regiona);
            numPosts.add(counta);
        }

        model.addAttribute("activeRegions", activeRegions);
        model.addAttribute("numPosts", numPosts);

        return "select";
    }
}
