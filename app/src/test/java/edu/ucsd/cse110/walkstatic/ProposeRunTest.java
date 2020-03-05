package edu.ucsd.cse110.walkstatic;

import android.widget.EditText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)

public class ProposeRunTest {
    @Test
    public void testTimeDate() {
        FragmentScenario<proposeRunFragment> scenario = FragmentScenario.launchInContainer(proposeRunFragment.class);
        scenario.onFragment(activity -> {
            Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(true);

            EditText date = activity.getActivity().findViewById(R.id.editDateText);
            EditText time = activity.getActivity().findViewById(R.id.editTimeText);
            RunProposal rp = new RunProposal(run);
            String fakeTime = "5:05";
            String fakeDate = "10/10/2020";

            rp.setTime(fakeTime);
            rp.setDate(fakeDate);

            date.setText(fakeDate);
            time.setText(fakeTime);

            assertThat(date.getText().toString()).isEqualTo(fakeDate);
            assertThat(time.getText().toString()).isEqualTo(fakeTime);
            assertThat(rp.getDate().equals(fakeDate));
            assertThat(rp.getTime().equals(fakeTime));
        });

    }
}
