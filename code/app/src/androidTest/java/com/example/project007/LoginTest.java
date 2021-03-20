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
    public void checkToSignup() {
        solo.assertCurrentActivity("Wong Activity", LoginActivity.class);
        solo.clickOnText("SignUp");
        solo.assertCurrentActivity("Wong Activity", RegActivity.class);
    }


    /**
     * check for a wrong password
     */
    @Test
    public void checkWrongUser(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
        solo.enterText((EditText) solo.getView(R.id.reg_email),"10000");
        solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     *Tset from login activity to main activity
     * Enter username and click login button
     */
    @Test
    public void checkToHome() {
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
        solo.enterText((EditText) solo.getView(R.id.reg_email),"1");
        solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }



    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
