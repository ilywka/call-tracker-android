package com.sashnikov.android.calltracker.ui.activity.synchronization;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.sashnikov.android.calltracker.viewmodel.SynchronizationViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;

/**
 * @author Ilya_Sashnikau
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SynchronizationActivityTest {

    @Rule
    public IntentsTestRule<SynchronizationActivity> intentsTestRule =
            new IntentsTestRule<>(SynchronizationActivity.class, true, true);
    private SynchronizationViewModel viewModelMock;


    @Before
    public void setUp() {
        viewModelMock = BDDMockito.mock(SynchronizationViewModel.class);
        intentsTestRule.getActivity().synchronizationViewModel = viewModelMock;
    }

    @Test
    public void testSettingsIntent() {
        SynchronizationActivity activity = intentsTestRule.getActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Settings")).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(SynchronizationSettingsActivity.class.getName()));
    }
}