cmake_minimum_required(VERSION 3.20)
project(Client)
include_directories(C:\\boost\\include\\boost-1_66)
include_directories(include)
link_directories(C:\\boost\\lib)
add_definitions(-DBOOST_ERROR_CODE_HEADER_ONLY)
link_libraries(ws2_32 wsock32)
set(CMAKE_CXX_STANDARD 14)
add_executable(Client src/connectionHandler.cpp
        src/client.cpp
        src/encoderDecoder.cpp
        )