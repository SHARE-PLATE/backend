package louie.hanse.shareplate.config;

import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.converter.StringToShareTypeConverter;
import louie.hanse.shareplate.interceptor.LogoutInterceptor;
import louie.hanse.shareplate.interceptor.MemberVerificationInterceptor;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.LoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberVerificationInterceptor(jwtProvider))
            .order(1)
            .addPathPatterns("/members", "/members/location", "/shares");

        registry.addInterceptor(new LogoutInterceptor(jwtProvider, loginService))
            .order(2)
            .addPathPatterns("/logout", "/reissue/access-token");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"));
        registrar.registerFormatters(registry);

        registry.addConverter(new StringToShareTypeConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS")
            .allowedHeaders("Access-Token", "Refresh-Token");
    }
}
