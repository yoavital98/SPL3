package bgu.spl.net.api.bidi;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserController {
    private final ConcurrentHashMap<String, User> users;
    private final ConcurrentHashMap<String, List<String>> followersOf;
    private final ConcurrentHashMap<String, List<String>> followingBy;
    private final ConcurrentHashMap<String, List<String>> blockedBy;
    private final ConcurrentHashMap<String, List<List<Message>>> messages; // 0 for posts, 1 for privates
    private final List<String> filter;

    public UserController() {
        this.users = new ConcurrentHashMap<>();
        this.followersOf = new ConcurrentHashMap<>();
        this.followingBy = new ConcurrentHashMap<>();
        this.blockedBy = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.filter = new ArrayList<>();
    }

    public boolean regiser(String userName, String password, String birthday) {
        if (users.containsKey(userName))
            return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD-MM-YYYY");
        LocalDate birth = LocalDate.parse(birthday, formatter);
        users.put(userName, new User(userName, password, birth));
        List<List<Message>> messages = new ArrayList<>();
        messages.add(new ArrayList<>());
        messages.add(new ArrayList<>());
        this.followersOf.put(userName, new ArrayList<>());
        this.followingBy.put(userName, new ArrayList<>());
        this.blockedBy.put(userName, new ArrayList<>());
        this.messages.put(userName, messages);
        return true;
    }

    public boolean login(String userName, String password) {
        if (!users.containsKey(userName))
            return false;
        User user = users.get(userName);
        if (!user.validatePassword(password))
            return false;
        return user.login();
    }

    public boolean logout(String userName) {
        if (!users.containsKey(userName))
            return false;
        return users.get(userName).logout();
    }

    public boolean follow(String userFollowing, String userFollowed) {
        if (!users.containsKey(userFollowing) || !users.containsKey(userFollowed))
            return false;
        else if (!users.get(userFollowing).isLoggedIn())
            return false;
        else if (!followingBy.containsKey(userFollowing) || !followersOf.containsKey(userFollowed))
            return false;
        else if (followingBy.get(userFollowing).contains(users.get(userFollowed)) || followersOf.get(userFollowed).contains(users.get(userFollowing)))
            return false;
        else if (blockedBy.get(userFollowing).contains(userFollowed))
            return false;
        followingBy.get(userFollowing).add(userFollowed);
        followersOf.get(userFollowed).add(userFollowing);
        return true;
    }

    public boolean unFollow(String userFollowing, String userFollowed) {
        if (!users.containsKey(userFollowing) || !users.containsKey(userFollowed))
            return false;
        else if (!users.get(userFollowing).isLoggedIn())
            return false;
        else if (!followingBy.containsKey(userFollowing) || !followersOf.containsKey(userFollowed))
            return false;
        else if (!followingBy.get(userFollowing).contains(users.get(userFollowed)) || !followersOf.get(userFollowed).contains(users.get(userFollowing)))
            return false;
        followingBy.get(userFollowing).remove(users.get(userFollowed));
        followersOf.get(userFollowed).remove(users.get(userFollowing));
        return true;
    }

    public boolean postMessage(String userName, String message) {
        if (!users.containsKey(userName))
            return false;
        if (!users.get(userName).isLoggedIn())
            return false;
        String messageCopy = message;
        List<String> usersMentioned = new ArrayList<>();
        int index;
        Character[] delimiter = {' ', '.', ';', ',', ':', '@'};
        List<Character> delimiters = new ArrayList<Character>(Arrays.asList(delimiter));
        while (messageCopy != null && messageCopy.indexOf("@") != -1) {
            index = messageCopy.indexOf("@");
            messageCopy = messageCopy.substring(index + 1, messageCopy.length());
            int endIndex = -1;
            String userFound;
            for (int i = 0; i < messageCopy.length(); i++) {
                if (delimiters.contains(messageCopy.charAt(i))) {
                    endIndex = i;
                    break;
                }
            }
            if (endIndex != -1) {
                userFound = messageCopy.substring(0, endIndex);
                usersMentioned.add(userFound);
                messageCopy = messageCopy.substring(endIndex, messageCopy.length());
            } else {
                usersMentioned.add(messageCopy);
                messageCopy = null;
            }
        }
        List<String> usersRecipients = new ArrayList<>();
        for (String userMentioned : usersMentioned) {
            if (users.containsKey(userMentioned))
                usersRecipients.add(userMentioned);
        }
        usersRecipients.addAll(followersOf.get(userName)); //cool method they suggeested
        messages.get(userName).get(0).add(new PostMessage(usersRecipients, message, userName));
        return true;
    }

    public boolean privateMessage(String userName, String userRecipient, String message, String date) {
        if (!users.containsKey(userName) || !users.containsKey(userRecipient))
            return false;
        else if (!followingBy.containsKey(userName) || !messages.containsKey(userName))
            return false;
        else if (!followingBy.get(userName).contains(users.get(userRecipient)))
            return false;
        for (String filter : filter) {
            message = message.replaceAll(filter, "<filtered>");
        }
        messages.get(userName).get(1).add(new PrivateMessage(message, users.get(userName), users.get(userRecipient), date));
        return true;
    }

    public List<Short[]> log(String userRequested) {
        if (!users.containsKey(userRequested))
            return null;
        if (!users.get(userRequested).isLoggedIn())
            return null;
        List<Short[]> log = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isLoggedIn()) {
                log.add(getData(user));
            }
        }
        return log;
    }

    public List<Short[]> stat(String userRequested, List<String> usersList) {
        if (!users.containsKey(userRequested))
            return null;
        if (!users.get(userRequested).isLoggedIn())
            return null;
        List<Short[]> log = new ArrayList<>();
        for (String userName : usersList) {
            if (!users.containsKey(userName))
                return null;
            User user = users.get(userName);
            log.add(getData(user));
        }
        return log;
    }

    private Short[] getData(User user) {
        LocalDate birthday;
        short age;
        short numberOfPosts;
        short numberOfFollowing;
        short numberOfFollowers;
        birthday = user.getBirthday();
        age = (short) Period.between(birthday, LocalDate.now()).getYears();
        numberOfPosts = (short) messages.get(user.getUserName()).get(0).size();
        numberOfFollowing = (short) followingBy.get(user.getUserName()).size();
        numberOfFollowers = (short) followersOf.get(user.getUserName()).size();
        Short[] logStat = new Short[6];
        logStat[0] = 10;
        logStat[1] = 7;
        logStat[2] = age;
        logStat[3] = numberOfPosts;
        logStat[4] = numberOfFollowing;
        logStat[5] = numberOfFollowers;
        return logStat;
    }

    public boolean block(String userName, String blockedUserName) {
        if (!users.containsKey(userName) || !users.containsKey(blockedUserName))
            return false;
        if (!blockedBy.get(userName).contains(blockedUserName)) { //if not already blocked
            blockedBy.get(userName).add(blockedUserName);
            if (followersOf.get(userName).contains(blockedUserName))
                followersOf.get(userName).remove(blockedUserName);
            if (followersOf.get(blockedUserName).contains(userName))
                followersOf.get(blockedUserName).remove(userName);
            if (followingBy.get(userName).contains(blockedUserName))
                followingBy.get(userName).remove(blockedUserName);
            if (followingBy.get(blockedUserName).contains(userName))
                followingBy.get(blockedUserName).remove(userName);
            return true;
        }
        return false;
    }
}
