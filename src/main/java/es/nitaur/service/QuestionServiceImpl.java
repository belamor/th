package es.nitaur.service;

import es.nitaur.domain.GenericEntity;
import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.exception.EntityNotValidException;
import es.nitaur.repository.QuizQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.validation.Validation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuizQuestionRepository quizQuestionRepository;

    public QuestionServiceImpl(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Override
    @Transactional
    public QuizQuestion updateQuestion(final Long id, final QuizQuestion question) {
        QuizQuestion questionToUpdate = getQuestion(id);
        questionToUpdate.setQuestion(question.getQuestion());
        questionToUpdate.setAnswers(question.getAnswers());
        questionToUpdate.incrementUpdateCount();
        return questionToUpdate;

    }

    @Override
    @Transactional
    public QuizQuestion answerQuestion(final Long id, List<QuizAnswer> quizAnswers) {
        validateEntity(quizAnswers);
        QuizQuestion questionToUpdate = getQuestion(id);
        questionToUpdate.setAnswers(quizAnswers);
        questionToUpdate.incrementUpdateCount();
        return questionToUpdate;
    }

    @Override
    public QuizQuestion getQuestion(final Long id) {
        return quizQuestionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(String.format("Could not find Question with ID=%d", id));
                    return new NoResultException("Cannot find QuizQuestion with supplied id. The object is not found.");
                });
    }

    @Override
    public Collection<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    @Override
    @Transactional
    public List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions) {
        validateEntity(quizQuestions);
        return quizQuestions.stream().map(question -> {
            QuizQuestion questionToUpdate = getQuestion(question.getId());
            questionToUpdate.setQuestion(question.getQuestion());
            return questionToUpdate;
        }).collect(toList());
    }

    @Override
    public Collection<QuizQuestion> getQuestions(final Long filterBySectionId) {
        return quizQuestionRepository.findBySectionId(filterBySectionId);
    }

    private static <T extends GenericEntity> void validateEntity(List<T> answers) {
        String res = answers.stream()
                .flatMap(answer -> Validation.buildDefaultValidatorFactory().getValidator().validate(answer).stream()
                        .map(Object::toString))
                .collect(Collectors.joining());
        if (!res.isEmpty()) {
            logger.error("Answer validation failed.");
            throw new EntityNotValidException("Entity validation failed. " + res);
        }
    }
}