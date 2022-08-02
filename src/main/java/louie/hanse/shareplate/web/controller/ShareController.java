package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/shares")
@RestController
public class ShareController {

    private final ShareService shareService;

    @PostMapping
    public void register(ShareRegisterRequest shareRegisterRequest, HttpServletRequest request)
        throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        shareService.register(shareRegisterRequest, memberId);
    }
}
