package yeonba.be.support;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yeonba.be.exception.ExceptionAdvice;

@ExtendWith(MockitoExtension.class)
public abstract class ControllerTestSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    protected Object controller;

    protected Object initController() {

        return controller;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
            .setControllerAdvice(new ExceptionAdvice())
            .build();
    }

    /**
     * ApiResponse 형태의 200 OK 응답 공통 부분 검증
     * @param resultActions 테스트 요청 결과(응답)
     * @return 결과에 대한 추가 검증이 가능하도록 ResultActions 반환
     * @throws Exception 검증 실패시 예외 발생
     */
    protected ResultActions assertOk(ResultActions resultActions) throws Exception {

        return resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.message").isEmpty());
    }

    /**
     * ApiResponse 형태의 400 Bad Request 공통 부분 검증
     * @param resultActions 테스트 요청 결과(응답)
     * @return 결과에 대한 추가 검증이 가능하도록 ResultActions 반환
     * @throws Exception 검증 실패시 예외 발생
     */
    protected ResultActions assertBadRequest(ResultActions resultActions) throws Exception {

        return resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("fail"))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }
}
