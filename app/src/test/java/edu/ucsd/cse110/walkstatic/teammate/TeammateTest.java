package edu.ucsd.cse110.walkstatic.teammate;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TeammateTest {
    @Test
    public void serializedTeammateEqual(){
        Teammate teammate = new Teammate();
        String json = teammate.toString();
        Teammate deserialized = Teammate.fromJSON(json);
        assertThat(teammate).isEqualTo(deserialized);
    }

    @Test
    public void newTeammatesNotEqual(){
        Teammate teammate = new Teammate();
        Teammate teammate2 = new Teammate();
        assertThat(teammate).isNotEqualTo(teammate2);
    }

    @Test
    public void nullTeammateNotEqual(){
        Teammate teammate = new Teammate();
        assertThat(teammate).isNotEqualTo(null);
    }

    @Test
    public void wrongObjectNotEqual(){
        Teammate teammate = new Teammate();
        assertThat(teammate).isNotEqualTo("");
    }
}
