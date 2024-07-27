package com.example.ll1_parsing;

import java.util.regex.Pattern;

public class MatcherCheck {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern INTEGER_VALUE_PATTERN = Pattern.compile("\\d+");
    private static final Pattern REAL_NUMBER_PATTERN = Pattern.compile("\\d+\\.\\d*|\\d*\\.\\d+");

    public boolean isName(String input) {
        return matchPattern(NAME_PATTERN, input);
    }

    public boolean isIntegerValue(String input) {
        return matchPattern(INTEGER_VALUE_PATTERN, input);
    }

    public boolean isRealNumber(String input) {
        return matchPattern(REAL_NUMBER_PATTERN, input);
    }

    private boolean matchPattern(Pattern pattern, String input) {
        return pattern.matcher(input).matches();
    }
}