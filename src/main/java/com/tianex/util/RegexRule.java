package com.tianex.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexRule {

    private List<String> positive = new ArrayList<String>();
    private List<String> negative = new ArrayList<String>();

    public RegexRule() {
    }

    public RegexRule(ArrayList<String> rules){
        for (String rule : rules) {
            addRule(rule);
        }
    }

    public boolean isEmpty(){
        return positive.isEmpty();
    }

    /**
     * 添加一个正则规则 ,正则规则有两种,正正则和反正则 ,URL符合正则规则需要满足下面条件: 1.至少能匹配一条正正则 ; 2.不能和任何反正则匹配
     * @param rule
     * 正则规律，需要按照正反正则的规范输入，在普通正则前加入一个正(负)号来表示正反正则（正正则可不加符号，或者加"+",反正则必须加上'-')
     */
    public void addRule(String rule) {
        if (rule.length() == 0) {
            return;
        }
        char pn = rule.charAt(0);
        String realrule = rule.substring(1);
        if (pn == '+') {
            addPositive(realrule);
        }
        else if (pn == '-') {
            addNegative(realrule);
        }
        else {
            addPositive(rule);
        }
    }

    public void addPositive(String positiveregex) {
        positive.add(positiveregex);
    }

    public void addNegative(String negativeregex) {
        negative.add(negativeregex);
    }

    public boolean satisfy(String str) {

        for (String nregex : negative) {
            if (Pattern.matches(nregex, str)) {
                return false;
            }
        }

        int count = 0;
        for (String pregex : positive) {
            if (Pattern.matches(pregex, str)) {
                count++;
            }
        }

        if (count == 0) {
            return false;
        }
        else {
            return true;
        }
    }

}
