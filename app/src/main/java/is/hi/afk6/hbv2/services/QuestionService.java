package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Question;

public interface QuestionService
{
    public Question saveNewQuestion(Question question);
    public List<Question> getAllQuestions();
    public Question getQuestionByID(Long questionID);
    public void updateQuestionByID(Long questionID, Question updatedQuestion);
    public void deleteQuestionByID(Long questionID);
}
