package com.publicAPI.oauth2.api.applicationApi.studiesAPI;

import org.json.JSONObject;


public class StudiesApi {


    public static String studiesJsonPayload(String status, String[] languages, String enTitle, String esTitle, String sponsor, String principalInvestigator, String irbNumber, String nickname, String uniqueProtocolId, String cro, int studyTargetEnrollmentNumber, int studySiteTargetEnrollmentNumber, String sponsorType, boolean hasJobTitleInSignatures) {

        // Create JSON Object
        JSONObject jsonObject = new JSONObject();

        // Set values in the JSON object
        jsonObject.put("status", status);

        // Languages array
        jsonObject.put("languages", languages);

        // Title object
        JSONObject titleObject = new JSONObject();
        titleObject.put("en", enTitle);
        titleObject.put("es", esTitle);
        jsonObject.put("title", titleObject);

        jsonObject.put("sponsor", sponsor);
        jsonObject.put("principalInvestigator", principalInvestigator);
        jsonObject.put("irbNumber", irbNumber);
        jsonObject.put("nickname", nickname);
        jsonObject.put("uniqueProtocolId", uniqueProtocolId);
        jsonObject.put("cro", cro);
        jsonObject.put("studyTargetEnrollmentNumber", studyTargetEnrollmentNumber);
        jsonObject.put("studySiteTargetEnrollmentNumber", studySiteTargetEnrollmentNumber);
        jsonObject.put("sponsorType", sponsorType);
        jsonObject.put("hasJobTitleInSignatures", hasJobTitleInSignatures);

        return jsonObject.toString();
    }


}
