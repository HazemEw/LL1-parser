package com.example.ll1_parsing;


public class Token {
    String token;
    int line;
    String type;
    public Token(String token, int line, String type) {
        this.token = token;
        this.line = line;
        this.type = type;

    }
}