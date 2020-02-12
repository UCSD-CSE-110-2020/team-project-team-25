package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.fakes.RoboMenuItem;

import java.util.UUID;

import androidx.fragment.app.testing.FragmentScenario;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.speech.SpeechListener;
import edu.ucsd.cse110.walkstatic.speech.VoiceDictation;
import edu.ucsd.cse110.walkstatic.speech.VoiceDictationFactory;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class EditRunTest {

    @Test
    public void runCreatedWithRandomUUIDAndFilledName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run).isEqualTo(new Run(run.getUUID(), "Run 1"));
        });
    }

    @Test
    public void RunCreatedWithGivenUUIDAndFilledName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        UUID uuid = UUID.randomUUID();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UUID", uuid);
        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class, bundle);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run).isEqualTo(new Run(uuid, "Run 1"));
        });
    }

    @Test
    public void voiceDictationFillsInNameWithNoError() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        VoiceDictationMock voiceDictationMock = new VoiceDictationMock();
        VoiceDictationFactory.setCurrentBlueprint(context -> {
            return voiceDictationMock;
        });

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            ImageButton runNameButton = fragment.getActivity().findViewById(R.id.dictate_name);
            runNameButton.callOnClick();
            voiceDictationMock.callOnSpeech("Run 1");
            voiceDictationMock.callOnFinish(false);
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run.getName()).isEqualTo( "Run 1");
        });
    }

    @Test
    public void voiceDictationDisablesButtonsDuringRecording() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        VoiceDictationMock voiceDictationMock = new VoiceDictationMock();
        VoiceDictationFactory.setCurrentBlueprint(context -> {
            return voiceDictationMock;
        });

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            ImageButton runNameButton = fragment.getActivity().findViewById(R.id.dictate_name);
            ImageButton startingPointButton = fragment.getActivity().findViewById(R.id.dictate_starting_point);

            assertThat(runNameButton.isEnabled()).isTrue();
            assertThat(startingPointButton.isEnabled()).isTrue();

            runNameButton.callOnClick();

            voiceDictationMock.callOnSpeech("Run 2");
            assertThat(runNameButton.isEnabled()).isFalse();
            assertThat(startingPointButton.isEnabled()).isFalse();

            voiceDictationMock.callOnFinish(false);
            assertThat(runNameButton.isEnabled()).isTrue();
            assertThat(startingPointButton.isEnabled()).isTrue();
        });
    }

    @Test
    public void voiceDictationOnErrorHasOldText() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        VoiceDictationMock voiceDictationMock = new VoiceDictationMock();
        VoiceDictationFactory.setCurrentBlueprint(context -> {
            return voiceDictationMock;
        });

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            ImageButton runNameButton = fragment.getActivity().findViewById(R.id.dictate_name);
            runNameButton.callOnClick();
            voiceDictationMock.callOnSpeech("Run 1");
            voiceDictationMock.callOnFinish(false);
            runNameButton.callOnClick();
            voiceDictationMock.callOnFinish(true);
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            Run run = runViewModel.sharedRun.getValue();
            assertThat(run.getName()).isEqualTo( "Run 1");
        });
    }

    @Test
    public void onNavigateAwayCancelsActiveVoiceRequest() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        VoiceDictationMock voiceDictationMock = new VoiceDictationMock();
        VoiceDictationFactory.setCurrentBlueprint(context -> {
            return voiceDictationMock;
        });

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            ImageButton runNameButton = fragment.getActivity().findViewById(R.id.dictate_name);
            runNameButton.callOnClick();
            voiceDictationMock.callOnSpeech("Run 1");
            MenuItem save = new RoboMenuItem(R.id.action_save);
            fragment.onOptionsItemSelected(save);

            RunViewModel runViewModel = new ViewModelProvider(fragment.getActivity()).get(RunViewModel.class);
            fragment.onDestroyView();
            assertThat(voiceDictationMock.canceled).isTrue();
        });
    }

    @Test
    public void saveDisabledWhenNoName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            MenuItem save = shadowOf(fragment.getActivity()).getOptionsMenu().findItem(R.id.action_save);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            assertThat(save.isEnabled()).isFalse();
            runName.setText("Run 1");
            //Workaround for bug in robolectric https://github.com/robolectric/robolectric/issues/3585
            fragment.onPrepareOptionsMenu(shadowOf(fragment.getActivity()).getOptionsMenu());
            runName.setText("");
            //Workaround for bug in robolectric https://github.com/robolectric/robolectric/issues/3585
            fragment.onPrepareOptionsMenu(shadowOf(fragment.getActivity()).getOptionsMenu());
            assertThat(save.isEnabled()).isFalse();
        });
    }

    @Test
    public void saveEnabledWhenNameExists() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            MenuItem save = shadowOf(fragment.getActivity()).getOptionsMenu().findItem(R.id.action_save);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            //Workaround for bug in robolectric https://github.com/robolectric/robolectric/issues/3585
            fragment.onPrepareOptionsMenu(shadowOf(fragment.getActivity()).getOptionsMenu());
            assertThat(save.isEnabled()).isTrue();
        });
    }

    @Test
    public void runNameTextHasNoErrorWhenNoNameAtStart() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            assertThat(runName.getError()).isNull();
        });
    }

    @Test
    public void runNameTextHasErrorWhenNoNameAfterType() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            runName.setText("");
            assertThat(runName.getError()).isNotNull();
        });
    }

    @Test
    public void runNameTextHasNoErrorWithName() {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setGraph(R.navigation.nav_graph);

        FragmentScenario<EditRunFragment> scenario = FragmentScenario.launchInContainer(EditRunFragment.class);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
            EditText runName = fragment.getActivity().findViewById(R.id.run_name_text);
            runName.setText("Run 1");
            assertThat(runName.getError()).isNull();
        });
    }

    private class VoiceDictationMock implements VoiceDictation {
        SpeechListener listener;
        private Bundle bundle;
        boolean canceled = false;
        @Override
        public void setListener(SpeechListener listener) {
            this.listener = listener;
        }

        @Override
        public void doRecognition(@Nullable Bundle arguments) {
            this.bundle = arguments;
        }

        public void callOnSpeech(String speech){
            this.listener.onSpeech(speech, this.bundle);
        }

        public void callOnFinish(boolean error){
            this.listener.onSpeechDone(error, this.bundle);
        }

        public void cancel(){
            this.canceled = true;
        }
    }

}