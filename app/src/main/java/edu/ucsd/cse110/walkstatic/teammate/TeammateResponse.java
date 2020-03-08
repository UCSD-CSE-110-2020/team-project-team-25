package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

public class TeammateResponse {
    public enum Response {
        GOING,
        BAD_TIME,
        NOT_GOOD;
    }

    private Response response;
    private Teammate user;

    public TeammateResponse(Teammate user){
        this.response = Response.GOING;
        this.user = user;
    }

    public void setResponse(Response newResponse){
        this.response = newResponse;
    }

    public Response getResponse(){
        return this.response;
    }

    public Teammate getUser(){
        return this.user;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof TeammateResponse)){
            return false;
        }
        TeammateResponse otherResponse = (TeammateResponse) other;
        boolean usersEqual = this.user.equals(otherResponse.user);
        boolean responsesEqual = this.response == otherResponse.response;
        return usersEqual && responsesEqual;
    }

    @Override
    public int hashCode(){
        return this.user.hashCode() + this.response.ordinal();
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
