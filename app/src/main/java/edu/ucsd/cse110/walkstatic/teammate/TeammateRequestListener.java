package edu.ucsd.cse110.walkstatic.teammate;

public interface TeammateRequestListener {
    public void onNewTeammateRequest(TeammateRequest request);
    public void onTeammateRequestDeleted(TeammateRequest request);
}
