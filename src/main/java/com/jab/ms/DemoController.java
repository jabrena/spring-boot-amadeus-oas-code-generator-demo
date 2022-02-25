package com.jab.ms;

import com.jab.ms.openapi.gen.amadeus.auth.api.OAuth2AccessTokenApi;
import com.jab.ms.openapi.gen.amadeus.flight_check_in_links.api.CheckinLinksApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${amadeus.clientid}")
    private String clientid;

    @Value("${amadeus.clientsecret}")
    private String clientsecret;

    @GetMapping(path = "/demo")
    public String demoOutput() {

        System.out.println(clientid);
        System.out.println(clientsecret);

        //Authentication
        final com.jab.ms.openapi.gen.amadeus.auth.client.ApiClient apiClient = new com.jab.ms.openapi.gen.amadeus.auth.client.ApiClient();
        OAuth2AccessTokenApi oAuth2AccessTokenApi = new OAuth2AccessTokenApi(apiClient);
        var response = oAuth2AccessTokenApi
                .oauth2Token("client_credentials", clientid, clientsecret);

        //Flight_check_in_links
        final com.jab.ms.openapi.gen.amadeus.flight_check_in_links.client.ApiClient apiClient2 = new com.jab.ms.openapi.gen.amadeus.flight_check_in_links.client.ApiClient();
        apiClient2.addDefaultHeader("Authorization", "Bearer " + response.getAccessToken());
        CheckinLinksApi checkinLinksApi = new CheckinLinksApi(apiClient2);
        var response2 = checkinLinksApi.getCheckinURLs("IB", "en-GB");

        System.out.println(response2);

        return response.toString();
    }

}
