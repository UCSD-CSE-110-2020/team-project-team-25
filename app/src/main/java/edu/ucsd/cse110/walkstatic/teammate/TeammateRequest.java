package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

public class TeammateRequest {
    private Teammate requester;
    private Teammate target;

    /**
     * Called via reflection in Firebase. If a parse error occurs only this constructor is called
     * which is why a default Teammate is specified.
     */
    private TeammateRequest(){
        this.target = this.requester = new Teammate();
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

    @Override
    public boolean equals(Object other){
        if(!(other instanceof TeammateRequest)){
            return false;
        }
        TeammateRequest request = (TeammateRequest) other;
        return this.requester.equals(request.requester) && this.target.equals(request.target);
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
