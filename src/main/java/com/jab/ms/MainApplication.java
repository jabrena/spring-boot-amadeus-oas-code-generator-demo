package com.jab.ms;

import com.jab.ms.openapi.gen.amadeus.auth.api.OAuth2AccessTokenApi;
import com.jab.ms.openapi.gen.amadeus.flight_check_in_links.api.CheckinLinksApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

@EnableScheduling
@Configuration
class WebConfig {

    public static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public RestTemplate getRestTemplateBean(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.defaultMessageConverters().build();
    }

    record AmadeusCredentials(String clientid, String secret) {}

    @Bean
    public AmadeusCredentials getAmadeusCredentials(
        @Value("${CLIENT_ID}") String AMADEUS_CLIENT_ID,
        @Value("${SECRET}") String AMADEUS_CLIENT_SECRET
    ) {
        logger.info("Loading environment variables:");
        logger.info("AMADEUS_CLIENT_ID: {}", AMADEUS_CLIENT_ID);
        logger.info("AMADEUS_CLIENT_SECRET: {}", AMADEUS_CLIENT_SECRET);

        return new AmadeusCredentials(AMADEUS_CLIENT_ID, AMADEUS_CLIENT_SECRET);
    }
}

@EnableScheduling
@Service
class AmadeusTokenService {

    public static Logger logger = LoggerFactory.getLogger(AmadeusTokenService.class);

    @Autowired
    WebConfig.AmadeusCredentials amadeusCredentials;

    private String accessToken;

    @Scheduled(fixedDelay = 170000)
    public void scheduleFixedDelayTask() {
        final com.jab.ms.openapi.gen.amadeus.auth.client.ApiClient apiClient = new com.jab.ms.openapi.gen.amadeus.auth.client.ApiClient();
        OAuth2AccessTokenApi oAuth2AccessTokenApi = new OAuth2AccessTokenApi(apiClient);
        var response = oAuth2AccessTokenApi.oauth2Token(
            "client_credentials",
            amadeusCredentials.clientid(),
            amadeusCredentials.secret()
        );

        this.accessToken = response.getAccessToken();

        var tokenInfo = oAuth2AccessTokenApi.getOauth2TokenInfo(accessToken);
        logger.info("{}", tokenInfo.toString());
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}

@RestController
class DemoController {

    public static Logger logger = LoggerFactory.getLogger(AmadeusTokenService.class);

    @Autowired
    private AmadeusTokenService amadeusTokenService;

    @GetMapping(path = "/api/v1/check-in-links")
    public ResponseEntity<String> getCheckInLinks() {
        var accessToken = amadeusTokenService.getAccessToken();

        //Flight_check_in_links
        final com.jab.ms.openapi.gen.amadeus.flight_check_in_links.client.ApiClient apiClient2 = new com.jab.ms.openapi.gen.amadeus.flight_check_in_links.client.ApiClient();
        apiClient2.addDefaultHeader("Authorization", "Bearer " + accessToken);
        CheckinLinksApi checkinLinksApi = new CheckinLinksApi(apiClient2);
        var response2 = checkinLinksApi.getCheckinURLs("IB", "en-GB");

        return ResponseEntity.ok().body(response2.toString());
    }
}
