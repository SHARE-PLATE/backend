package louie.hanse.shareplate.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
    private final String clientId;
    private final String redirectUrl;
    private final String accessTokenApiUrl;
    private final String loginFormUrl;
    private final String userApiUrl;
}
