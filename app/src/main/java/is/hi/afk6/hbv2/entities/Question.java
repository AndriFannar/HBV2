package is.hi.afk6.hbv2.entities;

import java.util.ArrayList;
import java.util.List;

public class Question
{
    private Long id;
    private String questionString;
    private double weight;
    private List<Questionnaire> questionnaires;
    private Integer answer;

    public Question()
    {
        this.questionnaires = new ArrayList<>();
    }

    public Question(String questionString, double weight) {
        this.questionString = questionString;
        this.weight = weight;

        this.questionnaires = new ArrayList<>();
    }

    public Question(Long id, String questionString, double weight, List<Questionnaire> questionnaires, Integer answer) {
        this.id = id;
        this.questionString = questionString;
        this.weight = weight;
        this.questionnaires = questionnaires;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}
