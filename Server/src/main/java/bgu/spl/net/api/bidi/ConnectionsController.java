package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsController<T> implements Connections<T>{
    private final ConcurrentHashMap<Integer, ConnectionHandler<T>> connections;
    int connectionCounter;

    public ConnectionsController() {
        this.connections = new ConcurrentHashMap<>();
        this.connectionCounter = 0;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!connections.containsKey(connectionId))
            return false;
        connections.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for(Map.Entry<Integer, ConnectionHandler<T>> entry : connections.entrySet())
            entry.getValue().send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
            connections.remove(connectionId);
    }
    public int add(ConnectionHandler<T> connectionHandler)
    {
        connections.put(connectionCounter, connectionHandler);
        return connectionCounter++;
    }
}
