package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.Operations.ServerOperations.NotificationOperation;

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
    private final ConcurrentHashMap<String, List<List<Message>>> receivedMessages; // 0 for posts, 1 for privates
    private final List<String> filter;
    private final Connections<Operation> connections;

    public UserController(Connections<Operation> connections) {
        this.users = new ConcurrentHashMap<>();
        this.followersOf = new ConcurrentHashMap<>();
        this.followingBy = new ConcurrentHashMap<>();
        this.blockedBy = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.receivedMessages = new ConcurrentHashMap<>();
        this.filter = new ArrayList<>();
        this.connections = connections;
    }

    public boolean regiser(String userName, String password, String birthday) {
        if (users.containsKey(userName))
            return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birth = LocalDate.parse(birthday, formatter);
        users.put(userName, new User(userName, password, birth));
        List<List<Message>> messages = new ArrayList<>();
        messages.add(new ArrayList<>());
        messages.add(new ArrayList<>());
        List<List<Message>> receivedMessages = new ArrayList<>();
        receivedMessages.add(new ArrayList<>());
        receivedMessages.add(new ArrayList<>());
        this.followersOf.put(userName, new ArrayList<>());
        this.followingBy.put(userName, new ArrayList<>());
        this.blockedBy.put(userName, new ArrayList<>());
        this.messages.put(userName, messages);
        this.receivedMessages.put(userName, receivedMessages);
        return true;
    }

    public boolean login(String userName, String password, int connectionId) {
        if (!users.containsKey(userName))
            return false;
        User user = users.get(userName);
        if (!user.validatePassword(password))
            return false;
        if(user.login(connectionId))
        {
            for(Message message: receivedMessages.get(userName).get(0))
                if(!((PostMessage)message).isReceived(userName))
                {
                    connections.send(users.get(userName).getConnectionId(), new NotificationOperation((short)9,(byte)1,((PostMessage)message).getSender(),((PostMessage)message).getMessage()));
                    ((PostMessage)message).messageReceived(userName);
                }
            for(Message message: receivedMessages.get(userName).get(1))
                if(!((PrivateMessage)message).isReceived())
                {
                    connections.send(users.get(userName).getConnectionId(), new NotificationOperation((short)9,(byte)0,((PrivateMessage)message).getSender().getUserName(),((PrivateMessage)message).getMessage()));
                    ((PrivateMessage)message).messageReceived();
                }
            return true;
        }
        return false;
    }

    public boolean logout(String userName) {
        if (!users.containsKey(userName))
            return false;
        return users.get(userName).logout();
    }

    public boolean followOrUnfollow(int folOrUnfol, String userFollowing, String userFollowed){
        if(folOrUnfol == 0)
            return follow(userFollowing, userFollowed);
        else
            return unFollow(userFollowing, userFollowed);
    }

    private boolean follow(String userFollowing, String userFollowed) {
        if (!users.containsKey(userFollowing) || !users.containsKey(userFollowed))
            return false;
        else if (!users.get(userFollowing).isLoggedIn())
            return false;
        else if (!followingBy.containsKey(userFollowing) || !followersOf.containsKey(userFollowed))
            return false;
        else if (followingBy.get(userFollowing).contains(userFollowed) || followersOf.get(userFollowed).contains(userFollowing))
            return false;
        else if (blockedBy.get(userFollowing).contains(userFollowed))
            return false;
        followingBy.get(userFollowing).add(userFollowed);
        followersOf.get(userFollowed).add(userFollowing);
        return true;
    }

    private boolean unFollow(String userFollowing, String userFollowed) {
        if (!users.containsKey(userFollowing) || !users.containsKey(userFollowed))
            return false;
        else if (!users.get(userFollowing).isLoggedIn())
            return false;
        else if (!followingBy.containsKey(userFollowing) || !followersOf.containsKey(userFollowed))
            return false;
        else if (!followingBy.get(userFollowing).contains(userFollowed) || !followersOf.get(userFollowed).contains(userFollowing))
            return false;
        followingBy.get(userFollowing).remove(userFollowed);
        followersOf.get(userFollowed).remove(userFollowing);
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
        while (messageCopy != null && messageCopy.contains("@")) {
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
        for (String follower : followersOf.get(userName)) {
            if (!usersRecipients.contains(follower))
                usersRecipients.add(follower);
        }
        PostMessage userMessage = new PostMessage(usersRecipients, message, userName);
        messages.get(userName).get(0).add(userMessage);
        for( String recipientName : usersRecipients){
            receivedMessages.get(recipientName).get(0).add(userMessage);
            if(users.get(recipientName).isLoggedIn()){
                connections.send(users.get(recipientName).getConnectionId(), new NotificationOperation((short)9,(byte)1,userName,userMessage.getMessage()));
                userMessage.messageReceived(recipientName);
            }
        }
        return true;
    }

    public boolean sendPrivateMessage(String userName, String userRecipient, String message, String date) {
        if (!users.containsKey(userName) || !users.containsKey(userRecipient))
            return false;
        else if (!followingBy.containsKey(userName) || !messages.containsKey(userName))
            return false;
        else if (!followingBy.get(userName).contains(userRecipient))
            return false;
        for (String filter : filter) {
            message = message.replaceAll(filter, "<filtered>");
        }
        PrivateMessage pm = new PrivateMessage(message, users.get(userName), users.get(userRecipient), date);
        messages.get(userName).get(1).add(pm);
        receivedMessages.get(userRecipient).get(1).add(pm);
        if(users.get(userRecipient).isLoggedIn())
        {
            connections.send(users.get(userRecipient).getConnectionId(), new NotificationOperation((short)9,(byte)0,userName,pm.getMessage()));
            pm.messageReceived();
        }
        return true;
    }

    public List<String> logstat(String userRequested) {
        if (!users.containsKey(userRequested))
            return null;
        if (!users.get(userRequested).isLoggedIn())
            return null;
        List<String> log = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isLoggedIn() && !blockedBy(userRequested, user.getUserName())) {
                log.add(getData(user));
            }
        }
        return log;
    }

    private boolean blockedBy(String userRequested, String userBlocking) {
        return blockedBy.get(userBlocking).contains(userRequested);
    }

    public List<String> stat(String userRequested, List<String> usersList) {
        if (!users.containsKey(userRequested))
            return null;
        if (!users.get(userRequested).isLoggedIn())
            return null;
        List<String> log = new ArrayList<>();
        for (String userName : usersList) {
            if (!users.containsKey(userName))
                return null;
            if(!blockedBy(userRequested, userName)) {
                User user = users.get(userName);
                log.add(getData(user));
            }
        }
        return log;
    }

    private String getData(User user) {
        LocalDate birthday;
        int age;
        int numberOfPosts;
        int numberOfFollowing;
        int numberOfFollowers;
        birthday = user.getBirthday();
        age = Period.between(birthday, LocalDate.now()).getYears();
        numberOfPosts = messages.get(user.getUserName()).get(0).size();
        numberOfFollowing = followingBy.get(user.getUserName()).size();
        numberOfFollowers = followersOf.get(user.getUserName()).size();
        return age+" "+numberOfPosts+" "+numberOfFollowing +" "+numberOfFollowers;
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
