package louie.hanse.shareplate.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.WishService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/wish-list")
@RestController
public class WishController {

    private final WishService wishService;

    @PostMapping
    public void register(@RequestBody Map<String, Long> map, HttpServletRequest request) {
        Long shareId = map.get("shareId");
        Long memberId = (Long) request.getAttribute("memberId");

        wishService.register(memberId, shareId);
    }
}
