package com.publicAPI.oauth2.api.applicationApi;

import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static helpers.RestResource.*;

public class _CommonAPI {

    public static Response common_API_POST(String URL_suffix, String token, String requestBody, String contentType, int responseCode, String [] responseBody, String JSON_Path){

        String URL = "/" + URL_suffix;

        String payload;
        if(requestBody.equals("false")) {payload = "{}";}
        else{payload = requestBody;}

        return post_ApiMethod(URL, token, payload, contentType, responseCode, responseBody, true, JSON_Path);
    }

    public static Response common_API_GET(String URL_suffix, String token, String queryParams, int responseCode, String [] responseBody, String JSON_Path) {

        String queryParams_ = "";

        if(!queryParams.equals("false")){queryParams_ = "?" +queryParams;}

        String URL = "/" + URL_suffix + queryParams_;

        return get_ApiMethod(URL, token, responseCode, responseBody, true, JSON_Path);
    }

    public static void common_API_PATCH(String URL_suffix, String token, String requestBody,String contentType, int responseCode, String [] responseBody, String JSON_Path){

        String URL = "/" + URL_suffix;

        String payload;
        if(requestBody.equals("false")) {payload = "{}";}
        else{payload = requestBody;}

        patch_ApiMethod(token,URL,payload,contentType,responseCode,responseBody,true,JSON_Path);
    }

    public static void common_API_PUT(String URL_suffix, String token, String requestBody, int responseCode, String [] responseBody, boolean validateJsonSchema,  String JSON_Path){

        String URL = "/" + URL_suffix;

        String payload;
        if(requestBody.equals("false")) {payload = "{}";}
        else{payload = requestBody;}

        put_ApiMethod(token, URL, payload, responseCode, responseBody, true, validateJsonSchema, JSON_Path);
    }

    public static void common_API_DELETE(String URL_Suffix, String token,int responseCode,String [] responseBody,boolean validateJsonSchema, String JSON_Path){

        String URL= "/" + URL_Suffix;
        delete_ApiMethod(token,URL,responseCode,responseBody,true,validateJsonSchema,JSON_Path);

    }


    public static Response common_API_POST_Response(String URL_suffix, String token, String requestBody, String contentType, int responseCode, String [] responseBody, String JSON_Path){

        String URL = "/" + URL_suffix;

        String payload;
        if(requestBody.equals("false")) {payload = "{}";}
        else{payload = requestBody;}

        return  post_ApiMethod(URL, token, payload, contentType, responseCode, responseBody, true, JSON_Path);
    }
    public static Response common_API_POST_Content(String URL_suffix, String token, File file, String contentType, int responseCode, String [] responseBody, String JSON_Path){

        String URL = "/" + URL_suffix;
        return  postContent_ApiMethod(URL,token,file,contentType,responseCode,responseBody,true,JSON_Path);
    }

    public static Response common_API_PUT_Content(String URL_suffix, String token, File file, String contentType, int responseCode, String [] responseBody, String JSON_Path){

        String URL = "/" + URL_suffix;
        return  putContent_ApiMethod(URL, token, file, contentType, responseCode, responseBody, true, JSON_Path);
    }
    public static Response common_API_PUT_Content(String url, File file, String contentType, int responseCode, String [] responseBody){

        return  putContent_ApiMethod(url, file, contentType, responseCode, responseBody, true);
    }

    public static int getStatus(String urlSufix, String token, int response_code, String JSON_path, boolean queryParams, String[] responseBody) {
        Response response = common_API_GET(urlSufix, token, String.valueOf(queryParams), response_code, responseBody, JSON_path);
        return response.statusCode();
    }

    public static void waitTest(String urlSufix, String token, int response_code, String JSON_path, boolean queryParams,
                         String[] responseBody, int timeoutSec, int pollIntervalSec, int statusCode) throws Exception {
        Awaitility.await().atMost(timeoutSec, TimeUnit.SECONDS).pollInterval(pollIntervalSec, TimeUnit.SECONDS)
                .until(() -> getStatus(urlSufix, token, response_code, JSON_path, queryParams, responseBody) == statusCode);
    }


    public static String jsonPayload_extension(boolean isString, String extensionValue) {

        JsonObject jsonPayload = new JsonObject();
        if (!isString) {int extensionValueInt = Integer.parseInt(extensionValue);
            jsonPayload.addProperty("extension", extensionValueInt);
        }else{
            jsonPayload.addProperty("extension", extensionValue);
        }
        return jsonPayload.toString();
    }

    public static String jsonPayload_FillFormFields(String valueValue) {

        StringBuilder jsonArrayString = new StringBuilder();
        jsonArrayString.append("[");

        JsonObject jsonPayload = new JsonObject();
        jsonPayload.addProperty("name", "Postcode Text Box");
        jsonPayload.addProperty("value", valueValue);
        jsonArrayString.append(jsonPayload);

        jsonArrayString.append("]");
        return jsonArrayString.toString();
    }




}
