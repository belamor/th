package es.nitaur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
    private QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @RequestMapping(value = "/api/quiz",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Quiz>> getQuizzes() {
        final Collection<Quiz> quizzes = quizService.findAll();
        return new ResponseEntity<Collection<Quiz>>(quizzes, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> getQuiz(@PathVariable final Long id) {
        final Quiz quiz = quizService.findOne(id);
        if (quiz == null) {
            return new ResponseEntity<Quiz>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Quiz>(quiz, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> createQuiz(@RequestBody final Quiz quiz) {

        List<QuizSection> sections = quiz.getSections();
        for (QuizSection section : sections) {
            section.setQuiz(quiz);
            List<QuizQuestion> quizQuestions = section.getQuizQuestions();
            for (QuizQuestion quizQuestion : quizQuestions) {
                quizQuestion.setSection(section);
            }
        }

        final Quiz savedQuiz = quizService.create(quiz);
        return new ResponseEntity<Quiz>(savedQuiz, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/quiz/delete/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable("id") final Long id) {
        quizService.delete(id);
        return new ResponseEntity<Quiz>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/api/quiz/updateQuestion/{id}",
            method = RequestMethod.POST)
    public ResponseEntity<QuizQuestion> updateQuestion(@PathVariable("id") final Long id, @RequestBody final QuizQuestion quizQuestion) {
        QuizQuestion updateQuestion = quizService.updateQuestion(id, quizQuestion);
        return new ResponseEntity<QuizQuestion>(updateQuestion, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/updateQuestions",
            method = RequestMethod.POST)
    public ResponseEntity<List<QuizQuestion>> updateQuestions(@RequestBody final List<QuizQuestion> quizQuestions) {
        List<QuizQuestion> updateQuestion = quizService.updateQuestions(quizQuestions);
        return new ResponseEntity<List<QuizQuestion>>(updateQuestion, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/getQuestion/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<QuizQuestion> getQuestion(@PathVariable("id") final Long id) {
        QuizQuestion question = quizService.getQuestion(id);
        return new ResponseEntity<QuizQuestion>(question, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/allQuestions",
            method = RequestMethod.GET)
    public ResponseEntity<List<QuizQuestion>> getQuestions(@RequestParam(value = "filterSectionId", required = false) Long filterBySectionId) {
        List<QuizQuestion> questions = quizService.getQuestions(filterBySectionId);
        return new ResponseEntity<List<QuizQuestion>>(questions, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quiz/answerQuestion/{id}",
            method = RequestMethod.POST)
    public ResponseEntity<QuizQuestion> answer(@PathVariable("id") final Long id, @RequestBody final List<QuizAnswer> quizAnswers) {
        QuizQuestion updatedQuestion = quizService.answerQuestion(id, quizAnswers);
        return new ResponseEntity<QuizQuestion>(updatedQuestion, HttpStatus.OK);
    }
}
