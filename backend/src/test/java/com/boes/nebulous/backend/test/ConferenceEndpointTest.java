package com.boes.nebulous.backend.test;

import com.boes.nebulous.backend.ConferenceEndpoint;
import com.boes.nebulous.backend.Profile;
import com.boes.nebulous.backend.ProfileForm;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.boes.nebulous.backend.OfyService.ofy;

public class ConferenceEndpointTest {

    private final LocalDatastoreServiceTestConfig dataStoreConfig =
            new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100);
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(dataStoreConfig);

    private ConferenceEndpoint conferenceApi;

    private User user;
    private String email = "example@gmail.com";
    private String domain = "gmail.com";

    private String displayName = "Matt";
    private ProfileForm.TeeShirtSize teeShirtSize = ProfileForm.TeeShirtSize.M;

    @Before
    public void setUp() {
        helper.setUp();
        conferenceApi = new ConferenceEndpoint();
        user = new User(email, domain);
    }

    @After
    public void tearDown() {
        ofy().clear();
        helper.tearDown();
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetProfileWithoutUser() throws Exception {
        conferenceApi.getProfile(null);
    }

    @Test
    public void testGetProfileFirstTime() throws Exception {
        Key<Profile> profileKey = Key.create(Profile.class, user.getEmail());

        Profile profile = ofy().load().key(profileKey).now();
        Assert.assertNull("User profile does not exist yet", profile);

        profile = conferenceApi.getProfile(user);
        Assert.assertNull("User profile does not exist yet", profile);
    }

    @Test
    public void testSaveProfile() throws Exception {
        ProfileForm form = new ProfileForm(displayName, teeShirtSize);
        Profile profile = conferenceApi.saveProfile(user, form);

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", displayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", teeShirtSize, profile.getTeeShirtSize());

        Key<Profile> profileKey = Key.create(Profile.class, user.getEmail());
        profile = ofy().load().key(profileKey).now();

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", displayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", teeShirtSize, profile.getTeeShirtSize());
    }

    @Test
    public void testSaveProfileWithNulls() throws Exception {
        ProfileForm form = new ProfileForm(null, null);
        Profile profile = conferenceApi.saveProfile(user, form);

        String defaultDisplayName = email.substring(0, email.indexOf("@"));

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", defaultDisplayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", ProfileForm.TeeShirtSize.NOT_SPECIFIED, profile.getTeeShirtSize());

        Key<Profile> profileKey = Key.create(Profile.class, user.getEmail());
        profile = ofy().load().key(profileKey).now();

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", defaultDisplayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", ProfileForm.TeeShirtSize.NOT_SPECIFIED, profile.getTeeShirtSize());
    }

    @Test
    public void testGetProfile() throws Exception {
        ProfileForm form = new ProfileForm(displayName, teeShirtSize);
        conferenceApi.saveProfile(user, form);

        Profile profile = conferenceApi.getProfile(user);

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", displayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", teeShirtSize, profile.getTeeShirtSize());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(displayName, teeShirtSize));

        String updatedDisplayName = "Jeff";
        ProfileForm.TeeShirtSize updatedTeeShirtSize = ProfileForm.TeeShirtSize.L;
        conferenceApi.saveProfile(user, new ProfileForm(updatedDisplayName, updatedTeeShirtSize));

        Profile profile = conferenceApi.getProfile(user);

        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", updatedDisplayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", updatedTeeShirtSize, profile.getTeeShirtSize());
    }

    @Test
    public void testUpdateProfileWithNulls() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(displayName, teeShirtSize));
        conferenceApi.saveProfile(user, new ProfileForm(null, null));

        Profile profile = conferenceApi.getProfile(user);

        // Expected behavior is that the existing properties do not get overwritten
        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", displayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", teeShirtSize, profile.getTeeShirtSize());
    }

}
