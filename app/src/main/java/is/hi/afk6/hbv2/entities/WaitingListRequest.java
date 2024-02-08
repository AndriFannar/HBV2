package is.hi.afk6.hbv2.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaitingListRequest
{
    private Long id;
    private User patient;
    private User staff;
    private String description;
    private boolean status;
    private LocalDate dateOfRequest;
    private Questionnaire questionnaire;
    private List<Integer> questionnaireAnswers;
    private double grade;

    public WaitingListRequest() {
    }

    public WaitingListRequest(Long id, User patient, User staff, String description, boolean status, LocalDate dateOfRequest, Questionnaire questionnaire, List<Integer> questionnaireAnswers, double grade) {
        this.id = id;
        this.patient = patient;
        this.staff = staff;
        this.description = description;
        this.status = status;
        this.dateOfRequest = dateOfRequest;
        this.questionnaire = questionnaire;
        this.questionnaireAnswers = questionnaireAnswers;
        this.grade = grade;
    }

    public WaitingListRequest(User patient, User staff, String description, Questionnaire questionnaire) {
        this.patient = patient;
        this.staff = staff;
        this.description = description;
        this.questionnaire = questionnaire;

        this.dateOfRequest = LocalDate.now();
        this.grade = 0;
        this.questionnaireAnswers = new ArrayList<>();
        this.status = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User staff) {
        this.staff = staff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(LocalDate dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public List<Integer> getQuestionnaireAnswers() {
        return questionnaireAnswers;
    }

    public void setQuestionnaireAnswers(List<Integer> questionnaireAnswers) {
        this.questionnaireAnswers = questionnaireAnswers;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
