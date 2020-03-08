package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TeammateResponseTest {
    @Test
    public void responseIndicatesTheSameAsSetEarlier(){
        TeammateResponse response = new TeammateResponse(null);
        response.setResponse(TeammateResponse.Response.NOT_GOOD);
        assertThat(response.getResponse()).isEqualTo(TeammateResponse.Response.NOT_GOOD);
    }

    @Test
    public void defaultResponseIsGoing(){
        TeammateResponse response = new TeammateResponse(null);
        assertThat(response.getResponse()).isEqualTo(TeammateResponse.Response.GOING);
    }

    @Test
    public void userIsSameThatIsSpecifiedEarlier(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        assertThat(response.getUser()).isEqualTo(user);
    }

    @Test
    public void teammateResponseNotEqualToNull(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        assertThat(response.equals(null)).isFalse();
    }

    @Test
    public void teammateResponseNotEqualToRandomObject(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        assertThat(response.equals("")).isFalse();
    }

    @Test
    public void teammateResponseNotEqualToDifferentTeammateInResponse(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("Bob");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        assertThat(response).isNotEqualTo(otherResponse);
    }

    @Test
    public void teammateResponseIsEqualToIdenticalResponse(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("John");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        assertThat(response).isEqualTo(otherResponse);
    }

    @Test
    public void equalsRespectsResponseType(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("John");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.GOING);
        assertThat(response).isNotEqualTo(otherResponse);
    }

    @Test
    public void hashCodesEqualWithSameDataEqual(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("John");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        assertThat(response.hashCode()).isEqualTo(otherResponse.hashCode());
    }

    @Test
    public void hashCodesEqualWithDifferentResponseUnequal(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("John");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.GOING);
        assertThat(response.hashCode()).isNotEqualTo(otherResponse.hashCode());
    }

    @Test
    public void hashCodesEqualWithDifferentUsersUnequal(){
        Teammate user = new Teammate("John");
        TeammateResponse response = new TeammateResponse(user);
        response.setResponse(TeammateResponse.Response.BAD_TIME);
        Teammate otherUser = new Teammate("Bob");
        TeammateResponse otherResponse = new TeammateResponse(otherUser);
        otherResponse.setResponse(TeammateResponse.Response.BAD_TIME);
        assertThat(response.hashCode()).isNotEqualTo(otherResponse.hashCode());
    }


}
