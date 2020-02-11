package edu.ucsd.cse110.walkstatic;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.ucsd.cse110.walkstatic.runs.Run;

public class RunViewModel extends ViewModel {
    MutableLiveData<Run> sharedRun = new MutableLiveData<Run>();

    public void setRun(Run run){
        this.sharedRun.setValue(run);
    }
}
