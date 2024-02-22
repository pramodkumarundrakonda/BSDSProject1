# TCP/UDP Client-Server Application

This repository hosts a simple yet versatile TCP/UDP client-server application implemented in Java. The application enables clients to interact with a server by sending requests and receiving responses over TCP or UDP protocols.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Usage](#usage)
    - [Compilation](#compilation)
    - [Running the Server](#running-the-server)
    - [Running the Client](#running-the-client)
- [File Structure](#file-structure)
- [Dependencies](#dependencies)
- [Makefile](#makefile)
- [Contributors](#contributors)
- [License](#license)

## Overview

The application comprises two primary components:

- **Server**: Manages incoming client requests, processes them, and sends appropriate responses.
- **Client**: Sends requests to the server and displays the received responses.

## Features

- **TCP and UDP Support**: The server accommodates both TCP and UDP protocols, allowing clients to opt for their preferred mode of communication.
- **KeyValue Store**: The server maintains a key-value store where clients can perform operations like PUT, GET, and DELETE on key-value pairs.
- **Checksum Validation**: Both the client and server conduct checksum validation to ensure data integrity during transmission.

## Usage

### Compilation

1. Ensure that you have the Java Development Kit (JDK) installed on your system.
2. Utilize the provided Makefile to compile the source code.
   ```bash
   make all
   ```

### Running the Server

Execute the following command to run the server:

- For TCP server:
  ```bash
  make run-server ARGS="port tcp"
  ```
- For UDP server:
  ```bash
  make run-server ARGS="port udp"
  ```

Replace `port` with the desired port number for the server.

### Running the Client

Run the client using the following command:

- For TCP client:
  ```bash
  make run-client ARGS="serverHost port tcp"
  ```
- For UDP client:
  ```bash
  make run-client ARGS="serverHost port udp"
  ```

Replace `serverHost` with the server's hostname or IP address, and `port` with the server's port number.

## File Structure

- **src/server**: Contains server-side Java source files.
- **src/client**: Contains client-side Java source files.
- **logs**: Directory for storing log files.
- **resources**: Directory for storing resource files.

## Dependencies

- The application relies on the Apache Log4j library for logging purposes.
- Ensure that the Log4j JAR files are included in the `lib` directory.

## Makefile

- The Makefile provides targets for compiling and executing the server and client applications.
- Targets include `all`, `server`, `client`, `run-server`, `run-client`, and `clean`.

## Contributors

- Pramod Kumar Undrakonda

