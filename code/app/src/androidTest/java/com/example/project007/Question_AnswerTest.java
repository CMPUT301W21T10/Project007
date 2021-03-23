package com.example.project007;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test view, add question and view, add answer. All the UI tests are written here, using Robotium test framework.
 */
public class Question_AnswerTest {
    private Solo solo;
    @Rule
    //Waiting for editing
    public ActivityTestRule<QuestionActivity> rule = new ActivityTestRule<QuestionActivity>(QuestionActivity.class, true, true);

    /**
     * Runs before all tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Get the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     *Add a question to the listview and check question using assertTrue
     */
    @Test
    public void showQuestionTest() {
        //could do the switch from ExperimentActivity to QuestionActivity
        //Waiting for editing
        solo.assertCurrentActivity("Wrong Activity", QuestionActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_question_text), "qq");

        solo.clickOnButton("Add Questions?");

        solo.clearEditText((EditText) solo.getView(R.id.add_question_text));//Clear edit text

        assertTrue(solo.waitForText("qq", 1, 2000));
        //solo.clickLongInList(0,0,2000);
        //assertFalse(solo.waitForText("qq", 1, 2000));
    }

    /**
     * Long click a question to view answer of this question and check if the is added successfully
     */
    @Test
    public void showAnswerTest() {
        solo.assertCurrentActivity("Wrong Activity", QuestionActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_question_text), "qqqq");

        solo.clickOnButton("Add Questions?");

        solo.clearEditText((EditText) solo.getView(R.id.add_question_text));//Clear edit text

        solo.waitForText("qqqq", 1, 2000);

        solo.clickInList(0);//Long click question to view answer of this question
        solo.enterText((EditText) solo.getView(R.id.add_answer_text), "aaaa");
        solo.clickOnButton("Add Answers?");
        solo.clearEditText((EditText) solo.getView(R.id.add_answer_text));
        assertTrue(solo.waitForText("aaaa", 1, 2000));
        solo.clickLongInList(0,0,2000);
        assertFalse(solo.waitForText("aaaa", 1, 2000));

        solo.goBack();
        solo.clickLongInList(0,0,2000);
        assertFalse(solo.waitForText("qqqq", 1, 2000));


    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }




}
