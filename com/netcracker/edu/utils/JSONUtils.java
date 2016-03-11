package com.netcracker.edu.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.netcracker.edu.utils.commands.CommandOutputFields;

public class JSONUtils {

    private static JsonParser parser = new JsonParser();

    public static String createStringStatusFromCommandJsonFormat(String jsonLine) throws JsonParseException {

        JsonObject json = parser.parse(jsonLine).getAsJsonObject();

        String statusHeader = CommandOutputFields.STATUS.toString();
        String codeHeader = CommandOutputFields.CODE.toString();
        String contentHeader = CommandOutputFields.CONTENT.toString();

        if (json.has(statusHeader) && json.has(codeHeader) && json.has(contentHeader)) {
            String status = "Status: " + json.get(statusHeader).getAsString();
            String statusCode = "Code: " + json.get(codeHeader).getAsString();
            String content = "Content: " + json.get(contentHeader).getAsString();
            return status + "\n" + statusCode + "\n" + content;
        } else {
            return jsonLine;
        }
    }
}
