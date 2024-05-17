package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class UserSearchRequest {

    @Parameter(
        name = "page",
        description = "페이지 번호, 기본 첫 페이지(0)",
        example = "0",
        in = ParameterIn.QUERY)
    @PositiveOrZero(message = "페이지 번호는 0이상이어야 합니다.")
    private Integer page;

    @Parameter(
        name = "area",
        description = "활동 지역",
        example = "서울",
        in = ParameterIn.QUERY)
    private String area;

    @Parameter(
        name = "vocalRange",
        description = "음역대",
        example = "고음",
        in = ParameterIn.QUERY)
    private String vocalRange;

    @Parameter(
        name = "ageLowerBound",
        description = "검색 나이 하한",
        example = "20",
        in = ParameterIn.QUERY)
    @Range(min = 20, max = 40, message = "검색 나이는 20~40내 값만 가능합니다.")
    private Integer ageLowerBound;

    @Parameter(
        name = "ageUpperBound",
        description = "검색 나이 상한",
        example = "40",
        in = ParameterIn.QUERY)
    @Range(min = 20, max = 40, message = "검색 나이는 20~40내 값만 가능합니다.")
    private Integer ageUpperBound;

    @Parameter(
        name = "heightLowerBound",
        description = "검색 키 하한",
        example = "150",
        in = ParameterIn.QUERY)
    @Range(min = 130, max = 220, message = "검색 키는 130~220cm 내 값만 가능합니다.")
    private Integer heightLowerBound;

    @Parameter(
        name = "heightUpperBound",
        description = "검색 키 상한",
        example = "180",
        in = ParameterIn.QUERY)
    @Range(min = 130, max = 220, message = "검색 키는 130~220cm 내 값만 가능합니다.")
    private Integer heightUpperBound;

    @Parameter(
        name = "includePreferredAnimal",
        description = "검색 기준에 선호하는 동물상 포함",
        example = "true",
        in = ParameterIn.QUERY)
    private Boolean includePreferredAnimal;
}