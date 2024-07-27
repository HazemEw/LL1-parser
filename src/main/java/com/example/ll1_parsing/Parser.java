package com.example.ll1_parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {

    static Map<String, String> parsingTableMap = new HashMap<>();

    private Stack<String> ll1Stack;

    private MatcherCheck matcherCheck;
    private String[] nonTerminals ;

    private String[] terminals;

    public Parser() {
        matcherCheck = new MatcherCheck();

        nonTerminals= new String[]{
                "module-decl", "module-heading", "block", "declarations", "const-decl",
                "const-list", "var-decl", "var-list", "var-item", "name-list", "more-names",
                "data-type", "procedure-decl", "procedure-heading", "stmt-list", "statement",
                "ass-stmt", "exp", "exp-prime", "term", "term-prime", "factor", "add-oper",
                "mul-oper", "read-stmt", "write-stmt", "write-list", "more-write-value",
                "write-item", "if-stmt", "else-part", "while-stmt", "loop-stmt", "exit-stmt",
                "call-stmt", "condition", "relational-oper", "name-value", "value", "$"
        };
        terminals = new String[]{
                "(", ")", "*", "+", ",", "-", ".", "/", ":=", ":", ";", "<", "<=", "=", ">",
                ">=", "|=", "begin", "call", "char", "const", "div", "do", "else", "end",
                "exit", "if", "integer", "integer-value", "loop", "mod", "module", "name",
                "procedure", "readchar", "readint", "readln", "readreal", "real", "real-value",
                "then", "until", "var", "while", "writechar", "writeint", "writeln", "writereal", "ε"
        };

        ll1Stack = new Stack<>(); // Use Stack in Algorithm
        ll1Stack.push("$"); // Starting Token
        ll1Stack.push("module-decl");
    }


    // Add Data to Table
    static {
        addEntries();
    }
    static void addEntries() {
        parsingTableMap.put("module-decl,module", "module-heading declarations block name .");
        parsingTableMap.put("module-heading,module", "module name ;");
        parsingTableMap.put("block,begin", "begin stmt-list end");
        parsingTableMap.put("declarations,begin", "const-decl var-decl procedure-decl");
        parsingTableMap.put("declarations,const", "const-decl var-decl procedure-decl");
        parsingTableMap.put("declarations,procedure", "const-decl var-decl procedure-decl");
        parsingTableMap.put("declarations,var", "const-decl var-decl procedure-decl");
        parsingTableMap.put("const-decl,begin", "ε");
        parsingTableMap.put("const-decl,const", "const const-list");
        parsingTableMap.put("const-decl,procedure", "ε");
        parsingTableMap.put("const-decl,var", "ε");
        parsingTableMap.put("const-decl,ε", "ε");
        parsingTableMap.put("const-list,begin", "ε");
        parsingTableMap.put("const-list,name", "name = value ; const-list");
        parsingTableMap.put("const-list,procedure", "ε");
        parsingTableMap.put("const-list,var", "ε");
        parsingTableMap.put("const-list,ε", "ε");
        parsingTableMap.put("var-decl,begin", "ε");
        parsingTableMap.put("var-decl,procedure", "ε");
        parsingTableMap.put("var-decl,var", "var var-list");
        parsingTableMap.put("var-decl,ε", "ε");
        parsingTableMap.put("var-list,begin", "ε");
        parsingTableMap.put("var-list,name", "var-item ; var-list");
        parsingTableMap.put("var-list,procedure", "ε");
        parsingTableMap.put("var-list,ε", "ε");
        parsingTableMap.put("var-item,name", "name-list : data-type");
        parsingTableMap.put("name-list,name", "name more-names");
        parsingTableMap.put("more-names,)", "ε");
        parsingTableMap.put("more-names,,", ", name-list");
        parsingTableMap.put("more-names,:", "ε");
        parsingTableMap.put("data-type,char", "char");
        parsingTableMap.put("data-type,integer", "integer");
        parsingTableMap.put("data-type,real", "real");
        parsingTableMap.put("procedure-decl,begin", "ε");
        parsingTableMap.put("procedure-decl,procedure", "procedure-heading declarations block name ; procedure-decl");
        parsingTableMap.put("procedure-heading,procedure", "procedure name ;");
        parsingTableMap.put("stmt-list,;", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,begin", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,call", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,exit", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,if", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,loop", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,name", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,readchar", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,readint", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,readln", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,readreal", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,until", "ε");
        parsingTableMap.put("stmt-list,while", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,writechar", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,writeint", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,writeln", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,writereal", "statement ; stmt-list");
        parsingTableMap.put("stmt-list,ε", "ε");
        parsingTableMap.put("stmt-list,end", "ε");
        parsingTableMap.put("stmt-list,else", "ε");
        parsingTableMap.put("statement,;", "ε");
        parsingTableMap.put("statement,begin", "block");
        parsingTableMap.put("statement,call", "call-stmt");
        parsingTableMap.put("statement,exit", "exit-stmt");
        parsingTableMap.put("statement,if", "if-stmt");
        parsingTableMap.put("statement,loop", "loop-stmt");
        parsingTableMap.put("statement,name", "ass-stmt");
        parsingTableMap.put("statement,readchar", "read-stmt");
        parsingTableMap.put("statement,readint", "read-stmt");
        parsingTableMap.put("statement,readln", "read-stmt");
        parsingTableMap.put("statement,readreal", "read-stmt");
        parsingTableMap.put("statement,while", "while-stmt");
        parsingTableMap.put("statement,writechar", "write-stmt");
        parsingTableMap.put("statement,writeint", "write-stmt");
        parsingTableMap.put("statement,writeln", "write-stmt");
        parsingTableMap.put("statement,writereal", "write-stmt");
        parsingTableMap.put("ass-stmt,name", "name := exp");
        parsingTableMap.put("exp,(", "term exp-prime");
        parsingTableMap.put("exp,integer-value", "term exp-prime");
        parsingTableMap.put("exp,name", "term exp-prime");
        parsingTableMap.put("exp,real-value", "term exp-prime");
        parsingTableMap.put("exp-prime,+", "add-oper term exp-prime");
        parsingTableMap.put("exp-prime,-", "add-oper term exp-prime");
        parsingTableMap.put("exp-prime,;", "ε");
        parsingTableMap.put("exp-prime,)", "ε");
        parsingTableMap.put("term,(", "factor term-prime");
        parsingTableMap.put("term,integer-value", "factor term-prime");
        parsingTableMap.put("term,name", "factor term-prime");
        parsingTableMap.put("term,real-value", "factor term-prime");
        parsingTableMap.put("term-prime,;", "ε");
        parsingTableMap.put("term-prime,*", "mul-oper factor term-prime");
        parsingTableMap.put("term-prime,+", "ε");
        parsingTableMap.put("term-prime,-", "ε");
        parsingTableMap.put("term-prime,)", "ε");
        parsingTableMap.put("term-prime,/", "mul-oper factor term-prime");
        parsingTableMap.put("term-prime,div", "mul-oper factor term-prime");
        parsingTableMap.put("term-prime,mod", "mul-oper factor term-prime");
        parsingTableMap.put("term-prime,ε", "ε");
        parsingTableMap.put("factor,(", "( exp )");
        parsingTableMap.put("factor,integer-value", "name-value");
        parsingTableMap.put("factor,name", "name-value");
        parsingTableMap.put("factor,real-value", "name-value");
        parsingTableMap.put("add-oper,+", "+");
        parsingTableMap.put("add-oper,-", "-");
        parsingTableMap.put("mul-oper,*", "*");
        parsingTableMap.put("mul-oper,/", "/");
        parsingTableMap.put("mul-oper,div", "div");
        parsingTableMap.put("mul-oper,mod", "mod");
        parsingTableMap.put("read-stmt,readchar", "readchar ( name-list )");
        parsingTableMap.put("read-stmt,readint", "readint ( name-list )");
        parsingTableMap.put("read-stmt,readln", "readln");
        parsingTableMap.put("read-stmt,readreal", "readreal ( name-list )");
        parsingTableMap.put("write-stmt,while", "ε");
        parsingTableMap.put("write-stmt,writechar", "writechar ( write-list )");
        parsingTableMap.put("write-stmt,writeint", "writeint ( write-list )");
        parsingTableMap.put("write-stmt,writeln", "writeln");
        parsingTableMap.put("write-stmt,writereal", "writereal ( write-list )");
        parsingTableMap.put("write-list,integer-value", "write-item more-write-value");
        parsingTableMap.put("write-list,name", "write-item more-write-value");
        parsingTableMap.put("write-list,real-value", "write-item more-write-value");
        parsingTableMap.put("more-write-value,,", ", write-list");
        parsingTableMap.put("more-write-value,)", "ε");
        parsingTableMap.put("write-item,integer-value", "value");
        parsingTableMap.put("write-item,name", "name");
        parsingTableMap.put("write-item,real-value", "value");
        parsingTableMap.put("if-stmt,if", "if condition then stmt-list else-part end");
        parsingTableMap.put("else-part,else", "else stmt-list");
        parsingTableMap.put("else-part,end", "ε");
        parsingTableMap.put("while-stmt,while", "while condition do stmt-list end");
        parsingTableMap.put("loop-stmt,loop", "loop stmt-list until condition");
        parsingTableMap.put("exit-stmt,exit", "exit");
        parsingTableMap.put("call-stmt,call", "call name");
        parsingTableMap.put("condition,integer-value", "name-value relational-oper name-value");
        parsingTableMap.put("condition,name", "name-value relational-oper name-value");
        parsingTableMap.put("condition,real-value", "name-value relational-oper name-value");
        parsingTableMap.put("relational-oper,|=", "|=");
        parsingTableMap.put("relational-oper,<", "<");
        parsingTableMap.put("relational-oper,<=", "<=");
        parsingTableMap.put("relational-oper,=", "=");
        parsingTableMap.put("relational-oper,>", ">");
        parsingTableMap.put("relational-oper,>=", ">=");
        parsingTableMap.put("name-value,integer-value", "value");
        parsingTableMap.put("name-value,name", "name");
        parsingTableMap.put("name-value,real-value", "value");
        parsingTableMap.put("value,integer-value", "integer-value");
        parsingTableMap.put("value,real-value", "real-value");
    }



    public String parsingCode(ArrayList<Token> tokens) {
        ArrayList<Token> originalTokens = new ArrayList<>(tokens);
        String topStack = "";
        String codeState = "";
        int index = 0;
        while (!ll1Stack.isEmpty()) {
            String token = tokens.get(index).token;
            token = checkNameOrNumber(token);
            topStack = ll1Stack.pop();
            switch (token) {
                case "$":
                    if (topStack.equals("$")) {
                        codeState = "parsed successfully.";
                    }
                    break;
                default:
                    if (isTerminal(topStack)) {
                        if (index < tokens.size() && topStack.equals(token)) {
                            index++;
                        } else {
                            if (originalTokens.get(index).token.equals("$")) {
                                codeState = "code not complete";
                            } else {
                                if (!isTerminal(originalTokens.get(index).token)){
                                codeState = "Line----> " + tokens.get(index).line + " : Unexpected token " + originalTokens.get(index-1).token;
                                }else
                                    codeState = "Line----> " + tokens.get(index).line + " : Unexpected token " + originalTokens.get(index).token;

                            }
                        }
                    } else if (isNonTerminal(topStack)) {
                        String roll = getProduction(topStack, token);
                        if (roll != null) {
                            if (!roll.equals("ε")) {
                                String[] productionSplit = roll.split("\\s+");
                                for (int i = productionSplit.length - 1; i >= 0; i--) {
                                    ll1Stack.push(productionSplit[i]);
                                }
                            }
                        } else {
                            if (originalTokens.get(index).token.equals("$")) {
                                codeState = "code not complete";
                            } else {
                                codeState = " Line---> " + tokens.get(index).line + ": Unexpected token " + originalTokens.get(index).token;
                            }
                        }
                    } else {
                        codeState = " Undefined symbol '" + topStack + "'";
                    }
                    break;
            }

            if (!codeState.isEmpty()) {
                break;
            }

        }
        return codeState;
    }

    boolean isNonTerminal(String symbol) {
        for (int i = 0; i < nonTerminals.length; i++) {
            if (nonTerminals[i].equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    boolean isTerminal(String symbol) {
        for (int i = 0; i < terminals.length; i++) {
            if (terminals[i].equals(symbol)) {
                return true;
            }
        }
        return false;
    }
    private String checkNameOrNumber(String token){
        if (!isTerminal(token) && matcherCheck.isName(token)) {
            token = "name";
        } else if (!isTerminal(token) && matcherCheck.isIntegerValue(token)) {
            token = "integer-value";
        } else if (!isTerminal(token) && matcherCheck.isRealNumber(token)) {
            token = "real-value";
        }
        return token;
    }

    //get Production From Table
    public static String getProduction(String nonTerminal, String terminal) {
        String key = nonTerminal + "," + terminal;
        return parsingTableMap.getOrDefault(key, null);
    }

    public String generateLL1Table() {
        StringBuilder tableString = new StringBuilder();
        for (String nonTerminal : nonTerminals) {
            for (String terminal : terminals) {
                String production = getProduction(nonTerminal, terminal);
                if (production != null) {
                    tableString.append("[").append(nonTerminal).append("][").append(terminal).append("]---->").append(production).append("\n").append("\n");
                }
            }
        }
        return tableString.toString();
    }

}
