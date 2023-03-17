package com.epam.esm.jwt.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(value = "${feign.client.config.name}", url = "${feign.client.config.url}")
public interface FeignClient {
    @GetMapping
    Map<String, String> verifyTokenAndGetMapOfClaims(@RequestParam("id_token") String token);
}