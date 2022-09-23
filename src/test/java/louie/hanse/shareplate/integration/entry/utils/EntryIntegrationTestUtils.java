package louie.hanse.shareplate.integration.entry.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.springframework.mock.web.MockMultipartFile;

public class EntryIntegrationTestUtils {

    public static ShareRegisterRequest getShareRegisterRequest(LocalDateTime closedDateTime) {
        MockMultipartFile image = new MockMultipartFile(
            "이미지", "test.txt".getBytes(StandardCharsets.UTF_8));

        ShareRegisterRequest request = new ShareRegisterRequest(ShareType.DELIVERY,
            List.of(image),
            "테스트를 위한 제목", 3000, 12000, 3,
            "강남역", "코드스쿼드", true, false, List.of("피자", "도미노피자"),
            37.498095, 127.027610, "도미노 피자에 대해서 어쩌구",
            closedDateTime);
        return request;
    }
}
