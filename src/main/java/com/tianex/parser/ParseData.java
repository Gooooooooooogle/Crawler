package com.tianex.parser;

import com.tianex.model.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseData {

    private String url;
    private String title;
    private List<Link> links;
    private HashMap<String, String> parseMap = new HashMap<String, String>();

    public ParseData() {

    }

    public ParseData(String url, String title, ArrayList<Link> links) {
        this.url = url;
        this.title = title;
        this.links = links;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public HashMap<String, String> getParseMap() {
        return parseMap;
    }

    public void setParseMap(HashMap<String, String> parseMap) {
        this.parseMap = parseMap;
    }
}
