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

/**
 * Test view, add question and view, add answer. All the UI tests are written here, using Robotium test framework.
 */
public class AppTest {
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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //adding count-base
        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Count-base");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Count Numbers");
        solo.pressSpinnerItem(0, 0);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.pressSpinnerItem(1, 0);
        solo.clickOnText("OK");
        View navView = solo.getView(R.id.nav_view);
        solo.clickOnView(navView);
        ListView ownListView = (ListView) solo.getView("own_list");
        View ownView =  ownListView.getChildAt(0);
        solo.clickLongOnView(ownView);
        View btn4view = solo.getView("button4");
        solo.clickOnView(btn4view);
        View homeView = solo.getView(R.id.navigation_home);
        solo.clickOnView(homeView);
        assertTrue(solo.waitForText("Count-base", 1, 2000));

        //find & enter count-base
        ListView currentListView = (ListView) solo.getView("experiment_list");
        View view1 =  currentListView.getChildAt(0);
        solo.clickOnView(view1);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView1 = (TextView) solo.getView("name_view"); // Get the listview
        String message1 = textView1.getText().toString(); // Get item from first position
        assertEquals("Count-base", message1);
        //add first count-base trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail1",1,1000));
        //add second count-base trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail2",1,1000));
        //add third count-base trail
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail3",1,1000));

        //test modify trails
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail4");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail4",1,1000));
        solo.clickLongInList(3);
        assertFalse(solo.waitForText("Trail4",1,1000));

        //ignore/un-ignore trails
        solo.clickInList(2);
        solo.clickOnMenuItem("Ignore Trail?");
        //solo.scrollToTop();
        solo.clickInList(2);
        solo.clickOnMenuItem("Un-Ignore Trail?");

        //show results
        solo.scrollToTop();
        solo.clickOnMenuItem("View Result");
        sleep(3000);
        solo.goBack();
        //show locations
        solo.clickOnMenuItem("View All Location?");
        sleep(3000);
        solo.goBack();
        //show help/tips
        solo.clickOnMenuItem("Help/Tips");
        sleep(1000);
        solo.goBack();
        //solo.goBack();

        /*//delete Count-base
        ListView experimentListView = (ListView) solo.getView("experiment_list");
        View experimentView = experimentListView.getChildAt(0);
        solo.clickLongOnView(experimentView);
        View deleteview = (View) solo.getView("button2");
        solo.clickOnView(deleteview);
        assertFalse(solo.waitForText("Count-base", 1, 2000));
        //delete Count-base*/


        //add Binomial
        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Binomial");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "TrueOrFalse");
        solo.pressSpinnerItem(0, 1);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        solo.clickOnView(navView);
        ListView ownListView1 = (ListView) solo.getView("own_list");
        View ownView1 =  ownListView1.getChildAt(1);
        solo.clickLongOnView(ownView1);
        solo.clickOnView(btn4view);
        solo.clickOnView(homeView);
        assertTrue(solo.waitForText("Binomial", 1, 2000));
        //find&enter Binomial
        ListView currentListView1 = (ListView) solo.getView("experiment_list");
        View view2 =  currentListView1.getChildAt(1);
        solo.clickOnView(view2);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView2 = (TextView) solo.getView("name_view"); // Get the listview
        String message2 = textView2.getText().toString(); // Get item from first position
        assertEquals("Binomial", message2);
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "10");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail1",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "20");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail2",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "30");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail3",1,1000));
        solo.clickOnMenuItem("View Result");
        sleep(3000);
        solo.goBack();
        solo.goBack();

        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Non-nega");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Integer numbers");
        solo.pressSpinnerItem(0, 2);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        solo.clickOnView(navView);
        ListView ownListView2 = (ListView) solo.getView("own_list");
        View ownView2 =  ownListView2.getChildAt(2);
        solo.clickLongOnView(ownView2);
        solo.clickOnView(btn4view);
        solo.clickOnView(homeView);
        assertTrue(solo.waitForText("Non-nega", 1, 2000));
        ListView currentListView2 = (ListView) solo.getView("experiment_list");
        View view3 =  currentListView2.getChildAt(2);
        solo.clickOnView(view3);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView3 = (TextView) solo.getView("name_view"); // Get the listview
        String message3 = textView3.getText().toString(); // Get item from first position
        assertEquals("Non-nega", message3);
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail1",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail2",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail3",1,1000));
        solo.clickOnMenuItem("View Result");
        sleep(3000);
        solo.goBack();
        solo.goBack();

        solo.clickOnView(solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Measurement");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Floating numbers");
        solo.pressSpinnerItem(0, 3);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        solo.clickOnView(navView);
        ListView ownListView3 = (ListView) solo.getView("own_list");
        View ownView3 =  ownListView3.getChildAt(1);
        solo.clickLongOnView(ownView3);
        solo.clickOnView(btn4view);
        solo.clickOnView(homeView);
        assertTrue(solo.waitForText("Measurement", 1, 2000));
        ListView currentListView3 = (ListView) solo.getView("experiment_list");
        View view4 =  currentListView3.getChildAt(2);
        solo.clickOnView(view4);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView4 = (TextView) solo.getView("name_view"); // Get the listview
        String message4 = textView4.getText().toString(); // Get item from first position
        assertEquals("Measurement", message4);
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail1",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        assertTrue(solo.waitForText("Trail2",1,1000));
        solo.clickOnView(solo.getView(R.id.action_add));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        solo.scrollToBottom();
        assertTrue(solo.waitForText("Trail3",1,1000));
        solo.clickOnMenuItem("View Result");
        sleep(3000);
        solo.goBack();
        solo.goBack();

        solo.clickLongOnView(view1);

    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}