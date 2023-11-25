package com.example.woofNwhiskers;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkRegisterButton() {
        // Check if the Register button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click on the Register button
        Espresso.onView(ViewMatchers.withId(R.id.register_btn))
                .perform(ViewActions.click());

        // Check if the RegisterActivity is launched
        Espresso.onView(ViewMatchers.withId(R.id.register_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkLoginButton() {
        // Check if the Login button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.login_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform a click on the Login button
        Espresso.onView(ViewMatchers.withId(R.id.login_btn))
                .perform(ViewActions.click());

        // Check if the LoginActivity is launched
        Espresso.onView(ViewMatchers.withId(R.id.login_activity_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
