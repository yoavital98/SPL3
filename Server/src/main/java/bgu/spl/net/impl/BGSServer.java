package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.Server;

public class BGSServer {
public static void main(String[] args){
/*
    TPCMain(args);
*/
    ReactorMain(args);
}
    public static void TPCMain(String[] args){
        ConnectionsController<Operation> connectionsController=new ConnectionsController<>();
        UserController userController=new UserController(connectionsController);
        try(Server<Operation> server=Server.threadPerClient(7777,()->new BGSProtocol(userController), OperationEncoderDecoder::new, connectionsController);){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void ReactorMain(String[] args) {
        ConnectionsController<Operation> connectionsController = new ConnectionsController<>();
        UserController userController = new UserController(connectionsController);
        try (Server<Operation> server = Server.reactor(Integer.parseInt(args[1]), Integer.parseInt(args[0]), () -> new BGSProtocol(userController), OperationEncoderDecoder::new, connectionsController);) {
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
