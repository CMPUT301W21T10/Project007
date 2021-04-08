package com.example.project007;

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

public class MeasurementTest {
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
    public void StartTest() throws InterruptedException {
        //testing entry
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //add Measurment
        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "MS-Test");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Measurment");
        solo.pressSpinnerItem(0, 3);
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
        assertTrue(solo.waitForText("MS-Test", 1, 2000));
        //find&enter Measurment
        ListView currentListView1 = (ListView) solo.getView("experiment_list");
        View view2 =  currentListView1.getChildAt(0);
        solo.clickOnView(view2);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView2 = (TextView) solo.getView("name_view"); // Get the listview
        String message2 = textView2.getText().toString(); // Get item from first position
        assertEquals("MS-Test", message2);
        //add first Measurment trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "22.2");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail1",1,1000));
        //add second Measurment trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "23.3");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail2",1,1000));
        //add third Measurment trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail3",1,1000));
        //test results
        solo.clickOnMenuItem("View Result");
        sleep(3000);
        solo.goBack();
        solo.goBack();

        //delete Measurment-base
        ListView experimentListView = (ListView) solo.getView("experiment_list");
        View experimentView = experimentListView.getChildAt(0);
        solo.clickLongOnView(experimentView);
        View deleteview = (View) solo.getView("button2");
        solo.clickOnView(deleteview);
        assertFalse(solo.waitForText("MS-Test", 1, 2000));
        //delete Measurment-base
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}

