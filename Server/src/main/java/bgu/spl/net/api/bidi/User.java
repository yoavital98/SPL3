package bgu.spl.net.api.bidi;

import jdk.vm.ci.meta.Local;

import java.time.LocalDate;

public class User {
    private final String userName;
    private final String password;
    private final LocalDate birthday;
    private boolean LoggedIn;

    public User(String userName, String password, LocalDate birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
        this.LoggedIn = false;
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
    public boolean login()
    {
        if(LoggedIn)
            return false;
        LoggedIn=true;
        return true;
    }
    public boolean logout()
    {
        if(!LoggedIn)
            return false;
        LoggedIn=false;
        return true;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}
