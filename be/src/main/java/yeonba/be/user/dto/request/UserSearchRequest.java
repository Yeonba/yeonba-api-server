package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class UserSearchRequest {

    @Schema(
        type = "number",
        description = "페이지 번호, 기본 첫 페이지(0)",
        example = "0")
    @PositiveOrZero(message = "페이지 번호는 0이상이어야 합니다.")
    private Integer page;

    @Schema(
        type = "string",
        description = "검색 지역",
        example = "서울")
    private String area;

    @Schema(
        type = "string",
        description = "검색 음역대",
        example = "고음")
    private String vocalRange;

    @Schema(
        type = "number",
        description = "검색 나이 하한",
        example = "20")
    @Range(min = 20, max = 40, message = "검색 나이는 20~40내 값만 가능합니다.")
    private Integer ageLowerBound;

    @Schema(
        type = "number",
        description = "검색 나이 상한",
        example = "30")
    @Range(min = 20, max = 40, message = "검색 나이는 20~40내 값만 가능합니다.")
    private Integer ageUpperBound;

    @Schema(
        type = "number",
        description = "검색 키 하한",
        example = "160")
    @Range(min = 130, max = 220, message = "검색 키는 130~220cm 내 값만 가능합니다.")
    private Integer heightLowerBound;

    @Schema(
        type = "number",
        description = "검색 키 상한",
        example = "170")
    @Range(min = 130, max = 220, message = "검색 키는 130~220cm 내 값만 가능합니다.")
    private Integer heightUpperBound;

    @Schema(
        type = "boolean",
        description = "검색 기준에 선호하는 동물상 포함 여부",
        example = "true")
    private Boolean includePreferredAnimal;
}
