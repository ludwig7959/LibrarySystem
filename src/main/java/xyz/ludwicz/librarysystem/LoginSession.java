package xyz.ludwicz.librarysystem;

public class LoginSession {

    private String id;
    private String name;
    private String phoneNumber;

    public LoginSession(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
