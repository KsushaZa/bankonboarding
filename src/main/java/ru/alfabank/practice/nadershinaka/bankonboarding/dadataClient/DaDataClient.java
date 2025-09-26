package ru.alfabank.practice.nadershinaka.bankonboarding.dadataClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataResponse;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.Suggestions;

import java.util.Map;

@FeignClient(name = "dadata-client", url = "${dadata.url}")
public interface DaDataClient {
    @PostMapping(value = "/suggestions/api/4_1/rs/suggest/address/", consumes = "application/json", produces = "application/json")
    DadataResponse searchAddress(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody
    );
}
