package is.hi.afk6.hbv2.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;

@RunWith(AndroidJUnit4.class)
public class QuestionnaireServiceImplementationTest {
    private QuestionnaireService questionnaireService;

    @Before
    public void createUserServiceClass() {
        questionnaireService = new QuestionnaireServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Test
    public void testGetAllQuestionnairesToDisplay()
    {
        questionnaireService.getQuestionnairesOnForm(new APICallback<List<Questionnaire>>() {
            @Override
            public void onComplete(ResponseWrapper<List<Questionnaire>> result) {
                assertFalse(result.getData().isEmpty());
            }
        });
    }
}
