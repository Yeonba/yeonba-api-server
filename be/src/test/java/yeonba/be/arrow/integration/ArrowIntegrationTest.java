package yeonba.be.arrow.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.controller.ArrowController;
import yeonba.be.exception.ExceptionAdvice;
import yeonba.be.fixture.UserFixtureFactory;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.VocalRange;


@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
public class ArrowIntegrationTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExceptionAdvice exceptionAdvice;

    @Autowired
    private ArrowController arrowController;
    private MockMvc mockMvc;
    private long userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(arrowController)
            .setControllerAdvice(exceptionAdvice)
            .alwaysDo(print())
            .build();

        VocalRange vocalRange = new VocalRange("저음");
        em.persist(vocalRange);

        Animal animal = new Animal("강아지상");
        em.persist(animal);

        Area area = new Area("서울");
        em.persist(area);
        em.flush();

        User user = UserFixtureFactory.builder()
            .vocalRange(vocalRange)
            .animal(animal)
            .area(area)
            .build().getFixture();
        em.persist(user);
        em.flush();

        userId = user.getId();
    }

    @DisplayName("출석 체크를 할 수 있다.")
    @Test
    void dailyCheck() throws Exception {

        // when & then
        mockMvc.perform(
                post("/daily-check")
                    .requestAttr("userId", userId)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.message").isEmpty())
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
