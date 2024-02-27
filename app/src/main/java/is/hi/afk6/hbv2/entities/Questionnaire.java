package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Questionnaires.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/01/2024
 * @version 1.5
 */
public class Questionnaire implements Parcelable
{
    private Long id;
    private String name;
    private List<Question> questions;
    private boolean displayOnForm;
    private List<WaitingListRequest> waitingListRequests;

    /**
     * Create a new empty Questionnaire.
     */
    public Questionnaire()
    {
        this.questions = new ArrayList<>();
        this.waitingListRequests = new ArrayList<>();
        this.displayOnForm = false;
    }

    /**
     * Create a new Questionnaire.
     *
     * @param id                  Unique ID of Questionnaire.
     * @param name                Name of Questionnaire.
     * @param questions           Questions belonging to the Questionnaire.
     * @param displayOnForm       Display this Questionnaire on registration form.
     * @param waitingListRequests WaitingListRequests that have been assigned this Questionnaire.
     */
    public Questionnaire(Long id, String name, List<Question> questions, boolean displayOnForm, List<WaitingListRequest> waitingListRequests) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.displayOnForm = displayOnForm;
        this.waitingListRequests = waitingListRequests;
    }

    /**
     * Create a new Questionnaire.
     *
     * @param name Name of Questionnaire.
     */
    public Questionnaire(String name) {
        this.name = name;

        this.questions = new ArrayList<>();
        this.waitingListRequests = new ArrayList<>();
        this.displayOnForm = false;
    }

    /**
     * Create a Questionnaire from Parcel.
     *
     * @param in Parcel input.
     */
    private Questionnaire (Parcel in)
    {
        this.id                  = in.readLong();
        this.name                = in.readString();
        this.questions           = in.readArrayList(Question.class.getClassLoader());
        this.displayOnForm       = in.readByte() != 0;
        this.waitingListRequests = in.readArrayList(WaitingListRequest.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeList(questions);
        dest.writeByte((byte) (displayOnForm ? 1 : 0));
        dest.writeList(waitingListRequests);
    }


    public static final Parcelable.Creator<Questionnaire> CREATOR = new Parcelable.Creator<Questionnaire>()
    {
        public Questionnaire createFromParcel(Parcel in)
        {
            return new Questionnaire(in);
        }

        public Questionnaire[] newArray(int size)
        {
            return new Questionnaire[size];
        }
    };
}
