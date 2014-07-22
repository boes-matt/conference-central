package com.boes.nebulous.backend;

import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotDefault;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Conference {

    private static final String DEFAULT_CITY = "Default City";
    private static final List<String> DEFAULT_TOPICS = Arrays.asList("Default", "Topic");

    @Id private long id;
    @Index private String name;
    private String description;
    @Parent private Key<Profile> profileKey;
    private String organizerEmail;
    @Index private List<String> topics;
    @Index(IfNotDefault.class) private String city = DEFAULT_CITY;
    private Date startDate;
    private Date endDate;
    @Index private int month;
    @Index private int maxAttendees;
    @Index private int seatsAvailable;

    private Conference() {}

    public Conference(long id, String organizerEmail, ConferenceForm form) {
        Preconditions.checkNotNull(form.getName(), "The name is required");
        this.id = id;
        this.name = form.getName();
        this.description = form.getDescription();
        this.profileKey = Key.create(Profile.class, organizerEmail);
        this.organizerEmail = organizerEmail;
        this.topics = form.getTopics() == null || form.getTopics().isEmpty() ? DEFAULT_TOPICS : form.getTopics();
        this.city = form.getCity() == null || form.getCity().isEmpty() ? DEFAULT_CITY : form.getCity();
        this.startDate = form.getStartDate();
        this.endDate = form.getEndDate();
        if (startDate != null) setMonth(startDate);
        this.maxAttendees = form.getMaxAttendees();
        this.seatsAvailable = maxAttendees;
    }

    private void setMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.month = calendar.get(Calendar.MONTH) + 1;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getCity() {
        return city;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getMonth() {
        return month;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

}
