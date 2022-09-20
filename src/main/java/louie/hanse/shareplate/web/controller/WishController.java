package louie.hanse.shareplate.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.WishService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/wish-list")
@RestController
@Validated
public class WishController {

    private final WishService wishService;

    @PostMapping
    public void register(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 위시정보 필드값이 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long> map,
        HttpServletRequest request) {
        Long shareId = map.get("shareId");
        Long memberId = (Long) request.getAttribute("memberId");

        wishService.register(memberId, shareId);
    }

    @DeleteMapping
    public void cancelWish(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 위시정보 필드값이 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long> map,
        HttpServletRequest request) {
        Long shareId = map.get("shareId");
        Long memberId = (Long) request.getAttribute("memberId");

        wishService.delete(memberId, shareId);
    }
}
