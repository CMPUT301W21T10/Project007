package com.example.project007;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class searchTest {
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
    public void Testsearch() {
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
        solo.clickOnView(solo.getView(R.id.action_search));
        solo.enterText((EditText) solo.getView(R.id.pop_username), "NN-test");
        solo.clickOnView(solo.getView(R.id.pop_search));
        assertTrue(solo.waitForText("NN-test", 1, 2000));
        solo.clickInList(0);//Long click
        solo.goBack();
        solo.goBack();

        //delete Count-base
        ListView experimentListView = (ListView) solo.getView("experiment_list");
        View experimentView = experimentListView.getChildAt(0);
        solo.clickLongOnView(experimentView);
        View deleteview = (View) solo.getView("button2");
        solo.clickOnView(deleteview);
        assertFalse(solo.waitForText("NN-test", 1, 2000));
        //delete Count-base

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