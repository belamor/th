package es.nitaur.service;

import es.nitaur.domain.Quiz;
import es.nitaur.repository.QuizRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Collection<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz getQuiz(final Long id) {
        return Optional.ofNullable(quizRepository.findOne(id))
                .orElseThrow(() -> new NoResultException("Cannot find Quiz with supplied id. The object is not found."));
    }

    public Quiz createQuiz(final Quiz quiz) {
        if (quiz.getId() != null) {
            logger.error("Attempted to createQuiz a Quiz, but id attribute was not null.");
            throw new EntityExistsException(
                    "Cannot createQuiz new Quiz with supplied id. The id attribute must be null to createQuiz an entity.");
        }
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(final Long id) {
        quizRepository.delete(id);
    }
}
