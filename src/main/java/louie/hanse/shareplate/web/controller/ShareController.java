package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public List<ShareSearchResponse> searchAroundMember(
        ShareSearchRequest shareSearchRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return shareService.searchAroundMember(shareSearchRequest, memberId);
    }
}
