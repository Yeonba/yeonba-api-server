package yeonba.be.support;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yeonba.be.config.AuthInterceptor;
import yeonba.be.exception.ExceptionAdvice;

@ActiveProfiles("test")
public abstract class ControllerTestSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    protected AuthInterceptor authInterceptor;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller())
            .setControllerAdvice(new ExceptionAdvice())
            .alwaysDo(print())
            .build();
    }

    protected abstract Object controller();
}
