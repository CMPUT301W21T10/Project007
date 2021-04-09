package com.example.project007;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test view, add question and view, add answer. All the UI tests are written here, using Robotium test framework.
 */
public class Question_AnswerTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);
    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     *Login into the app.
     */

    @Test
    public void TestQA() {
        //could do the switch from ExperimentActivity to QuestionActivity
        //Waiting for editing
        //add NonNegative
        //testing entry
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "NN-test");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "non negative");
        solo.pressSpinnerItem(0, 2);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        View navView = solo.getView(R.id.nav_view);
        solo.clickOnView(navView);
        ListView ownListView1 = (ListView) solo.getView("own_list");
        View ownView1 =  ownListView1.getChildAt(0);
        solo.clickLongOnView(ownView1);
        View btn4view = solo.getView("button4");
        solo.clickOnView(btn4view);
        View homeView = solo.getView(R.id.navigation_home);
        solo.clickOnView(homeView);
        assertTrue(solo.waitForText("NN-test", 1, 2000));
        //find&enter Binomial
        ListView currentListView1 = (ListView) solo.getView("experiment_list");
        View view2 =  currentListView1.getChildAt(0);
        solo.clickOnView(view2);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView2 = (TextView) solo.getView("name_view"); // Get the listview
        String message2 = textView2.getText().toString(); // Get item from first position
        assertEquals("NN-test", message2);

        solo.clickOnMenuItem("Questions");

        solo.enterText((EditText) solo.getView(R.id.add_question_text), "questions");

        //solo.clickOnButton("Add Questions");
        solo.clickOnView(solo.getView(R.id.add_question_button));

        solo.clearEditText((EditText) solo.getView(R.id.add_question_text));//Clear edit text

        solo.waitForText("questions", 1, 2000);

        solo.clickInList(0);//click question to view answer of this question
        solo.enterText((EditText) solo.getView(R.id.add_answer_text), "answers");
        //solo.clickOnButton("Add Answers?");
        solo.clickOnView(solo.getView(R.id.add_answer_button));
        solo.clearEditText((EditText) solo.getView(R.id.add_answer_text));
        assertTrue(solo.waitForText("answers", 1, 2000));
        //check previous still there
        solo.goBack();
        solo.goBack();
        solo.goBack();
        //back to main then re-enter
        ListView currentListView2 = (ListView) solo.getView("experiment_list");
        View view3 =  currentListView2.getChildAt(0);
        solo.clickOnView(view3);
        solo.clickOnMenuItem("Questions");
        assertTrue(solo.waitForText("questions", 1, 2000));
        solo.clickInList(0);
        assertTrue(solo.waitForText("answers", 1, 2000));

        //delete Q&A
        solo.clickLongInList(0,1,2000);
        assertFalse(solo.waitForText("answers", 1, 2000));

        solo.goBack();
        solo.clickLongInList(0,0,2000);
        assertFalse(solo.waitForText("questions", 1, 2000));


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
