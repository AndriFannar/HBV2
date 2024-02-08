package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;

public interface QuestionnaireService
{
    public Questionnaire saveNewQuestionnaire(Questionnaire questionnaire);
    public List<Questionnaire> getAllQuestionnaires();
    public Questionnaire getQuestionnaireByID(Long questionnaireID);
    public List<Questionnaire> getQuestionnairesOnForm();
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID);
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID);
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID);
    public void deleteQuestionnaireByID(Long questionnaireID);
}
