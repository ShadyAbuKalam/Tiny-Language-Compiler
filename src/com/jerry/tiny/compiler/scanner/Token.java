package com.jerry.tiny.compiler.scanner;

/**
 * Created by Shady Atef on 11/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class Token {
    public String value;
    
    public String getValue() {
        return value;
    }
    
    public TokenType getType() {
        return type;
    }
    
    public TokenType type;
    
    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }
}

