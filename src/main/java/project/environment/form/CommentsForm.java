package project.environment.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsForm {

	@NotEmpty(message = "내용은 필수 항목입니다.")
	private String commentsContent;
}
