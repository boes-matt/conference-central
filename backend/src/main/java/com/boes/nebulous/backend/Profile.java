package com.boes.nebulous.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Profile {

    private @Id String email;
    private String displayName;
    private ProfileForm.TeeShirtSize teeShirtSize;

    private Profile() {}

    public Profile(String email, String displayName, ProfileForm.TeeShirtSize teeShirtSize) {
        this.email = email;
        this.displayName = displayName;
        this.teeShirtSize = teeShirtSize;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProfileForm.TeeShirtSize getTeeShirtSize() {
        return teeShirtSize;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setTeeShirtSize(ProfileForm.TeeShirtSize teeShirtSize) {
        this.teeShirtSize = teeShirtSize;
    }

}
