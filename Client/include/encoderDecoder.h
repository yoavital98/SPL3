#ifndef CLIENT_ENCODERDECODER_H
#define CLIENT_ENCODERDECODER_H

#include <string>
#include <iostream>
#include <vector>
#include <sstream>
#include <chrono>
#include <ctime>
#include <iomanip>

class EncoderDecoder {
private:
    bool registerEncode(std::vector<std::string> wordList, char bytes[], int& length);
    bool loginEncode(std::vector<std::string> wordList, char bytes[], int& length);
    bool logoutEncode(std::string line, char bytes[], int& length);
    bool followEncode(std::vector<std::string> wordList, char bytes[], int& length);
    bool postEncode(std::string content, char bytes[], int& length);
    bool pmEncode(std::string username, std::string content, char bytes[], int& length);
    bool logstatEncode(std::string line, char bytes[], int& length);
    bool statEncode(std::string content, char bytes[], int& length);
    bool blockEncode(std::string username, char bytes[], int& length);
    short bytesToShort(char* bytesArr);

public:
    EncoderDecoder();

    ~EncoderDecoder();

    bool Encode(std::string line, char bytes[], int &length);

    bool Decode(std::string& line, std::vector<char> messageToDecode);

    //enum enumTranslator(std::string const& str);



};

#endif //CLIENT_ENCODERDECODER_H
