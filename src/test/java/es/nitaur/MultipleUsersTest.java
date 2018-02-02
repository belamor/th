package es.nitaur;

import es.nitaur.domain.QuizQuestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultipleUsersTest {

    private static final String GET_QUESTION_API = "/api/quizzes/questions/1";

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void answerQuestions() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        IntStream.range(0, 10).forEach(i -> executorService.submit(new HttpPatchRunnable(port, i)));

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        QuizQuestion question = restTemplate.getForObject(GET_QUESTION_API, QuizQuestion.class);
        assertThat("There were 10 updates to the question", 10L, is(question.getUpdateCount()));
    }

}
