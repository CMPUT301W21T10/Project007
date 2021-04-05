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
    public void StartTest() {
        //测试进入
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //添加count-base
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
        solo.enterText((EditText) solo.getView(R.id.editTextName), "Count-base");
        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Count Numbers");
        solo.pressSpinnerItem(0, 1);
        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
        solo.clickOnText("OK");
        assertTrue(solo.waitForText("Count-base", 1, 2000));
        //查找并进入count-base
        ListView currentListView = (ListView) solo.getView("experiment_list");
        View convertView =  currentListView.getChildAt(4);
        solo.clickOnView(convertView);
        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
        final TextView textView = (TextView) solo.getView("name_view"); // Get the listview
        String message = (String) textView.getText().toString(); // Get item from first position
        assertEquals("Count-base", message);
        //添加第一条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "10");
        solo.clickOnText("OK");
        //添加第二条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "20");
        solo.clickOnText("OK");
        //添加第三条count-base的trail
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail2");
        solo.enterText((EditText) solo.getView(R.id.ResultText), "30");
        solo.clickOnText("OK");
        solo.goBack();

//        //添加count-base
//        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
//        solo.enterText((EditText) solo.getView(R.id.editTextName), "Count-base");
//        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Count Numbers");
//        solo.pressSpinnerItem(0, 1);
//        solo.enterText((EditText) solo.getView(R.id.minimumTrails), "0");
//        solo.clickOnText("OK");
//        assertTrue(solo.waitForText("Count-base", 1, 2000));
//
//        //查找并进入count-base
//        ListView currentListView = (ListView) solo.getView("experiment_list");
//        View convertView =  currentListView.getChildAt(0);
//        solo.clickOnView(convertView);
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//        final TextView textView = (TextView) solo.getView("name_view"); // Get the listview
//        String message = (String) textView.getText().toString(); // Get item from first position
//        assertEquals("Count-base", message);
//
//        //添加第一条count-base的trail
//        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
//        solo.enterText((EditText) solo.getView(R.id.trail_Title_editText), "Trail1");
//        solo.enterText((EditText) solo.getView(R.id.ResultText), "12");
//        solo.clickOnText("OK");




//        solo.clickLongOnView(convertView);
//
//        //Testing Trails frags
//        solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
//        solo.waitForView(R.id.ok_pressed);//now in the fragment
//        solo.goBack();
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
//        //Testing Results
//        solo.clickOnView(solo.getView(R.id.viewResult));
//        solo.goBack();
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
//        //Testing QR
//        solo.clickOnView(solo.getView(R.id.QROpt));
//        solo.goBack();
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
//        //Testing QR scan
//        solo.clickOnView(solo.getView(R.id.ScanQROpt));
//        solo.goBack();
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}