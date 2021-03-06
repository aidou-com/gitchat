package com.longmaple.ttmall.productsvr.intercept;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;


@Component
public class FeignClientInterceptor implements RequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TOKEN_TYPE = "Bearer";

	@Override
	public void apply(RequestTemplate template) {
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null && authentication
				.getDetails() instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication
					.getDetails();
			logger.debug("Authentication token = " + details.getTokenValue() + " " + details.getTokenType());
			template.header(AUTHORIZATION_HEADER,
					String.format("%s %s", BEARER_TOKEN_TYPE, details.getTokenValue()));
			
		}
	}
}