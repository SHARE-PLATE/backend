package louie.hanse.shareplate.integration.share.utils;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import java.nio.charset.StandardCharsets;

public class ShareIntegrationTestUtils {

    public static MultiPartSpecification createMultiPartSpecification(String name, Object value) {
        return new MultiPartSpecBuilder(value)
            .controlName(name)
            .charset(StandardCharsets.UTF_8)
            .build();
    }
}
