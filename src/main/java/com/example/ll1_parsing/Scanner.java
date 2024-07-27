package com.example.ll1_parsing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Scanner {
    private ArrayList<Token> tokens;
    int lineIndex = 0;
    private String[] keyWords ;

    public Scanner()
    {
        tokens = new ArrayList<Token>();
        keyWords=new String []{
                "module",
                "BEGIN",
                "END",
                "CONST",
                "VAR",
                "INTEGER",
                "REAL",
                "CHAR",
                "PROCEDURE",
                "IF",
                "THEN",
                "ELSE",
                "WHILE",
                "LOOP",
                "EXIT",
                "CALL",
                "UNTIL",
                "READINT",
                "READREAL",
                "READCHAR",
                "READLN",
                "WRITEINT",
                "WRITEREAL",
                "WRITECHAR",
                "WRITELN",
                "DO"
        };
    }

    public ArrayList<Token> scan(String filePath) throws IOException {

            String code = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] lines = code.split("\n");
            lineIndex = 1;

            for (String line : lines) {
                String lineCode = line.replaceAll("\\s+", " ");
                String[] tokensInLine = lineCode.split("\\s+|(?=[()\\-+*/=;,:|])|(?<=[()\\-+*/=;,:<>|])");
                for (String tokenValue : tokensInLine) {
                    addToken(tokenValue);
                }
                lineIndex++;
            }

        ArrayList<Token> finalTokens = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String tokenValue = token.token;
            switch (determineCase(tokenValue, i, tokens)) {
                case "SPECIAL_CHAR":
                    String token1 = tokenValue.substring(0, tokenValue.length() - 1);
                    String token2 = tokenValue.substring(tokenValue.length() - 1);
                    finalTokens.add(new Token(token1, token.line, "name"));
                    finalTokens.add(new Token(token2, token.line, "Special Char"));
                    break;
                case "ASSIGNMENT":
                    finalTokens.add(new Token(":=", token.line, "Assignment"));
                    i++;
                    break;
                case "LESS_EQUAL":
                    finalTokens.add(new Token("<=", token.line, "Operation"));
                    i++;
                    break;
                case "GREATER_EQUAL":
                    finalTokens.add(new Token(">=", token.line, "Operation"));
                    i++;
                    break;
                default:
                    finalTokens.add(token);
                    break;
            }
        }
        finalTokens.add(new Token("$",600,"End of Code"));
        tokens.clear();
        tokens.addAll(finalTokens);
        return tokens;
    }

    private String determineCase(String tokenValue, int i, ArrayList<Token> tokens) {
        if (tokenValue.length() > 1 && tokenValue.endsWith(".")) {
            return "SPECIAL_CHAR";
        }
        if (tokenValue.equals(":") && i + 1 < tokens.size() && tokens.get(i + 1).token.equals("=")) {
            return "ASSIGNMENT";
        }
        if (tokenValue.equals("<") && i + 1 < tokens.size() && tokens.get(i + 1).token.equals("=")) {
            return "LESS_EQUAL";
        }
        if (tokenValue.equals(">") && i + 1 < tokens.size() && tokens.get(i + 1).token.equals("=")) {
            return "GREATER_EQUAL";
        }
        return "DEFAULT";
    }

    private void addToken(String tokenValue) {
        if(Objects.equals(tokenValue, ""))
             return;
        String operation = ":=|\\+|\\-|\\*|\\/|mod|div|=|<=|<|>=|>|\\|=";
        String specialChar = "\\;|\\,|\\(|\\)|\\[|\\]|:|\\.";

        if (tokenValue.matches("\\d+\\.\\d+")) {
            tokens.add(new Token( tokenValue, lineIndex,"Real Number"));
        } else if (tokenValue.matches("\\d+")) {
            tokens.add(new Token( tokenValue, lineIndex,"integer"));
        } else if (isKeyWord(tokenValue)) {
            tokens.add(new Token( tokenValue, lineIndex,"Key Word"));
        } else if (tokenValue.matches(operation)) {
            tokens.add(new Token( tokenValue, lineIndex,"Operation"));
        } else if (tokenValue.matches(specialChar)) {
            tokens.add(new Token( tokenValue, lineIndex,"Special Char"));
        }

        else if (Character.isLetter(tokenValue.charAt(0))) {
            if (tokenValue.equals("mod") || tokenValue.equals("div")) {
                tokens.add(new Token( tokenValue, lineIndex,"Operation"));
            } else {
                char[] charArray = tokenValue.toCharArray();
                int nonspecializing = checkSpecialChars(charArray);
                if (nonspecializing == -1) {
                    tokens.add(new Token( tokenValue, lineIndex,"name"));
                } else {
                    while (true) {
                        char[] part1 = Arrays.copyOfRange(charArray, 0, nonspecializing);
                        char[] part2 = Arrays.copyOfRange(charArray, nonspecializing, charArray.length);

                        if (part1.length > 0) {
                            addToken(String.valueOf(part1));
                        }

                        if (part2.length > 0) {
                            addToken(String.valueOf(part2));
                        }
                        break;
                    }
                }
            }
        }
    }
    public int checkSpecialChars(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isLetterOrDigit(chars[i]) && chars[i] != '.') {
                return i;
            }
        }
        return -1;
    }

    public boolean isKeyWord(String word) {
        for (String reservedWord : keyWords) {
            if (reservedWord.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

}




