package ar.edu.itba.population.models.socialnetwork;

import ar.edu.itba.population.TimeInterval;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FriendFinderTest {

    @Test
    public void getFriendsFor() {
        Person p1 = new Person(1, new TimeInterval(1990, 2010));
        Person p2 = new Person(2, new TimeInterval(1991, 2010));
        Person p3 = new Person(3, new TimeInterval(1992, 2010));
        Person p4 = new Person(4, new TimeInterval(1993, 2010));
        Person p5 = new Person(5, new TimeInterval(1994, 2010));

        List<Person> persons = Arrays.asList(p1, p2, p3, p4, p5);
        FriendFinder friendFinder = new FriendFinder(persons, 4);
        List<Person> p1Friends = friendFinder.getFriendsFor(p1);
        assertTrue(p1Friends.size() <= 4);
        assertEquals(p1Friends.size(), p1Friends.stream().distinct().count());
    }

    @Test
    public void shouldNotReturnFriendsIfAlreadyHasReachedLimit() {
        Person p1 = new Person(1, new TimeInterval(1990, 2010));
        Person p2 = new Person(2, new TimeInterval(1991, 2010));
        p1.addFriend(p2);
        List <Person> persons = Arrays.asList(p1, p2);

        FriendFinder friendFinder = new FriendFinder(persons, 1);
        List<Person> p1Friends = friendFinder.getFriendsFor(p1);
        assertEquals(0, p1Friends.size());
    }

    @Test
    public void shouldFilterAlreadyFriends() {
        Person p1 = new Person(1, new TimeInterval(1990, 2010));
        Person p2 = new Person(2, new TimeInterval(1991, 2010));
        p1.addFriend(p2);
        List <Person> persons = Arrays.asList(p1, p2);

        FriendFinder friendFinder = new FriendFinder(persons, 2);
        List<Person> p1Friends = friendFinder.getFriendsFor(p1);
        assertEquals(0, p1Friends.size());
    }

}