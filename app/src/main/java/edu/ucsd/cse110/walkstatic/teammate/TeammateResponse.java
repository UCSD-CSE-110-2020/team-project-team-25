package edu.ucsd.cse110.walkstatic.teammate;

import com.google.gson.Gson;

import edu.ucsd.cse110.walkstatic.R;

public class TeammateResponse {
    public enum Response {
        GOING(R.string.going),
        BAD_TIME(R.string.bad_time),
        NOT_GOOD(R.string.not_good);

        private int stringResource;

        Response(int stringResource){
            this.stringResource = stringResource;
        }

        public int getStringResource(){
            return this.stringResource;
        }
    }

    private Response response;
    private Teammate user;

    public TeammateResponse(){
        this.user = null;
        this.response = Response.GOING;
    }

    public TeammateResponse(Teammate user){
        this();
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
