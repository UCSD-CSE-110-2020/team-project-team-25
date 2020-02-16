package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class ViewRunTest {


    @Test
    public void favoritedRunDisplayed() {

        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(true);
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_white_24dp);
        });
    }

    @Test
    public void unfavoritedRunDisplayed() {

        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(false);
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_border_white_24dp);
        });
    }

    @Test
    public void difficultyOfRunDisplayed() {

        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(false).setDifficulty("Moderate");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            TextView difficulty = activity.getActivity().findViewById(R.id.difficulty);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_border_white_24dp);
            assertThat(difficulty.getText().toString()).isEqualTo("Moderate");
        });
    }

}