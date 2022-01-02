#include <stdlib.h>
#include <connectionHandler.h>
#include <encoderDecoder.h>
#include <thread>
#include <atomic>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
void Server2Client(ConnectionHandler& connectionHandler, EncoderDecoder& encoderDecoder, std::atomic<bool>& run)
{
    while(run)
    {
        int len;
        std::string answer;
        if(encoderDecoder.Decode(answer))
        {
            std::cout << answer << std::endl;
            if(answer=="Successfully logged out")
                run = false;
        }
        else
        {
            std::cout << "Message passing from server failed, disconnecting" << std::endl;
            run=false;
            break;
        }
    }
}
void Client2Server(ConnectionHandler& connectionHandler,EncoderDecoder& encoderDecoder, std::atomic<bool>& run) {
    while (run) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        char bytesToSend[bufsize];
        int length;
        if(encoderDecoder.Encode(line, bytesToSend, length)) {
            if (!connectionHandler.sendBytes(bytesToSend, length)) {
                std::cout << "Lost Connection With The Server, Shutting Down!\n" << std::endl;
                run=false;
                break;
            }
        }
        else{
            std::cout << "Invalid Command, Please Try Again!" << std::endl;
        }
    }
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::atomic<bool> terminate_Client(true);
    EncoderDecoder encoderDecoder;
    std::thread server2ClientThread(&Server2Client, std::ref(connectionHandler),std::ref(encoderDecoder), std::ref(terminate_Client));
    std::thread client2ServerThread(&Client2Server, std::ref(connectionHandler),std::ref(encoderDecoder), std::ref(terminate_Client));
    server2ClientThread.join();
    client2ServerThread.join();
    return 0;
}
