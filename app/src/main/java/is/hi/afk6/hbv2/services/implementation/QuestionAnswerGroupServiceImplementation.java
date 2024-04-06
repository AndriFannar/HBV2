package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import is.hi.afk6.hbv2.services.QuestionAnswerGroupService;

/**
 * Service for QuestionAnswerGroup class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 06/04/2024
 * @version 1.0
 */
public class QuestionAnswerGroupServiceImplementation implements QuestionAnswerGroupService
{
    @Override
    public void saveNewQuestionAnswerGroup(QuestionAnswerGroup questionAnswerGroup)
    {

    }

    @Override
    public List<QuestionAnswerGroup> getAllQuestionAnswerGroup() {
        return null;
    }

    @Override
    public QuestionAnswerGroup getQuestionAnswerGroupById(Long questionAnswerGroupID) {
        return null;
    }

    @Override
    public void updateQuestionAnswerGroup(QuestionAnswerGroup updatedQuestionAnswerGroup) {

    }

    @Override
    public void deleteQuestionAnswerGroupByID(Long questionAnswerGroupID) {

    }
}
