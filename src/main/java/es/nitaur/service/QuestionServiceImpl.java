package es.nitaur.service;

import es.nitaur.domain.QuizAnswer;
import es.nitaur.domain.QuizQuestion;
import es.nitaur.repository.QuizQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class QuestionServiceImpl implements  QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuizQuestionRepository quizQuestionRepository;

    public QuestionServiceImpl(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Override
    @Transactional
    public QuizQuestion updateQuestion(Long id, final QuizQuestion question) {
        return quizQuestionRepository.findById(id)
                .map(questionToUpdate -> {
                    questionToUpdate.setQuestion(question.getQuestion());
                    questionToUpdate.setAnswers(question.getAnswers());
                    return questionToUpdate;
                })
                .orElseThrow(() -> new NoResultException("Cannot updateQuiz Question with supplied id. The object is not found."));
    }

    @Override
    @Transactional
    public QuizQuestion answerQuestion(Long id, List<QuizAnswer> quizAnswers) {
        return quizQuestionRepository.findById(id).map(questionToUpdate -> {
            questionToUpdate.setAnswers(quizAnswers);
            questionToUpdate.incrementUpdateCount();
            return questionToUpdate;
        }).orElseThrow(() -> {
            logger.error("Attempted to answer Question, but Question is not found");
            return new NoResultException(
                    "Cannot answer Question with supplied id. The object is not found.");
        });
    }

    @Override
    public QuizQuestion getQuestion(Long id) {
        return quizQuestionRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Cannot find QuizQuestion with supplied id. The object is not found."));
    }

    @Override
    public Collection<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    @Override
    @Transactional
    public List<QuizQuestion> updateQuestions(List<QuizQuestion> quizQuestions) {
        return quizQuestions.stream().map(question -> {
            QuizQuestion questionToUpdate = getQuestion(question.getId());
            questionToUpdate.setQuestion(question.getQuestion());
            return questionToUpdate;
        }).collect(toList());
    }

    @Override
    public Collection<QuizQuestion> getQuestions(Long filterBySectionId) {
        return quizQuestionRepository.findBySectionId(filterBySectionId);
    }

}
