package edu.ucsd.cse110.walkstatic.store;

public interface GenericWatcher<ListenerType> {
    public void addWatcherListener(ListenerType listener);
    public void deleteAllListeners();
}
