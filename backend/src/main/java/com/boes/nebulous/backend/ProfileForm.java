package com.boes.nebulous.backend;

public class ProfileForm {

    private String displayName;
    private TeeShirtSize teeShirtSize;

    private ProfileForm() {}

    public ProfileForm(String displayName, TeeShirtSize teeShirtSize) {
        this.displayName = displayName;
        this.teeShirtSize = teeShirtSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TeeShirtSize getTeeShirtSize() {
        return teeShirtSize;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setTeeShirtSize(TeeShirtSize teeShirtSize) {
        this.teeShirtSize = teeShirtSize;
    }

    public static enum TeeShirtSize {
        NOT_SPECIFIED,
        XS,
        S,
        M,
        L,
        XL,
        XXL,
        XXXL
    }

}
