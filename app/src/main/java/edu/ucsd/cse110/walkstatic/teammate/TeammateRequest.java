package edu.ucsd.cse110.walkstatic.teammate;

public class TeammateRequest {
    private Teammate requester;
    private Teammate target;

    private TeammateRequest(){
    }

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
