package is.hi.afk6.hbv2.entities;

/**
 * Users questions answers and question paired.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2024-04-06
 * @version 1.0
 */
public class QuestionAnswerPair {

    private final Question question;
    private final Integer answer;

    public QuestionAnswerPair(Question question, Integer answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return this.question;
    }

    public Integer getAnswer() {
        return this.answer;
    }
}
