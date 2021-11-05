package com.javainuse.websocket.config;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class WebSocket {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        ServerSocket server = new ServerSocket(8080);
        try {
            log.info("Server has started on 127.0.0.1:80.");
            log.info("Waiting for a connection...");

            while (true) {

                Socket client = server.accept();
                log.info("A client connected.");

                new Thread(() -> {
                    try {
                        client.setKeepAlive(true);
                        PrintWriter browserClientWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"));
                        BufferedReader browserClientReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));

                        String message;
                        boolean isClientConnected = false;


                        while ((message = browserClientReader.readLine()) != null) {


                            //log.debug("-> {}", message);

                            if (!isClientConnected) {
                                if (message.contains("Sec-WebSocket-Key:")) {
                                    String token = message.split(":")[1].trim();

                               //     log.info("-> Отримано токен клієнта [{}]", token);

                                    String response = prepareResponse(token);
                                    log.info(" <- response  {}", response);
                                    browserClientWriter.println(response);
                                    browserClientWriter.flush();
                                    isClientConnected = true;

                                }
                            } else {
                                log.info("Web Client connected");

                                log.info("-> {}", message);
                                handleMessages(client);


                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String prepareResponse(String secWebSocketKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String encodeToString = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((secWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")));
        log.info("Sec-WebSocket-Key:  {}", secWebSocketKey);
        log.info("Sec-WebSocket-Accept: {}", encodeToString);
        return "HTTP/1.1 101 Switching Protocols\r\nConnection: Upgrade\r\nUpgrade: websocket\r\nSec-WebSocket-Accept: " + encodeToString + "\r\n\r\n";

    }

    private static void handleMessages(Socket client) throws IOException {
        //todo допилити декодуваня фреймів
        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        Scanner reader = new Scanner(in, "UTF-8");

        while (reader.hasNext()) {
            String newLine = reader.nextLine();
            log.info("New message: {}", newLine);
            byte[] allByte = newLine.getBytes("UTF-8");
            log.info("New message: {}", newLine.getBytes("UTF-8"));

            byte[] encoded = new byte[newLine.length()];

            byte[] key = new byte[] { allByte[2], allByte[3], allByte[4], allByte[5] };

            System.arraycopy(allByte, 0, encoded, 5, 7);

            log.info("All byte {}", allByte);
            log.info("Key byte {}", key);
            log.info("Enc byte {}", encoded);

            byte[] decoded = new byte[newLine.length()];
            for (int i = 0; i < encoded.length; i++) {
                decoded[i] = (byte) (encoded[i] ^ key[i & 0x3]);
            }
            String decodedString = new String(decoded);
            log.info("New message: {}", decodedString);

        }

    }

}
