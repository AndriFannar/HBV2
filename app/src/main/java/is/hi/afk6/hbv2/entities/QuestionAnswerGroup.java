package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Health question answer groups.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2024-04-06
 * @version 1.0
 */
public class QuestionAnswerGroup implements Parcelable
{
    // Database primary key.
    private Long id;

    // Variables.
    private String groupName;

    private List<String> questionAnswers;

    private List<Long> questionIDs;


    /**
     * Create a new empty question.
     */
    public QuestionAnswerGroup()
    {
        this.questionAnswers = new ArrayList<>();
        this.questionIDs = new ArrayList<>();
    }


    /**
     * Create a new QuestionAnswerGroup.
     *
     * @param groupName       Name of QuestionAnswerGroup
     * @param questionAnswers Answers of Questions.
     */
    public QuestionAnswerGroup(String groupName, List<String> questionAnswers)
    {
        this.groupName = groupName;
        this.questionAnswers = questionAnswers;
        this.questionIDs = new ArrayList<>();
    }

    /**
     * Create a QuestionAnswerGroup from Parcel.
     *
     * @param in Parcel input.
     */
    private QuestionAnswerGroup (Parcel in)
    {
        this.id              = in.readLong();
        this.groupName       = in.readString();
        this.questionAnswers = in.readArrayList(String.class.getClassLoader());
        this.questionIDs     = in.readArrayList(Long.class.getClassLoader());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<String> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public List<Long> getQuestionIDs() {
        return questionIDs;
    }

    public void setQuestionIDs(List<Long> questionIDs) {
        this.questionIDs = questionIDs;
    }

    public void addQuestionID(Long questionID)
    {
        this.questionIDs.add(questionID);
    }

    public void removeQuestionID(Long questionID)
    {
        this.questionIDs.remove(questionID);
    }

    @NonNull
    @Override
    public String toString() {
        return "QuestionAnswerGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", questionAnswers=" + questionAnswers +
                ", questionIDs=" + questionIDs +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(groupName);
        dest.writeList(questionAnswers);
        dest.writeList(questionIDs);
    }


    public static final Parcelable.Creator<QuestionAnswerGroup> CREATOR = new Parcelable.Creator<QuestionAnswerGroup>()
    {
        public QuestionAnswerGroup createFromParcel(Parcel in)
        {
            return new QuestionAnswerGroup(in);
        }

        public QuestionAnswerGroup[] newArray(int size)
        {
            return new QuestionAnswerGroup[size];
        }
    };
}
