package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.web.dto.share.ShareDetailResponse;
import louie.hanse.shareplate.web.dto.share.ShareEditRequest;
import louie.hanse.shareplate.web.dto.share.ShareMineSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        ShareSearchRequest shareSearchRequest) {
        return shareService.searchAroundMember(shareSearchRequest);
    }

    @GetMapping("/mine")
    public List<ShareSearchResponse> searchMine(
        ShareMineSearchRequest shareMineSearchRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return shareService.searchMine(shareMineSearchRequest, memberId);
    }

    @GetMapping("/{id}")
    public ShareDetailResponse getDetail(@PathVariable Long id, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return shareService.getDetail(id, accessToken);
    }

    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, ShareEditRequest shareEditRequest,
        HttpServletRequest request)
        throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        shareService.edit(shareEditRequest, id, memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        shareService.delete(id, memberId);
    }
}
