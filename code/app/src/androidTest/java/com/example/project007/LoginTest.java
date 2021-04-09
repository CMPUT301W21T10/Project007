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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for LoginActivity. All the UI tests are written here.Robotium test framework is used
 */

public class LoginTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<LoginActivity>(LoginActivity.class,true,true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }


    /**
     *Test for singup activity
     * click sign up button
     */
    @Test
    public void checkTologin() {
        solo.assertCurrentActivity("Wong Activity", LoginActivity.class);
        //solo.clickOnText("SignUp");
        solo.assertCurrentActivity("Wong Activity", MainActivity.class);
    }

  //  @Test
  //  public void checkTologinold() {
    //    solo.assertCurrentActivity("Wong Activity", LoginActivity.class);
        //solo.clickOnText("SignUp");
  //      solo.clearEditText((EditText) solo.getView(R.id.reg_email));
 //       solo.enterText((EditText) solo.getView(R.id.reg_email),"830834555938f14e");
        //solo.clickOnText("Sign In");
 //       solo.assertCurrentActivity("Wong Activity", MainActivity.class);
//    }


//    /**
//     * check for a wrong password
//     */
//    @Test
//    public void checkWrongUser(){
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
//        solo.enterText((EditText) solo.getView(R.id.reg_email),"10000");
//        solo.clickOnText("Sign In");
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//    }

//    /**
//     *Tset from login activity to main activity
//     * Enter username and click login button
//     */
//    @Test
//    public void checkToHome() {
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
//        solo.enterText((EditText) solo.getView(R.id.reg_email),"1");
//        solo.clickOnText("Sign In");
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//    }

//    /**
//     *Tset from login activity to main activity
//     * Enter username and click login button
//     */
//    @Test
//    public void checkExperiment() {
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
//        solo.enterText((EditText) solo.getView(R.id.reg_email),"1");
//        solo.clickOnText("Sign In");
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnView((FloatingActionButton) solo.getView(R.id.add_experiment_button));
//
//        solo.enterText((EditText) solo.getView(R.id.editTextName), "Name");
//        solo.enterText((EditText) solo.getView(R.id.editTextDescription), "Description");
//        solo.clickOnText("OK");
//        solo.goBack();
//
//        ListView currentListView = (ListView) solo.getView("experiment_list");
//        View convertView =  currentListView.getChildAt(0);
//        solo.clickOnView(convertView);
//
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//        solo.goBack();
//
//        solo.clickLongOnView(convertView);
//
//        solo.clickOnView((Button) solo.getView(R.id.button));
//
//        solo.clearEditText((EditText) solo.getView(R.id.editTextName)); //Clear the EditText
//        solo.enterText((EditText) solo.getView(R.id.editTextName), "newName");
//        solo.clickOnText("OK");
//
//        solo.clickOnView(convertView);
//
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
//        final TextView textView = (TextView) solo.getView("name_view"); // Get the listview
//        String message = (String) textView.getText().toString(); // Get item from first position
//
//        assertEquals("newName", message);
//
//        //Testing Trails frags
//        //solo.clickOnView((FloatingActionButton) solo.getView(R.id.experimentBtn));
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
//        //solo.clickOnView(solo.getView(R.id.ScanQROpt));
//        solo.goBack();
//        solo.assertCurrentActivity("Wrong Activity", TrailsActivity.class);
//
//    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
