package ru.alwertus.alwserv.common;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectExtended extends JSONObject {

    public JSONObjectExtended(String source) throws JSONException {
        super(source);
    }

    public String getString(String key, String defaultValue) {
        try {
            return super.getString(key);
        } catch(JSONException e) {
            return defaultValue;
        }
    }
    public Long getLong(String key, Long defaultValue) {
        try {
            return super.getLong(key);
        } catch(JSONException e) {
            return defaultValue;
        }
    }
    public Integer getInt(String key, Integer defaultValue) {
        try {
            return super.getInt(key);
        } catch(JSONException e) {
            return defaultValue;
        }
    }

}