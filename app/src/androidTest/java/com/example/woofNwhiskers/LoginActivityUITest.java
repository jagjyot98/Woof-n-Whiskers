package com.example.woofNwhiskers;

import android.os.RemoteException;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityUITest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void loginUser_Success() throws RemoteException {
        // Ensure that the device is unlocked
        unlockDevice();

        // Input data
        String email = "test@example.com";
        String password = "12345678";

        // Type email and password
        Espresso.onView(ViewMatchers.withId(R.id.emlEdt))
                .perform(ViewActions.typeText(email), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.psdEdt))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());

        // Click login button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn))
                .perform(ViewActions.click());

        // Check if the ProfileActivity is launched after successful login
        //Espresso.onView(ViewMatchers.withId(R.id.profileActivityLayout))
                //.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    private void unlockDevice() throws RemoteException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        if (!device.isScreenOn()) {
            device.wakeUp();
        }

        // Perform any necessary actions to unlock the device (e.g., swipe, enter PIN, etc.)
        // This can vary depending on the device and security settings.

        // Example: Swipe up to unlock
        device.swipe(300, 1000, 300, 500, 5);
    }
}