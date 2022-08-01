package louie.hanse.shareplate.config;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.interceptor.LoginInterceptor;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtProvider))
                .order(1)
                .addPathPatterns("/members/location");
    }
}
