package es.nitaur.service;

import es.nitaur.domain.Quiz;

import java.util.Collection;

public interface QuizService {

    Collection<Quiz> getAllQuizzes();

    Quiz getQuiz(Long id);

    Quiz createQuiz(Quiz quiz);

    Quiz updateQuiz(Quiz quiz);

    void deleteQuiz(Long id);
}
