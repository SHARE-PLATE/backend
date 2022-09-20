package louie.hanse.shareplate.integration.share;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import java.nio.charset.StandardCharsets;

class ShareIntegrationTestUtils {

    public static MultiPartSpecification createMultiPartSpecification(String name, Object value) {
        return new MultiPartSpecBuilder(value)
            .controlName(name)
            .charset(StandardCharsets.UTF_8)
            .build();
    }
}
