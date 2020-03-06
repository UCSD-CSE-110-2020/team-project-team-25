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
    public void differingEmailsNotEqual(){
        Teammate teammate = new Teammate("1");
        Teammate teammate2 = new Teammate("2@gmail.com");
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

    @Test
    public void hashCodeRepresentsObject(){
        Teammate teammate = new Teammate();
        Teammate teammateClone = new Teammate();
        assertThat(teammate.hashCode()).isEqualTo(teammateClone.hashCode());
    }

    @Test
    public void noInitialsOnNoName(){
        Teammate teammate = new Teammate();
        assertThat(teammate.getInitials()).isEqualTo("");
    }

    @Test
    public void initialIsOneNameFirstChar(){
        Teammate teammate = new Teammate();
        teammate.setName("Name");
        assertThat(teammate.getInitials()).isEqualTo("N");
    }

    @Test
    public void firstLastNameGiveInitials(){
        Teammate teammate = new Teammate();
        teammate.setName("First Last");
        assertThat(teammate.getInitials()).isEqualTo("FL");
    }

    @Test
    public void firstLastNameWithTwoSpacesGivesInitials(){
        Teammate teammate = new Teammate();
        teammate.setName("First  Last");
        assertThat(teammate.getInitials()).isEqualTo("FL");
    }

    @Test
    public void threeNamesOnlyGivesFirstAndLastInitial(){
        Teammate teammate = new Teammate();
        teammate.setName("First Middle Last");
        assertThat(teammate.getInitials()).isEqualTo("FL");
    }

    @Test
    public void getColorDiffersForDifferentNames(){
        Teammate teammate = new Teammate();
        teammate.setName("Waluigi W");
        Teammate teammate2 = new Teammate();
        teammate2.setName("Waluigi Wa");
        assertThat(teammate.getColor()).isNotEqualTo(teammate2.getColor());
    }

}
