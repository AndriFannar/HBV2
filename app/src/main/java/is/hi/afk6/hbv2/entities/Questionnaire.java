package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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
    private List<Long> questionIDs;
    private boolean displayOnForm;

    /**
     * Create a new empty Questionnaire.
     */
    public Questionnaire()
    {
        this.questionIDs = new ArrayList<>();
        this.displayOnForm = false;
    }

    /**
     * Create a new Questionnaire.
     *
     * @param id                  Unique ID of Questionnaire.
     * @param name                Name of Questionnaire.
     * @param questionIDs           Questions belonging to the Questionnaire.
     * @param displayOnForm       Display this Questionnaire on registration form.
     */
    public Questionnaire(Long id, String name, List<Long> questionIDs, boolean displayOnForm) {
        this.id = id;
        this.name = name;
        this.questionIDs = questionIDs;
        this.displayOnForm = displayOnForm;
    }

    /**
     * Create a new Questionnaire.
     *
     * @param name Name of Questionnaire.
     */
    public Questionnaire(String name)
    {
        this.name = name;
        this.questionIDs = new ArrayList<>();
        this.displayOnForm = false;
    }

    /**
     * Create a Questionnaire from Parcel.
     *
     * @param in Parcel input.
     */
    private Questionnaire (Parcel in)
    {
        this.id            = in.readLong();
        this.name          = in.readString();
        this.questionIDs   = in.readArrayList(Long.class.getClassLoader());
        this.displayOnForm = in.readByte() != 0;
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

    public List<Long> getQuestionIDs() {
        return questionIDs;
    }

    public void setQuestionIDs(List<Long> questionIDs) {
        this.questionIDs = questionIDs;
    }

    public boolean isDisplayOnForm() {
        return displayOnForm;
    }

    public void setDisplayOnForm(boolean displayOnForm) {
        this.displayOnForm = displayOnForm;
    }

    @NonNull
    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", questionIDs=" + questionIDs +
                ", displayOnForm=" + displayOnForm +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeList(questionIDs);
        dest.writeByte((byte) (displayOnForm ? 1 : 0));
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
