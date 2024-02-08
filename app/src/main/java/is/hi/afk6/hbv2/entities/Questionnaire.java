package is.hi.afk6.hbv2.entities;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire
{
    private Long id;
    private String name;
    private List<Question> questions;
    private boolean displayOnForm;
    private List<WaitingListRequest> waitingListRequests;

    public Questionnaire()
    {
        this.questions = new ArrayList<>();
        this.waitingListRequests = new ArrayList<>();
        this.displayOnForm = false;
    }

    public Questionnaire(Long id, String name, List<Question> questions, boolean displayOnForm, List<WaitingListRequest> waitingListRequests) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.displayOnForm = displayOnForm;
        this.waitingListRequests = waitingListRequests;
    }

    public Questionnaire(String name) {
        this.name = name;

        this.questions = new ArrayList<>();
        this.waitingListRequests = new ArrayList<>();
        this.displayOnForm = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isDisplayOnForm() {
        return displayOnForm;
    }

    public void setDisplayOnForm(boolean displayOnForm) {
        this.displayOnForm = displayOnForm;
    }

    public List<WaitingListRequest> getWaitingListRequests() {
        return waitingListRequests;
    }

    public void setWaitingListRequests(List<WaitingListRequest> waitingListRequests) {
        this.waitingListRequests = waitingListRequests;
    }
}
