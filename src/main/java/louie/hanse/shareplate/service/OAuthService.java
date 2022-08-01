package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.oauth.OAuthAccessToken;
import louie.hanse.shareplate.oauth.OAuthProperties;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperties oAuthProperties;

    public String getAccessToken(String code) {
        RestTemplate restTemplate = createRestTemplateWithFormHttpMessageConverter();

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("grant_type", "authorization_code");
        param.add("client_id", oAuthProperties.getClientId());
        param.add("redirect_uri", oAuthProperties.getRedirectUrl());
        param.add("code", code);

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> oAuthTokenRequestHttpEntity = new HttpEntity<>(
            param, httpHeaders);

        return restTemplate.postForObject(oAuthProperties.getAccessTokenApiUrl(),
            oAuthTokenRequestHttpEntity, OAuthAccessToken.class).getAccessToken();
    }

    public OauthUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = createRestTemplateWithFormHttpMessageConverter();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        return restTemplate.postForObject(oAuthProperties.getUserApiUrl(), httpEntity,
            OauthUserInfo.class);
    }

    private RestTemplate createRestTemplateWithFormHttpMessageConverter() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        return restTemplate;
    }
}
