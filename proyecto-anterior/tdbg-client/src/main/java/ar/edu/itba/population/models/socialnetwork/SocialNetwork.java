package ar.edu.itba.population.models.socialnetwork;

import ar.edu.itba.population.Population;
import ar.edu.itba.population.TimeInterval;
import ar.edu.itba.population.TimeIntervalListWithStats;
import ar.edu.itba.population.models.*;
import ar.edu.itba.population.models.common.City;
import ar.edu.itba.util.Pair;
import ar.edu.itba.population.models.cpath.CPathGenerationConfiguration;
import ar.edu.itba.population.models.cpath.CPathGenerator;
import ar.edu.itba.population.models.cpath.CPathIntervalGenerator;
import ar.edu.itba.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SocialNetwork extends Population {

    private final Logger logger = LoggerFactory.getLogger(SocialNetwork.class);

    /**
     * Number of persons
     */
    private final int personCount;

    /**
     * Maximum amount of friendships for each person
     */
    private final int maxFriendships;

    /**
     * Maximum amount of intervals on each friendship
     */
    private final int maxFriendshipIntervals;

    /**
     * Number of brands
     */
    private final int brandCount;

    /**
     * Maximum amount of fans for each brand
     */
    private final int maxFans;

    private final List<CPathGenerationConfiguration> cPathGenerationConfigurations;

    /**
     * Maximum amount of intervals on each fan relationship
     */
    private final int maxFansIntervals;

    private final int cityCount;

    private final Map<Integer, Person> persons = new HashMap<>();

    private final Map<Integer, Brand> brands = new HashMap<>();

    private final Map<Integer, City> cities = new HashMap<>();

    private final List<Relationship> livedIn = new ArrayList<>();

    private final List<Relationship> friendships = new ArrayList<>();

    private final List<Relationship> fans = new ArrayList<>();

    protected SocialNetwork(SocialNetworkBuilder builder) {
        this.personCount = builder.personCount;
        this.maxFriendships = builder.maxFriendships;
        this.maxFriendshipIntervals = builder.maxFriendshipIntervals;
        this.brandCount = builder.brandCount;
        this.maxFans = builder.maxFans;
        this.maxFansIntervals = builder.maxFansIntervals;
        this.cityCount = builder.cityCount;
        this.cPathGenerationConfigurations = builder.cPathConfigurations;
    }

    public static int generatedNodes(List<CPathGenerationConfiguration> configurations) {
        return configurations.stream()
                .map(CPathGenerationConfiguration::nodesToGenerate)
                .reduce(0, Integer::sum);
    }

    protected void generateNodesAndRelationships() {

        logger.debug("Creating cities...");
        for (int i = 0; i < cityCount; i++){
            createCity();
        }

        cPathGenerationConfigurations.forEach(this::generateCPaths);

        int remainingPersons = personCount - generatedNodes(this.cPathGenerationConfigurations);

        logger.debug("Creating persons...");
        for (int i = 0; i < remainingPersons; i++) {
            final TimeInterval t = new TimeInterval(TimeInterval.TIME_INTERVAL_MAX_YEAR);
            createPerson(t);
        }

        logger.debug("Creating brands...");
        for (int i = 0; i < brandCount; i++) {
            createBrand();
        }

        logger.debug("Creating lived in edges...");
        persons.keySet().forEach(this::createLivedIn);

        logger.debug("Creating friendship edges...");
        List<Person> personList = new ArrayList<>(persons.values());
        FriendFinder friendFinder = new FriendFinder(personList, maxFriendships);
        for (Person person : personList) {
            createFriendships(person, friendFinder);
        }

        logger.debug("Creating fan edges...");
        brands.values().forEach(this::createFans);
        unwindData();
    }

    private void unwindData() {
        logger.debug("Starting unwind process");
        logger.debug("Unwinding persons...");
        unwindObjectNodes(persons);
        logger.debug("Unwinding brands...");
        unwindObjectNodes(brands);
        logger.debug("Unwinding cities...");
        unwindObjectNodes(cities);
        logger.debug("Unwinding attribute and value nodes...");
        unwindAttributesAndValues();
        logger.debug("Unwinding lived in...");
        unwindRelationships(livedIn, RelationshipType.LIVED_IN);
        logger.debug("Unwinding friendships...");
        unwindFriendships();
        logger.debug("Unwinding fans...");
        unwindRelationships(fans, RelationshipType.FAN);
    }

    private void unwindFriendships() {
        unwindRelationships(friendships, RelationshipType.FRIEND);
    }

    private void unwindObjectNodes(Map<Integer, ? extends ObjectNode> objectNodes) {
        List<ObjectNode> objectNodeList = new ArrayList<>(objectNodes.values());
        unwindNodes(objectNodeList, ObjectNode.LABEL);
    }

    private void generateCPaths(CPathGenerationConfiguration cPathGenerationConfiguration) {
        for (int path = 0; path < cPathGenerationConfiguration.getNumberOfPaths(); path++) {
            int numberOfPersons = cPathGenerationConfiguration.getMinimumLengthOfPath() + 1;

            TimeIntervalListWithStats timeIntervalListWithStats = TimeInterval.list(numberOfPersons);
            List<TimeInterval> objectNodeIntervals = timeIntervalListWithStats.getIntervals();

            Pair<List<TimeInterval>, TimeInterval> cPathIntervals = new CPathGenerator(new CPathIntervalGenerator())
                    .generate(timeIntervalListWithStats);

            List<TimeInterval> relationShipIntervals = cPathIntervals.getLeft();

            List<Integer> ids = new ArrayList<>();

            for (TimeInterval nodeInterval : objectNodeIntervals) {
                Person person = createPerson(nodeInterval);
                ids.add(person.getId());
            }

            for (int i = 0; i < relationShipIntervals.size(); i++) {
                Person firstPerson = persons.get(ids.get(i));
                Person secondPerson = persons.get(ids.get(i + 1));
                createFriendship(firstPerson, secondPerson, Collections.singletonList(relationShipIntervals.get(i)));
            }

            logger.info("CPath of length {} with interval {} generated from node {} to {}. Ids {}, intervals {}", cPathGenerationConfiguration.getMinimumLengthOfPath(), cPathIntervals.getRight(), ids.get(0), ids.get(ids.size() - 1), ids, relationShipIntervals);
        }
    }

    /**
     * Creates the edge for the relation "LivedIn".
     * @param personId id of the person from which the edge will start.
     */
    private void createLivedIn(int personId) {
        List<Integer> randomCitiesIds = randomCities();
        List<TimeInterval> intervals = TimeInterval.createRandomConsecutiveIntervals(persons.get(personId).getInterval(), randomCitiesIds.size());

        for (int i = 0; i < intervals.size(); i++) {
            int randomCityId = randomCitiesIds.get(i);
            Relationship relationship = new Relationship(RelationshipType.LIVED_IN, persons.get(personId),
                    cities.get(randomCityId), Collections.singletonList(intervals.get(i)));
            livedIn.add(relationship);
        }
    }

    /**
     * Selects a random amount of cities from the available ones
     * @return a list of cities
     */
    private List<Integer> randomCities() {
        int numberOfCitiesLivedIn = Utils.randomInteger(1, cities.size());
        List<Integer> cityIds = new ArrayList<>(cities.keySet());
        Collections.shuffle(cityIds);
        return cityIds.subList(0, numberOfCitiesLivedIn);
    }

    /**
     * Creates friendship edges for a person
     * @param person person to create friendships
     */
    private void createFriendships(final Person person, FriendFinder friendFinder) {
        final List<Person> randomFriends = friendFinder.getFriendsFor(person);

        for (Person friend : randomFriends) {
            final Optional<TimeInterval> timeInterval = TimeInterval.intersection(friend.getInterval(),
                    person.getInterval());

            if (timeInterval.isPresent()) {
                int totalNumberOfIntervals = Utils.randomInteger(1, maxFriendshipIntervals + 1);
                List<TimeInterval> intervalList = TimeInterval.createRandomDisjointIntervals(timeInterval.get(), totalNumberOfIntervals);
                createFriendship(person, friend, intervalList);
            } else {
                logger.error("Error creating friendship edge: empty interval. Skipping.");
            }
        }
    }

    private void createFriendship(Person firstPerson, Person secondPerson, List<TimeInterval> intervals) {
        Relationship friendshipEdge = new Relationship(RelationshipType.FRIEND, firstPerson, secondPerson, intervals);
        firstPerson.addFriend(secondPerson);
        secondPerson.addFriend(firstPerson);
        friendships.add(friendshipEdge);
    }

    /**
     * Creates fan edges for a brand
     * @param brand brand to create friendships
     */
    private void createFans(Brand brand) {
        final List<Integer> randomFans = randomFans();

        for (Integer fanId : randomFans) {
            Person fan = persons.get(fanId);
            final Optional<TimeInterval> timeInterval = TimeInterval.intersection(fan.getInterval(),
                    brand.getInterval());

            if (timeInterval.isPresent()) {
                int totalNumberOfIntervals = Utils.randomInteger(1, maxFansIntervals + 1);
                List<TimeInterval> intervalList = TimeInterval.createRandomDisjointIntervals(timeInterval.get(), totalNumberOfIntervals);
                Relationship fanRelationship = new Relationship(RelationshipType.FAN, fan, brand, intervalList);
                fans.add(fanRelationship);
            } else {
                logger.error("Error creating fan edge: empty interval. Skipping.");
            }
        }
    }

    private List<Integer> randomFans() {
        int numberOfFans = Utils.randomInteger(1, maxFans + 1);
        List<Integer> personIds = new ArrayList<>(persons.keySet());
        Collections.shuffle(personIds);
        return personIds.subList(0, numberOfFans);
    }

    private Person createPerson(TimeInterval t) {
        final int personId = getNextId();
        Person person = new Person(personId, t);
        persons.put(personId, person);
        createAttribute("Name", person, t);
        return person;
    }

    private void createBrand() {
        final TimeInterval t = new TimeInterval(TimeInterval.TIME_INTERVAL_MAX_YEAR);
        final int brandId = getNextId();
        Brand brand = new Brand(brandId, t);
        createAttribute("Name", brand, t);
        brands.put(brandId, brand);
    }

    private void createCity() {
        final TimeInterval t = new TimeInterval(TimeInterval.TIME_INTERVAL_MIN_YEAR, TimeInterval.TIME_INTERVAL_MAX_YEAR);
        final int cityId = getNextId();
        City city = new City(cityId, t);
        createAttribute("Name", city, t);
        cities.put(cityId, city);
    }

    public int getMaxFriendshipIntervals() {
        return maxFriendshipIntervals;
    }

    public int getPersonCount() {
        return personCount;
    }

    public int getMaxFriendships() {
        return maxFriendships;
    }

    public int getCityCount() {
        return cityCount;
    }


    public int getBrandCount() {
        return brandCount;
    }

    public int getMaxFans() {
        return maxFans;
    }

    public int getMaxFansIntervals() {
        return maxFansIntervals;
    }

    public List<CPathGenerationConfiguration> getCPathGenerationConfigurations() {
        return cPathGenerationConfigurations;
    }
}
