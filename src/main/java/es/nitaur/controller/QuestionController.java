package es.nitaur.controller;

import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/quizzes/questions", produces = MediaType.APPLICATION_JSON_VALUE)

public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<QuizQuestion> updateQuestion(@PathVariable("id") final Long id, @Valid @RequestBody final QuizQuestion quizQuestion) {
        return new ResponseEntity<>(questionService.updateQuestion(id, quizQuestion), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuizQuestion>> updateQuestions(@RequestBody final List<QuizQuestion> quizQuestions) {
        return new ResponseEntity<>(questionService.updateQuestions(quizQuestions), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestion> getQuestion(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(questionService.getQuestion(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<QuizQuestion>> getQuestions(@RequestParam(value = "filterSectionId", required = false) Optional<Long> filterBySectionId) {
        return new ResponseEntity<>(filterBySectionId
                .map(questionService::getQuestions)
                .orElse(questionService.getAllQuestions()), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizQuestion> answer(@PathVariable("id") final Long id, @RequestBody final List<QuizAnswer> quizAnswers) {
        return new ResponseEntity<>(questionService.answerQuestion(id, quizAnswers), HttpStatus.OK);
    }
}
