package edu.ucsd.cse110.walkstatic.teammate;

public class TeammateRequest {
    private final Teammate requester;
    private final Teammate target;

    public TeammateRequest(Teammate requester, Teammate target){
        this.requester = requester;
        this.target = target;
    }
    public Teammate getRequester(){
        return this.requester;
    }

    public Teammate getTarget(){
        return this.target;
    }
}
