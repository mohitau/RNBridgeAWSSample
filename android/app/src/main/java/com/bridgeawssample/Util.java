package com.bridgeawssample;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Util {
    public static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap writableMap = Arguments.createMap();
        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof Float || value instanceof Double) {
                writableMap.putDouble(key, jsonObject.getDouble(key));
            } else if (value instanceof Number) {
                writableMap.putInt(key, jsonObject.getInt(key));
            } else if (value instanceof String) {
                writableMap.putString(key, jsonObject.getString(key));
            } else if (value instanceof JSONObject) {
                writableMap.putMap(key, convertJsonToMap(jsonObject.getJSONObject(key)));
            } else if (value instanceof JSONArray) {
                writableMap.putArray(key, convertJsonToArray(jsonObject.getJSONArray(key)));
            } else if (value == JSONObject.NULL) {
                writableMap.putNull(key);
            }
        }

        return writableMap;
    }

    public static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
        WritableArray writableArray = Arguments.createArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof Float || value instanceof Double) {
                writableArray.pushDouble(jsonArray.getDouble(i));
            } else if (value instanceof Number) {
                writableArray.pushInt(jsonArray.getInt(i));
            } else if (value instanceof String) {
                writableArray.pushString(jsonArray.getString(i));
            } else if (value instanceof JSONObject) {
                writableArray.pushMap(convertJsonToMap(jsonArray.getJSONObject(i)));
            } else if (value instanceof JSONArray) {
                writableArray.pushArray(convertJsonToArray(jsonArray.getJSONArray(i)));
            } else if (value == JSONObject.NULL) {
                writableArray.pushNull();
            }
        }
        return writableArray;
    }

    public static JSONObject convertMapToJson(ReadableMap readableMap) throws JSONException {
        JSONObject object = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            switch (readableMap.getType(key)) {
                case Null:
                    object.put(key, JSONObject.NULL);
                    break;
                case Boolean:
                    object.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    object.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    object.put(key, readableMap.getString(key));
                    break;
                case Map:
                    object.put(key, convertMapToJson(readableMap.getMap(key)));
                    break;
                case Array:
                    object.put(key, convertArrayToJson(readableMap.getArray(key)));
                    break;
            }
        }
        return object;
    }

    public static JSONArray convertArrayToJson(ReadableArray readableArray) throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < readableArray.size(); i++) {
            switch (readableArray.getType(i)) {
                case Null:
                    break;
                case Boolean:
                    array.put(readableArray.getBoolean(i));
                    break;
                case Number:
                    array.put(readableArray.getDouble(i));
                    break;
                case String:
                    array.put(readableArray.getString(i));
                    break;
                case Map:
                    array.put(convertMapToJson(readableArray.getMap(i)));
                    break;
                case Array:
                    array.put(convertArrayToJson(readableArray.getArray(i)));
                    break;
            }
        }
        return array;
    }
}
