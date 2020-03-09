package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
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
        }).moveToState(Lifecycle.State.DESTROYED);
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
        }).moveToState(Lifecycle.State.DESTROYED);
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
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void featuresOfRunDisplayed() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(false).setDifficulty("Moderate")
                .setstreetVsTrail("Street").setloopVsOut("Loop").setflatVsHilly("Flat").setevenVsUneven("Even Surface");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            TextView difficulty = activity.getActivity().findViewById(R.id.difficulty);
            TextView streetVsTrail = activity.getActivity().findViewById(R.id.urban);
            TextView loopVsOut = activity.getActivity().findViewById(R.id.endedness);
            TextView flatVsHilly = activity.getActivity().findViewById(R.id.hillyness);
            TextView evenVsUneven = activity.getActivity().findViewById(R.id.evenness);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_border_white_24dp);
            assertThat(difficulty.getText().toString()).isEqualTo("Moderate");
            assertThat(streetVsTrail.getText().toString()).isEqualTo("Street");
            assertThat(loopVsOut.getText().toString()).isEqualTo("Loop");
            assertThat(flatVsHilly.getText().toString()).isEqualTo("Flat");
            assertThat(evenVsUneven.getText().toString()).isEqualTo("Even Surface");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void notesOfRunDisplayed() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(false).setDifficulty("Moderate")
                .setstreetVsTrail("Street").setloopVsOut("Loop").setflatVsHilly("Flat").setevenVsUneven("Even Surface").setNotes("Note");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            TextView difficulty = activity.getActivity().findViewById(R.id.difficulty);
            TextView streetVsTrail = activity.getActivity().findViewById(R.id.urban);
            TextView loopVsOut = activity.getActivity().findViewById(R.id.endedness);
            TextView flatVsHilly = activity.getActivity().findViewById(R.id.hillyness);
            TextView evenVsUneven = activity.getActivity().findViewById(R.id.evenness);
            TextView note = activity.getActivity().findViewById(R.id.notes);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_border_white_24dp);
            assertThat(difficulty.getText().toString()).isEqualTo("Moderate");
            assertThat(streetVsTrail.getText().toString()).isEqualTo("Street");
            assertThat(loopVsOut.getText().toString()).isEqualTo("Loop");
            assertThat(flatVsHilly.getText().toString()).isEqualTo("Flat");
            assertThat(evenVsUneven.getText().toString()).isEqualTo("Even Surface");
            assertThat(note.getText().toString()).isEqualTo("Notes:\nNote");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void noNotesOfRunDisplayed() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("This is a run").setStartingPoint("This is a starting point").setFavorited(false).setDifficulty("Moderate")
                .setstreetVsTrail("Street").setloopVsOut("Loop").setflatVsHilly("Flat").setevenVsUneven("Even Surface");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView runName = activity.getActivity().findViewById(R.id.run_name);
            TextView startingPoint = activity.getActivity().findViewById(R.id.starting_point);
            ImageView favoriteIndicator = activity.getActivity().findViewById(R.id.favorite_run);
            TextView difficulty = activity.getActivity().findViewById(R.id.difficulty);
            TextView streetVsTrail = activity.getActivity().findViewById(R.id.urban);
            TextView loopVsOut = activity.getActivity().findViewById(R.id.endedness);
            TextView flatVsHilly = activity.getActivity().findViewById(R.id.hillyness);
            TextView evenVsUneven = activity.getActivity().findViewById(R.id.evenness);
            TextView note = activity.getActivity().findViewById(R.id.notes);
            assertThat(runName.getText().toString()).isEqualTo("This is a run");
            assertThat(startingPoint.getText().toString()).isEqualTo("This is a starting point");
            assertThat(shadowOf(favoriteIndicator.getDrawable()).getCreatedFromResId()).isEqualTo(R.drawable.ic_star_border_white_24dp);
            assertThat(difficulty.getText().toString()).isEqualTo("Moderate");
            assertThat(streetVsTrail.getText().toString()).isEqualTo("Street");
            assertThat(loopVsOut.getText().toString()).isEqualTo("Loop");
            assertThat(flatVsHilly.getText().toString()).isEqualTo("Flat");
            assertThat(evenVsUneven.getText().toString()).isEqualTo("Even Surface");
            assertThat(note.getText().toString()).isEqualTo("");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void noStepsYeildsHifens() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("A");
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView steps = activity.getActivity().findViewById(R.id.last_steps_view);
            TextView miles = activity.getActivity().findViewById(R.id.last_miles_view);
            assertThat(steps.getText().toString()).isEqualTo("-- Steps");
            assertThat(miles.getText().toString()).isEqualTo("-- Miles");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void runWithStepsAndMilesProducesRightOutput() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("A");
        run.setInitialSteps(0);
        run.setSteps(55);
        run.setMiles(0.234);
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView steps = activity.getActivity().findViewById(R.id.last_steps_view);
            TextView miles = activity.getActivity().findViewById(R.id.last_miles_view);
            assertThat(steps.getText().toString()).isEqualTo("55 Steps");
            assertThat(miles.getText().toString()).isEqualTo(".23 Miles");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @Test
    public void runWithTimeProducestime() {
        Bundle bundle = new Bundle();
        Run run = new Run().setName("A");
        run.setInitialSteps(0);
        run.setSteps(55);
        run.setMiles(0.234);
        run.setStartTime(0);
        run.finalizeTime(4000);
        bundle.putSerializable("Run", run);
        FragmentScenario<ViewRunFragment> scenario = FragmentScenario.launchInContainer(ViewRunFragment.class, bundle);
        scenario.onFragment(activity -> {
            TextView time = activity.getActivity().findViewById(R.id.run_time);
            TextView miles = activity.getActivity().findViewById(R.id.last_miles_view);
            assertThat(time.getText().toString()).isEqualTo("Time: 00:00:04");
        }).moveToState(Lifecycle.State.DESTROYED);
    }

    @After
    public void noListenerLeak(){
        MockFirebaseHelpers.assertNoListenerLeak();
    }
}
