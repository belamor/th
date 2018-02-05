package es.nitaur;

import es.nitaur.domain.Quiz;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static es.nitaur.TestUtil.convertObjectToJsonBytes;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuizCreationTest {

    @Autowired
    private MockMvc mockMvc;

    private Quiz quizWithId;
    private Quiz quizWithoutId;

    @Before
    public void init() {

        quizWithId = new Quiz();
        quizWithId.setId(1L);
        quizWithId.setName("Quiz with Id");

        quizWithoutId = new Quiz();
        quizWithoutId.setName("Quiz without Id");
    }


    @Test
    public void cannotCreateQuizWithExistingId() throws Exception {

        this.mockMvc.perform(post("/api/quizzes")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(quizWithId)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void canCreateQuizWithoutId() throws Exception {

        this.mockMvc.perform(post("/api/quizzes")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(quizWithoutId)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Quiz without Id")));
    }
}
