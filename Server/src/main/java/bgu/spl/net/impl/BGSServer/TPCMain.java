package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args){
        ConnectionsController<Operation> connectionsController=new ConnectionsController<>();
        UserController userController=new UserController(connectionsController);
        try(Server<Operation> server=Server.threadPerClient(7777,()->new BGSProtocol(userController), OperationEncoderDecoder::new, connectionsController);){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
