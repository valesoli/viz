package ar.edu.itba.population.models.socialnetwork;

import ar.edu.itba.population.TimeInterval;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class PersonTest {

    private Person person;
    private final TimeInterval interval = new TimeInterval(1990, 2010);

    @Before
    public void setUp() {
        person = new Person(1, interval);
    }

    @Test
    public void geTitle() {
        assertEquals(Person.TITLE, person.getTitle());
    }

    @Test
    public void toMap() {
        Map<String, Object> map = person.toMap();
        assertEquals(1, map.get("id"));
        assertEquals(Collections.singletonList(interval.toString()), map.get("interval"));
        assertEquals(person.getTitle(), map.get("title"));
    }

    @Test
    public void friendsTest() {
        Person friend = new Person(2, new TimeInterval(1995, 2010));
        Person enemy = new Person(3, new TimeInterval(1994, 2010));
        person.addFriend(friend);
        assertTrue(person.isFriendsWith(friend));
        assertFalse(person.isFriendsWith(enemy));
        assertEquals(1, person.numberOfFriends());
    }
}
