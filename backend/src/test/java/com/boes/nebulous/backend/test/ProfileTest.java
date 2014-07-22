package com.boes.nebulous.backend.test;

import com.boes.nebulous.backend.Profile;
import com.boes.nebulous.backend.ProfileForm;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    private final LocalDatastoreServiceTestConfig dataStoreConfig =
            new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100);
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(dataStoreConfig);

    private Profile profile;
    private final String email = "example@gmail.com";
    private final String displayName = "example";
    private final ProfileForm.TeeShirtSize teeShirtSize = ProfileForm.TeeShirtSize.M;

    @Before
    public void setUp() {
        helper.setUp();
        profile = new Profile(email, displayName, teeShirtSize);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGetters() {
        Assert.assertEquals("Wrong email", email, profile.getEmail());
        Assert.assertEquals("Wrong display name", displayName, profile.getDisplayName());
        Assert.assertEquals("Wrong tee shirt size", teeShirtSize, profile.getTeeShirtSize());
    }

}
