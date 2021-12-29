package bgu.spl.net.api.bidi;

import jdk.vm.ci.meta.Local;

import java.time.LocalDate;

public class User {
    private final String userName;
    private final String password;
    private final LocalDate birthday;
    private boolean LoggedIn;
    private int connectionId;

    public User(String userName, String password, LocalDate birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
        this.LoggedIn = false;
        this.connectionId = -1;
    }
    public String getUserName() {
        return userName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
    public boolean isLoggedIn()
    {
        return LoggedIn;
    }
    public boolean login(int connectionId)
    {
        if(LoggedIn)
            return false;
        LoggedIn=true;
        this.connectionId = connectionId;
        return true;
    }
    public boolean logout()
    {
        if(!LoggedIn)
            return false;
        LoggedIn=false;
        connectionId = -1;
        return true;
    }
    public int getConnectionId() {
        return connectionId;
    }
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}
