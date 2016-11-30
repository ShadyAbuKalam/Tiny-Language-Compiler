package com.jerry.tiny.compiler.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shady Atef on 11/18/16.
 * Copyrights Shadyoatef@gmail.com
 */
public class TinyScanner {
    ScannerMode mode = ScannerMode.Start;
    private HashMap<TokenType, String> TokensMap = new HashMap<>();
    private PushbackInputStream inputStream;
    
    public TinyScanner() {
        this.TokensMap.put(TokenType.If, "if");
        this.TokensMap.put(TokenType.Then, "then");
        this.TokensMap.put(TokenType.Else, "else");
        this.TokensMap.put(TokenType.End, "end");
        this.TokensMap.put(TokenType.Repeat, "repeat");
        this.TokensMap.put(TokenType.Until, "until");
        this.TokensMap.put(TokenType.Read, "read");
        this.TokensMap.put(TokenType.Write, "write");
        
        
        this.TokensMap.put(TokenType.Plus, "+");
        this.TokensMap.put(TokenType.Minus, "-");
        this.TokensMap.put(TokenType.Multiplication, "*");
        this.TokensMap.put(TokenType.Division, "/");
        this.TokensMap.put(TokenType.Equal, "=");
        this.TokensMap.put(TokenType.LessThan, "<");
        this.TokensMap.put(TokenType.LeftBracket, "(");
        this.TokensMap.put(TokenType.RightBracket, ")");
        this.TokensMap.put(TokenType.SemiColon, ";");
        this.TokensMap.put(TokenType.Assignment, ":=");
        
    }
    
    public Token scan() {
        mode = ScannerMode.Start;
        String value = "";
        try {
            
            while (mode != ScannerMode.Done) {
                //Read a char
                int input = inputStream.read();
                
                //Check if we reached the end of the stream
                if (input == -1) {
                    
                    mode = ScannerMode.Done;
                    inputStream.close();
                    //If there was no previous chars digested, just return null & quit
                    if(value.isEmpty())
                        return null;
                    else
                    continue;
                }
                char c = (char) input;
                if (mode == ScannerMode.Start) {
                    if (Character.isSpaceChar(c)) {
                        continue;
                    } else if (c == '{') {
                        mode = ScannerMode.InComment;
                    } else if (Character.isDigit(c)) {
                        value += c;
                        mode = ScannerMode.InNum;
                    } else if (Character.isAlphabetic(c)) {
                        value += c;
                        mode = ScannerMode.InID;
                    } else if (c == ':') {
                        value += c;
                        mode = ScannerMode.InAssign;
                    } else {
                        value += c;
                        mode = ScannerMode.Done;
                    }
                } else if (mode == ScannerMode.InComment) {
                    if (c == '}') {
                        mode = ScannerMode.Start;
                    }
                } else if (mode == ScannerMode.InNum) {
                    
                    if (Character.isDigit(c)) {
                        value += c;
                    } else {
                        inputStream.unread(c);
                        mode = ScannerMode.Done;
                    }
                } else if (mode == ScannerMode.InID) {
                    if (Character.isAlphabetic(c)) {
                        value += c;
                    } else {
                        inputStream.unread(c);
                        mode = ScannerMode.Done;
                    }
                } else if (mode == ScannerMode.InAssign) {
                    if (c == '=') {
                        value += c;
                        mode = ScannerMode.Done;
                        
                    } else {
                        inputStream.unread(c);
                        mode = ScannerMode.Done;
                    }
                }
                
            }
            
            //If the last token that was processed is a new line,return the next one
            if(value.equals(System.getProperty("line.separator")))
                return scan();
            
            // Check If the token is one of the predefined symbols or reserved words
            for (Map.Entry<TokenType, String> entry : TokensMap.entrySet()) {
                if (value.equals(entry.getValue())) {
                    return new Token(value, entry.getKey());
                }
            }
            
            //Check if the token is Number
            // Note this check is so simple because we are sure that value will be all numeric or no
            if (Character.isDigit(value.charAt(0))) {
                return new Token(value, TokenType.Number);
            }
            
            //Check if the token is Identifier
            // Note this check is so simple because we are sure that value will be all numeric or no
            if (Character.isAlphabetic(value.charAt(0))) {
                return new Token(value, TokenType.Identifier);
            }
            return new Token(value, TokenType.None);
            
            
        } catch (IOException e) {
            //We have reached the end of the stream
            return null;
            
        }
    }
    
    public void setInputStream(InputStream inputStream) {
        this.inputStream = new PushbackInputStream(inputStream);
    }
    
    
    enum ScannerMode {Start, InComment, InNum, InID, InAssign, Done}
    

}
