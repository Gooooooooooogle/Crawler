package com.tianex.parser;

public class ParseResult {

    private ParseData parsedata;
    private ParseText parsetext;
    private Object parseObj;

    public ParseResult() {
    }

    public ParseResult(ParseData parsedata, ParseText parsetext) {
        this.parsedata = parsedata;
        this.parsetext = parsetext;
    }

    public ParseData getParsedata() {
        return parsedata;
    }

    public void setParsedata(ParseData parsedata) {
        this.parsedata = parsedata;
    }

    public ParseText getParsetext() {
        return parsetext;
    }

    public void setParsetext(ParseText parsetext) {
        this.parsetext = parsetext;
    }

    public Object getParseObj() {
        return parseObj;
    }

    public void setParseObj(Object parseObj) {
        this.parseObj = parseObj;
    }
}
