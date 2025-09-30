package ru.alfabank.practice.nadershinaka.bankonboarding.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.alfabank.practice.nadershinaka.bankonboarding.config.DaDataConfig;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataResponse;

import java.util.Map;

@FeignClient(name = "dadata-client", url = "${dadata.url}", configuration = DaDataConfig.class)
public interface DaDataClient {

    @PostMapping(value = "/suggestions/api/4_1/rs/suggest/address/",
            consumes = "application/json",
            produces = "application/json")
    DadataResponse searchAddress(
            @RequestBody DadataRequest request
    );
}