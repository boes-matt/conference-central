package com.boes.nebulous.backend;

import java.util.Date;
import java.util.List;

public class ConferenceForm {

    private String name;
    private String description;
    private List<String> topics;
    private String city;
    private Date startDate;
    private Date endDate;
    private int maxAttendees;

    private ConferenceForm() {}

    public ConferenceForm(String name, String description,
                          List<String> topics, String city,
                          Date startDate, Date endDate,
                          int maxAttendees) {
        this.name = name;
        this.description = description;
        this.topics = topics;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxAttendees = maxAttendees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

}
