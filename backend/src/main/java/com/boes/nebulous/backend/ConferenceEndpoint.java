package com.boes.nebulous.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import java.util.List;

import static com.boes.nebulous.backend.OfyService.factory;
import static com.boes.nebulous.backend.OfyService.ofy;

import javax.inject.Named;

@Api(name = "conference",
        description = "API for the Conference Central Backend application.",
        version = "v1",
        scopes = { Constants.EMAIL_SCOPE },
        clientIds = { Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
        audiences = { Constants.ANDROID_AUDIENCE },
        namespace = @ApiNamespace(ownerDomain = "backend.nebulous.boes.com", ownerName = "backend.nebulous.boes.com", packagePath=""))
public class ConferenceEndpoint {

    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);
        return response;
    }

    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.POST)
    public Profile saveProfile(User user, ProfileForm form) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("Authorization required");

        Profile profile = getProfileFromDataStore(user);
        if (profile != null) updateProfile(profile, form);
        else profile = createProfile(user, form);

        ofy().save().entity(profile).now();
        return profile;
    }

    @ApiMethod(name = "getProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.GET)
    public Profile getProfile(User user) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("Authorization required");
        else return getProfileFromDataStore(user);
    }

    @ApiMethod(name = "createConference", path = "conference", httpMethod = ApiMethod.HttpMethod.POST)
    public Conference createConference(User user, ConferenceForm form) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("Authorization required");

        Profile profile = getProfileFromDataStore(user);
        if (profile == null) profile = saveProfile(user, new ProfileForm(null, null));

        Key<Conference> conferenceKey = factory().allocateId(profile, Conference.class);
        Conference conference = new Conference(conferenceKey.getId(), user.getEmail(), form);

        ofy().save().entity(conference).now();

        return conference;
    }

    @ApiMethod(name = "queryConferences", path = "queryConferences", httpMethod = ApiMethod.HttpMethod.POST)
    public List<Conference> queryConferences() {
        Query<Conference> query = ofy().load().type(Conference.class).order("name");
        return query.list();
    }

    @ApiMethod(name = "getConferencesCreated", path = "getConferencesCreated", httpMethod = ApiMethod.HttpMethod.POST)
    public List<Conference> getConferencesCreated(User user) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("Authorization required");

        Key<Profile> profileKey = Key.create(Profile.class, user.getEmail());
        Query<Conference> query = ofy().load().type(Conference.class).ancestor(profileKey).order("name");
        return query.list();
    }

    public List<Conference> filterPlayground() {
        Query<Conference> query = ofy().load().type(Conference.class).order("name");
        query = query.filter("city", "London");
        query = query.filter("topics", "Medical Innovations");
        query = query.filter("month", 6);

        return query.list();
    }

    private static String extractDisplayName(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    private Profile getProfileFromDataStore(User user) {
        Key<Profile> key = Key.create(Profile.class, user.getEmail());
        return ofy().load().key(key).now();
    }

    private void updateProfile(Profile profile, ProfileForm form) {
        String displayName = form.getDisplayName();
        ProfileForm.TeeShirtSize teeShirtSize = form.getTeeShirtSize();

        if (!isEmpty(displayName)) profile.setDisplayName(displayName);
        if (teeShirtSize != null) profile.setTeeShirtSize(teeShirtSize);
    }

    private Profile createProfile(User user, ProfileForm form) {
        String email = user.getEmail();
        String displayName =
                !isEmpty(form.getDisplayName()) ? form.getDisplayName() : extractDisplayName(email);
        ProfileForm.TeeShirtSize teeShirtSize =
                form.getTeeShirtSize() != null ? form.getTeeShirtSize() : ProfileForm.TeeShirtSize.NOT_SPECIFIED;

        return new Profile(email, displayName, teeShirtSize);
    }

    private boolean isEmpty(String s) {
        if (s == null) return true;
        else if (s.trim().isEmpty()) return true;
        else return false;
    }

}