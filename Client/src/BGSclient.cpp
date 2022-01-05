#include <stdlib.h>
#include <connectionHandler.h>
#include <encoderDecoder.h>
#include <thread>
#include <atomic>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
void Server2Client(ConnectionHandler& connectionHandler, EncoderDecoder& encoderDecoder, std::atomic<bool>& run, std::condition_variable& cv, std::unique_lock<std::mutex>& lck)
{
    while(run)
    {
        std::string answer;
        std::vector<char> charVector;
        char ch;
        try {
            do{
                connectionHandler.getBytes(&ch, 1);
                charVector.push_back(ch);
            }while (';' != ch);
        } catch (std::exception& e) {
            std::cerr << "Lost Connection With The Server. Disconnecting..." << std::endl;
            run = false;
            break;
        }
        if(encoderDecoder.Decode(answer, charVector)) {
            std::cout << answer << std::endl;
            if (answer == "ACK 3 Logged out Successfully") {
            run = false;
            cv.notify_all();
        }
            if(answer == "ERROR 3")
                cv.notify_all();
        }
        else
        {
            std::cout << "Message passing from server failed, disconnecting" << std::endl;
            run=false;
            break;
        }
    }
}
void Client2Server(ConnectionHandler& connectionHandler,EncoderDecoder& encoderDecoder, std::atomic<bool>& run, std::condition_variable& cv, std::unique_lock<std::mutex>& lck) {
    while (run) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        char bytesToSend[bufsize];
        int length;
        if(encoderDecoder.Encode(line, bytesToSend, length)) {
            std::string output(bytesToSend);
            if (!connectionHandler.sendBytes(bytesToSend, length)) {
                std::cout << "Lost Connection With The Server, Shutting Down!\n" << std::endl;
                run=false;
                break;
            }
        }
        else{
            std::cout << "Invalid Command, Please Try Again!" << std::endl;
        }
        if(line=="LOGOUT")
            cv.wait(lck);
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
    std::condition_variable cv;
    std::mutex mtx;
    std::unique_lock<std::mutex> lck(mtx);
    std::atomic<bool> terminate_Client(true);
    EncoderDecoder encoderDecoder;
    std::thread server2ClientThread(&Server2Client, std::ref(connectionHandler),std::ref(encoderDecoder), std::ref(terminate_Client), std::ref(cv), std::ref(lck));
    std::thread client2ServerThread(&Client2Server, std::ref(connectionHandler),std::ref(encoderDecoder), std::ref(terminate_Client), std::ref(cv), std::ref(lck));
    server2ClientThread.join();
    client2ServerThread.join();
    return 0;
}