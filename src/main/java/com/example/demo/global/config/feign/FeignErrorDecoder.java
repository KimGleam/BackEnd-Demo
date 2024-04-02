package com.example.demo.global.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;


public class FeignErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {

		return switch (response.status()) {
			//case 404 -> new InterfaceRequestException(InterfaceErrorCode.NOT_FOUND_ERROR);
			case 500 -> new Exception();
			default -> new Exception(response.reason());
		};
	}
}
