package com.example.woofNwhiskers;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class CardPaymentTest {

    @Rule
    public ActivityScenarioRule<CardPayment> activityScenarioRule =
            new ActivityScenarioRule<>(CardPayment.class);

    @Test
    public void testEmptyCardName() {
        // Start the activity
        ActivityScenario.launch(CardPayment.class);

        // Perform actions and check if the error message is displayed
        Espresso.onView(withId(R.id.payNow)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.cardName)).check(matches(hasErrorText("Enter the name on card")));
    }

    @Test
    public void testInvalidCardNumber() {
        // Start the activity
        ActivityScenario.launch(CardPayment.class);

        // Perform actions and check if the error message is displayed
        Espresso.onView(withId(R.id.cardNumber)).perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.payNow)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.cardNumber)).check(matches(hasErrorText("Enter a valid Card Number")));
    }

    // Add more tests for other validation scenarios

    // You can add tests for other validation scenarios similarly

    // Remember to run these tests on an emulator or physical device

}
