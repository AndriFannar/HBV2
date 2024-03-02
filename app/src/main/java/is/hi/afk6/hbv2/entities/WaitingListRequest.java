package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Long patientID;
    private Long staffID;
    private String description;
    private boolean status;
    private LocalDate dateOfRequest;
    private Long questionnaireID;
    private List<Integer> questionnaireAnswers;
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
     * @param patientID              Patient (User) making the WaitingListRequest.
     * @param staffID                Staff member (User) assigned to the WaitingListRequest.
     * @param description          Description of Users (patient's) problem.
     * @param status               Status of WaitingListRequest.
     * @param dateOfRequest        Date of WaitingListRequest creation.
     * @param questionnaireID        Assigned Questionnaire.
     * @param questionnaireAnswers User answers to assigned Questionnaire.
     * @param grade                Priority grade of WaitingListRequest.
     */
    public WaitingListRequest(Long id, Long patientID, Long staffID, String description, boolean status, LocalDate dateOfRequest, Long questionnaireID, List<Integer> questionnaireAnswers, double grade) {
        this.id = id;
        this.patientID = patientID;
        this.staffID = staffID;
        this.description = description;
        this.status = status;
        this.dateOfRequest = dateOfRequest;
        this.questionnaireID = questionnaireID;
        this.questionnaireAnswers = questionnaireAnswers;
        this.grade = grade;
    }

    /**
     * Create a new WaitingListRequest.
     *
     * @param patientID              Patient (User) making the WaitingListRequest.
     * @param staffID                Staff member (User) assigned to the WaitingListRequest.
     * @param description          Description of Users (patient's) problem.
     * @param questionnaireID        Assigned Questionnaire.
     */
    public WaitingListRequest(Long patientID, Long staffID, String description, Long questionnaireID) {
        this.patientID = patientID;
        this.staffID = staffID;
        this.description = description;
        this.questionnaireID = questionnaireID;

        this.dateOfRequest = LocalDate.now();
        this.grade = 0;
        this.questionnaireAnswers = new ArrayList<>();
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
        this.patientID            = in.readLong();
        this.staffID              = in.readLong();
        this.description          = in.readString();
        this.status               = in.readByte() != 0;
        this.dateOfRequest        = LocalDate.parse(in.readString());
        this.questionnaireID      = in.readLong();
        this.questionnaireAnswers = in.readArrayList(Integer.class.getClassLoader());
        this.grade                = in.readDouble();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
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

    public Long getQuestionnaireID() {
        return questionnaireID;
    }

    public void setQuestionnaireID(Long questionnaireID) {
        this.questionnaireID = questionnaireID;
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

    @Override
    public String toString() {
        return "WaitingListRequest{" +
                "id=" + id +
                ", patientID=" + patientID +
                ", staffID=" + staffID +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateOfRequest=" + dateOfRequest +
                ", questionnaireID=" + questionnaireID +
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
        dest.writeLong(patientID);
        dest.writeLong(staffID);
        dest.writeString(description);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(dateOfRequest.toString());
        dest.writeLong(questionnaireID);
        dest.writeList(questionnaireAnswers);
        dest.writeDouble(grade);
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
