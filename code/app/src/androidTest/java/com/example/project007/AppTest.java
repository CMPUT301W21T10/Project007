package com.example.project007;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
            new ActivityTestRule<LoginActivity>(LoginActivity.class,true,true);
    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     *Login into the app.
     */
    @Test
    public void StartTest() throws InterruptedException {
        //测试进入
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //添加count-base
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Count-base");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Count Numbers");
        solo.pressSpinnerItem(0, 0);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        assertTrue(solo.waitForText("Count-base", 1, 2000));
        //查找并进入count-base
        ListView currentListView = (ListView) solo.getView("experiment_list");
        View view1 =  currentListView.getChildAt(0);
        solo.clickOnView(view1);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView1 = (TextView) solo.getView("name_view"); // Get the listview
        String message1 = (String) textView1.getText().toString(); // Get item from first position
        assertEquals("Count-base", message1);
        //添加第一条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第二条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第三条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //测试results
//        solo.clickOn(R.menu.menu_trails_activity);
        solo.clickOnView(solo.getView(R.id.viewResult));
        sleep(3000);
        solo.scrollToBottom();
        sleep(3000);
        solo.goBack();
        solo.clickOnMenuItem("View All Location?");
        sleep(3000);
        solo.goBack();
        solo.clickOnMenuItem("Help/Tips");
        solo.goBack();

        //添加Binomial
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Binomial");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "TrueOrFalse");
        solo.pressSpinnerItem(0, 1);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        assertTrue(solo.waitForText("Binomial", 1, 2000));
        //查找并进入Binomial
        View view2 =  currentListView.getChildAt(1);
        solo.clickOnView(view2);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView2 = (TextView) solo.getView("name_view"); // Get the listview
        String message2 = (String) textView2.getText().toString(); // Get item from first position
        assertEquals("Binomial", message2);
        //添加第一条Binomial的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "10");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第二条Binomial的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "20");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第三条Binomial的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.SuccessText), "30");
        solo.enterText((EditText) solo.getView(R.id.failText), "10");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //测试results
        solo.clickOnView(solo.getView(R.menu.menu_trails_activity),false);
        solo.clickOnButton("View Result(Plots)");
        sleep(3000);
        solo.scrollToBottom();
        sleep(3000);
        solo.goBack();

        //添加Non-negative
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Non-negative");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Non-integer numbers");
        solo.pressSpinnerItem(0, 2);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        assertTrue(solo.waitForText("Binomial", 1, 2000));
        //查找并进入Non-negative
        View view3 =  currentListView.getChildAt(2);
        solo.clickOnView(view3);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView3 = (TextView) solo.getView("name_view"); // Get the listview
        String message3 = (String) textView3.getText().toString(); // Get item from first position
        assertEquals("Binomial", message3);
        //添加第一条Non-negative的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第二条Non-negative的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第三条Non-negative的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //测试results
        solo.clickOnView(solo.getView(R.menu.menu_trails_activity),false);
        solo.clickOnButton("View Result(Plots)");
        sleep(3000);
        solo.scrollToBottom();
        sleep(3000);
        solo.goBack();

        //添加Measurement
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Measurement");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Floating numbers");
        solo.pressSpinnerItem(0, 3);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        assertTrue(solo.waitForText("Measurement", 1, 2000));
        //查找并进入Measurement
        View view4 =  currentListView.getChildAt(3);
        solo.clickOnView(view4);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView4 = (TextView) solo.getView("name_view"); // Get the listview
        String message4 = (String) textView4.getText().toString(); // Get item from first position
        assertEquals("Binomial", message4);
        //添加第一条Measurement的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第二条Measurement的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //添加第三条Measurement的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail3");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30.0");
        solo.clickOnView(solo.getView(R.id.ok_pressed));
        //测试results
        solo.clickOnView(solo.getView(R.menu.menu_trails_activity),false);
        solo.clickOnButton("View Result(Plots)");
        sleep(3000);
        solo.scrollToBottom();
        sleep(3000);
        solo.goBack();

        //测试长按的
        solo.clickLongOnView(view1);
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}