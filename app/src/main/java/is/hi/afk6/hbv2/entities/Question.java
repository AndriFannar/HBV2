package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Question class for Questionnaire.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/01/2024
 * @version 1.5
 */
public class Question implements Parcelable
{
    private Long id;
    private String questionString;
    private double weight;
    private List<Long> questionnaireIDs;
    private int numberOfAnswers;
    private Integer answer;

    /**
     * Create a new empty Question.
     */
    public Question()
    {
        this.questionnaireIDs = new ArrayList<>();
    }

    /**
     * Create a new Question.
     *
     * @param questionString  String for question to ask.
     * @param weight          Weight of the Question.
     * @param numberOfAnswers Number of answers for the question.
     */
    public Question(String questionString, double weight, int numberOfAnswers) {
        this.questionString = questionString;
        this.weight = weight;

        this.questionnaireIDs = new ArrayList<>();
    }

    /**
     * Create a new Question.
     *
     * @param id             Unique ID of Question.
     * @param questionString String for question to ask.
     * @param weight         Weight of the Question.
     * @param numberOfAnswers Number of answers for the question.
     * @param questionnaireIDs Questionnaires that Question belongs in.
     * @param answer         User selected answer.
     */
    public Question(Long id, String questionString, double weight, int numberOfAnswers, List<Long> questionnaireIDs, Integer answer) {
        this.id = id;
        this.questionString = questionString;
        this.weight = weight;
        this.questionnaireIDs = questionnaireIDs;
        this.answer = answer;
    }

    /**
     * Create a Question from Parcel.
     *
     * @param in Parcel input.
     */
    private Question (Parcel in)
    {
        this.id               = in.readLong();
        this.questionString   = in.readString();
        this.weight           = in.readDouble();
        this.numberOfAnswers  = in.readInt();
        this.questionnaireIDs = in.readArrayList(Long.class.getClassLoader());
        this.answer           = in.readInt();
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

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public List<Long> getQuestionnaireIDs() {
        return questionnaireIDs;
    }

    public void setQuestionnaireIDs(List<Long> questionnaireIDs) {
        this.questionnaireIDs = questionnaireIDs;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(questionString);
        dest.writeDouble(weight);
        dest.writeInt(numberOfAnswers);
        dest.writeList(questionnaireIDs);
        dest.writeInt(answer);
    }


    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>()
    {
        public Question createFromParcel(Parcel in)
        {
            return new Question(in);
        }

        public Question[] newArray(int size)
        {
            return new Question[size];
        }
    };
}
