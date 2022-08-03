package louie.hanse.shareplate.config;

import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.converter.StringToShareTypeConverter;
import louie.hanse.shareplate.interceptor.MemberVerificationInterceptor;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberVerificationInterceptor(jwtProvider))
                .order(1)
                .addPathPatterns("/members", "/members/location", "/shares");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"));
        registrar.registerFormatters(registry);

        registry.addConverter(new StringToShareTypeConverter());
    }
}
