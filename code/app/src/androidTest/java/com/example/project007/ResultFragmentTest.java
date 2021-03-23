package com.example.project007;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//public class ResultFragmentTest {
//    private Solo solo;
//    @Rule
//    public ActivityTestRule<TrailsActivity> rule = new ActivityTestRule<TrailsActivity>(TrailsActivity.class,true,true);
//    /**
//     * Runs before all tests and creates solo instance.
//     * @throws Exception
//     */
//
//    @Before
//    public void setup(){
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
//    }
//    /**
//     *Test for singup activity
//     * click sign up button
//     */
//    @Test
//    public void checkToSignup() {
//        solo.assertCurrentActivity("Wong Activity", LoginActivity.class);
//        solo.clickOnText("SignUp");
//        solo.assertCurrentActivity("Wong Activity", RegActivity.class);
//    }
//}
