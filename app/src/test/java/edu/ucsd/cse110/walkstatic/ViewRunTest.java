package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class ViewRunTest {


    @Test
    public void RunCopiesName() {

        Bundle bundle = new Bundle();
        Run run = new Run("This is a run");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
        });
    }

}