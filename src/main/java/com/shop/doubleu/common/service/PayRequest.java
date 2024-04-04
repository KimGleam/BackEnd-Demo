package com.shop.doubleu.common.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PayRequest", url = "${api.server.url}")
public interface PayRequest {

	@PostMapping(value = "${api.server.send-api}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	String post(@RequestHeader("Authorization") String apiKey, @RequestBody Map<String, Object> params);
}
