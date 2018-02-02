package es.nitaur;

import com.google.common.collect.Lists;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllQuestionsValidTest {

    private static final String QUESTION_API = "/api/quizzes/questions";
    private static final String ANSWER_PUT_API = "/api/quizzes/questions/1/answers";


    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void questionsAreNotSavedWithEmptyQuestionText() {
        QuizQuestion quizQuestion1 = new QuizQuestion();
        quizQuestion1.setId(1L);
        quizQuestion1.setQuestion("<<redacted>>");

        QuizQuestion quizQuestion2 = new QuizQuestion();
        quizQuestion2.setId(2L);
        quizQuestion2.setQuestion(null);

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion1, quizQuestion2);

        restTemplate.put(QUESTION_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> exchange = restTemplate.exchange(QUESTION_API + "?filterSectionId=1",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<QuizQuestion>>() {
                });
        List<QuizQuestion> body = exchange.getBody();

        for (QuizQuestion quizQuestion : body) {
            assertThat("Question text should not be <<redacted>>", "<<redacted>>", not(quizQuestion.getQuestion()));
        }
    }

    @Test
    public void questionsAreSavedWithQuestionText() throws Exception {
        QuizQuestion quizQuestion3 = new QuizQuestion();
        quizQuestion3.setId(3L);
        quizQuestion3.setQuestion("<<redacted>>");

        QuizQuestion quizQuestion4 = new QuizQuestion();
        quizQuestion4.setId(4L);
        quizQuestion4.setQuestion("<<redacted>>");

        List<QuizQuestion> questionsToUpdate = Lists.newArrayList(quizQuestion3, quizQuestion4);

        restTemplate.put(QUESTION_API, questionsToUpdate);

        ResponseEntity<List<QuizQuestion>> exchange = restTemplate.exchange(QUESTION_API + "?filterSectionId=2", HttpMethod.GET, null, new ParameterizedTypeReference<List<QuizQuestion>>() {
        });
        List<QuizQuestion> body = exchange.getBody();

        for (QuizQuestion quizQuestion : body) {
            assertThat("Question text is only <<redacted>>", "<<redacted>>", is(quizQuestion.getQuestion()));
        }
    }
}
