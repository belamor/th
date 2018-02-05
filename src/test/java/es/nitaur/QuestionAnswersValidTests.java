package es.nitaur;

import es.nitaur.domain.Quiz;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static es.nitaur.TestUtil.convertObjectToJsonBytes;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionAnswersValidTests {

    @Autowired
    private MockMvc mockMvc;

    private QuizQuestion question;

    @Before
    public void init(){
        question = new QuizQuestion();
        question.setQuestion("TestQuestion");
    }

    @Test
    public void shouldNotSaveAnswersWithNoAnswerText() throws Exception {

        this.mockMvc.perform(patch("/api/quizzes/questions/1/answers")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(Collections.singletonList(new QuizAnswer()))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("EntityNotValidException")));
    }
}
