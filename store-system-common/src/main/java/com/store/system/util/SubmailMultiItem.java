package com.store.system.util;

import java.util.Map;

public class SubmailMultiItem {

    private String to;

    private Map<String, String> vars;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }
}
