package hr.ferit.iveselin.studentvozi.model;


public enum RideType {
    OFFER("Ponuda"),
    REQUEST("Potražnja");

    private String displayName;

    RideType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
