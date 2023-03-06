package com.epam.esm.jwt.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(name = "FeignClient", url = "https://oauth2.googleapis.com/tokeninfo")
public interface FeignClient {
    @GetMapping
    Map<String, String> verifyTokenAndGetMapOfClaims(@RequestParam("id_token") String token);
}