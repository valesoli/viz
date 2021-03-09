package ar.edu.itba.client;

import ar.edu.itba.population.models.cpath.CPathGenerationConfiguration;
import ar.edu.itba.population.models.socialnetwork.SocialNetwork;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocialNetworkConfigurationParserTest {

    @Mock
    private final PropertiesConfiguration propertiesConfiguration = mock(PropertiesConfiguration.class);

    @Test
    public void parse() {
        SocialNetworkConfigurationParser parser = new SocialNetworkConfigurationParser();
        mockProperties();
        SocialNetwork socialNetwork = (SocialNetwork) parser.parse(propertiesConfiguration);
        assertEquals(100, socialNetwork.getPersonCount());
        assertEquals(2, socialNetwork.getMaxFriendships());
        assertEquals(3, socialNetwork.getMaxFriendshipIntervals());
        assertEquals(3, socialNetwork.getCityCount());
        assertEquals(2, socialNetwork.getBrandCount());
        assertEquals(1, socialNetwork.getMaxFans());
        assertEquals(1, socialNetwork.getMaxFansIntervals());
        List<CPathGenerationConfiguration> cPathGenerationConfigurations = socialNetwork.getCPathGenerationConfigurations();
        assertCPathConfiguration(2, 6, cPathGenerationConfigurations.get(0));
        assertCPathConfiguration(3, 8, cPathGenerationConfigurations.get(1));
        assertCPathConfiguration(4, 10, cPathGenerationConfigurations.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCPathConfiguration() {
        SocialNetworkConfigurationParser parser = new SocialNetworkConfigurationParser();
        when(propertiesConfiguration.getList(Integer.class, "socialNetwork.cPath.numberOfPaths", new ArrayList<>())).thenReturn(Arrays.asList(2, 3));
        when(propertiesConfiguration.getList(Integer.class, "socialNetwork.cPath.minLength", new ArrayList<>())).thenReturn(Collections.singletonList(6));
        parser.parse(propertiesConfiguration);
    }

    private void assertCPathConfiguration(int numberOfPaths, int minimumLength, CPathGenerationConfiguration cPathGenerationConfiguration) {
        assertEquals(numberOfPaths, cPathGenerationConfiguration.getNumberOfPaths());
        assertEquals(minimumLength, cPathGenerationConfiguration.getMinimumLengthOfPath());
    }

    private void mockProperties() {
        when(propertiesConfiguration.getInt(eq("socialNetwork.persons"), anyInt())).thenReturn(100);
        when(propertiesConfiguration.getInt(eq("socialNetwork.maxFriendships"), anyInt())).thenReturn(2);
        when(propertiesConfiguration.getInt(eq("socialNetwork.maxFriendshipIntervals"), anyInt())).thenReturn(3);
        when(propertiesConfiguration.getInt(eq("socialNetwork.cityCount"), anyInt())).thenReturn(3);
        when(propertiesConfiguration.getInt(eq("socialNetwork.brandCount"), anyInt())).thenReturn(2);
        when(propertiesConfiguration.getInt(eq("socialNetwork.maxFans"), anyInt())).thenReturn(1);
        when(propertiesConfiguration.getInt(eq("socialNetwork.maxFansIntervals"), anyInt())).thenReturn(1);
        when(propertiesConfiguration.getList(Integer.class, "socialNetwork.cPath.numberOfPaths", new ArrayList<>())).thenReturn(Arrays.asList(2, 3, 4));
        when(propertiesConfiguration.getList(Integer.class, "socialNetwork.cPath.minLength", new ArrayList<>())).thenReturn(Arrays.asList(6, 8, 10));
    }
}