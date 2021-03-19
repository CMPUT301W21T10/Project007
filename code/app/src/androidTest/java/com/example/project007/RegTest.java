package com.example.project007;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<RegActivity> rule = new ActivityTestRule<RegActivity>(RegActivity.class,true,true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     *Tset from login activity to main activity
     * Enter username and click login button
     */
    @Test
    public void checkToHome() {
        solo.assertCurrentActivity("Wong Activity", RegActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.reg_name));
        solo.clearEditText((EditText) solo.getView(R.id.reg_email));
        solo.clearEditText((EditText) solo.getView(R.id.reg_phone));
        solo.enterText((EditText) solo.getView(R.id.reg_name),"Alex Xu");
        solo.enterText((EditText) solo.getView(R.id.reg_email),"1295724@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.reg_phone),"6478463215");
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Wong Activity", LoginActivity.class);
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
