package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TeammateRequestTest {

    @Test
    public void teammateRequestNotEqualToNull(){
        Teammate requester = new Teammate("a");
        Teammate target = new Teammate("b");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        assertThat(request1.equals(null)).isFalse();
    }

    @Test
    public void teammateRequestNotEqualToOtherObject(){
        Teammate requester = new Teammate("a");
        Teammate target = new Teammate("b");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        assertThat(request1.equals(target)).isFalse();
    }

    @Test
    public void teammateRequestsWithSameTeammatesEqual(){
        Teammate requester = new Teammate("a");
        Teammate target = new Teammate("b");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target);
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    public void teammateRequestsWithDifferentTargetsUnequal(){
        Teammate requester = new Teammate("a");
        Teammate target = new Teammate("b");
        Teammate target2 = new Teammate("c");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target2);
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    public void teammateRequestsWithDifferentRequestersUnequal(){
        Teammate requester = new Teammate("a");
        Teammate requester2 = new Teammate("other");
        Teammate target = new Teammate("b");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester2, target);
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    public void teammateRequestsWithDifferentInstanceOfSameTeammatesEqual(){
        Teammate requester = new Teammate("a");
        Teammate requester2 = Teammate.fromJSON(requester.toString());
        Teammate target = new Teammate("b");
        Teammate target2 = Teammate.fromJSON(target.toString());
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester2, target2);
        assertThat(request1).isEqualTo(request2);
    }

    @Test
    public void equalTeammateRequestsHaveSameHash(){
        Teammate requester = new Teammate("a");
        Teammate requester2 = Teammate.fromJSON(requester.toString());
        Teammate target = new Teammate("b");
        Teammate target2 = Teammate.fromJSON(target.toString());
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester2, target2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    public void unequalTeammateRequestsHaveDifferingHash(){
        Teammate requester = new Teammate("a");
        Teammate target = new Teammate("b");
        Teammate target2 = new Teammate("c");
        TeammateRequest request1 = new TeammateRequest(requester, target);
        TeammateRequest request2 = new TeammateRequest(requester, target2);
        assertThat(request1.hashCode()).isNotEqualTo(request2.hashCode());
    }
}
