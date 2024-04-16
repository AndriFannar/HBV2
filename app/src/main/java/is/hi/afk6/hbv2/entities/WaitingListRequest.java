package is.hi.afk6.hbv2.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold Waiting List Request information.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/01/2024
 * @version 1.5
 */
public class WaitingListRequest implements Parcelable
{
    private Long id;
    private User patient;
    private User staff;
    private String description;
    private boolean status;
    private LocalDate dateOfRequest;
    private Questionnaire questionnaire;
    private HashMap<Long, Integer> questionnaireAnswers;
    private double grade;

    /**
     * Create a new empty WaitingListRequest.
     */
    public WaitingListRequest()
    {
        this.dateOfRequest = LocalDate.now();
    }

    /**
     * Create a new WaitingListRequest.
     *
     * @param id                   Unique ID of the WaitingListRequest.
     * @param patient              Patient (User) making the WaitingListRequest.
     * @param staff                Staff member (User) assigned to the WaitingListRequest.
     * @param description          Description of Users (patient's) problem.
     * @param status               Status of WaitingListRequest.
     * @param dateOfRequest        Date of WaitingListRequest creation.
     * @param questionnaire        Assigned Questionnaire.
     * @param questionnaireAnswers User answers to assigned Questionnaire.
     * @param grade                Priority grade of WaitingListRequest.
     */
    public WaitingListRequest(Long id, User patient, User staff, String description, boolean status, LocalDate dateOfRequest, Questionnaire questionnaire, HashMap<Long, Integer> questionnaireAnswers, double grade) {
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

    /**
     * Create a new WaitingListRequest.
     *
     * @param patient              Patient (User) making the WaitingListRequest.
     * @param staff                Staff member (User) assigned to the WaitingListRequest.
     * @param description          Description of Users (patient's) problem.
     * @param questionnaire        Assigned Questionnaire.
     */
    public WaitingListRequest(User patient, User staff, String description, Questionnaire questionnaire) {
        this.patient = patient;
        this.staff = staff;
        this.description = description;
        this.questionnaire = questionnaire;

        this.dateOfRequest = LocalDate.now();
        this.grade = 0;
        this.questionnaireAnswers = new HashMap<>();
        this.status = false;
    }


    /**
     * Create a WaitingListRequest from Parcel.
     *
     * @param in Parcel input.
     */
    private WaitingListRequest (Parcel in)
    {
        this.id                   = in.readLong();
        this.patient              = in.readParcelable(User.class.getClassLoader());
        this.staff                = in.readParcelable(User.class.getClassLoader());
        this.description          = in.readString();
        this.status               = in.readByte() != 0;
        this.dateOfRequest        = LocalDate.parse(in.readString());
        this.questionnaire        = in.readParcelable(Questionnaire.class.getClassLoader());
        this.grade                = in.readDouble();

        Bundle bundle = in.readBundle(getClass().getClassLoader());
        this.questionnaireAnswers = (HashMap<Long, Integer>) bundle.getSerializable("map");
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

    public HashMap<Long, Integer> getQuestionnaireAnswers() {
        return questionnaireAnswers;
    }

    public void setQuestionnaireAnswers(HashMap<Long, Integer> questionnaireAnswers) {
        this.questionnaireAnswers = questionnaireAnswers;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @NonNull
    @Override
    public String toString() {
        return "WaitingListRequest{" +
                "id=" + id +
                ", patient=" + patient +
                ", staff=" + staff +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateOfRequest=" + dateOfRequest +
                ", questionnaireID=" + questionnaire +
                ", questionnaireAnswers=" + questionnaireAnswers +
                ", grade=" + grade +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(patient, flags);
        dest.writeParcelable(staff, flags);
        dest.writeString(description);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(dateOfRequest.toString());
        dest.writeParcelable(questionnaire, flags);
        dest.writeDouble(grade);

        Bundle bundle = new Bundle();
        bundle.putSerializable("map", questionnaireAnswers);
        dest.writeBundle(bundle);
    }


    public static final Parcelable.Creator<WaitingListRequest> CREATOR = new Parcelable.Creator<WaitingListRequest>()
    {
        public WaitingListRequest createFromParcel(Parcel in)
        {
            return new WaitingListRequest(in);
        }

        public WaitingListRequest[] newArray(int size)
        {
            return new WaitingListRequest[size];
        }
    };
}
