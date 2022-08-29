package louie.hanse.shareplate.web.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.KeywordService;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/keywords")
@RestController
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping
    public void register(@RequestBody KeywordRegisterRequest keywordRegisterRequest,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        keywordService.register(keywordRegisterRequest, memberId);
    }
}
