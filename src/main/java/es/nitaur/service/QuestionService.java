package es.nitaur.service;

import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;

import java.util.Collection;
import java.util.List;

public interface QuestionService {

    QuizQuestion updateQuestion(Long id, QuizQuestion quiz);

    QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers);

    QuizQuestion getQuestion(Long id);

    Collection<QuizQuestion> getAllQuestions();

    List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions);

    Collection<QuizQuestion> getQuestions(Long sectionId);
}
