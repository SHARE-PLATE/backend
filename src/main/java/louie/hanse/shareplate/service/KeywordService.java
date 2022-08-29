package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.KeywordRepository;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final MemberService memberService;

    @Transactional
    public void register(KeywordRegisterRequest request, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Keyword keyword = request.toEntity(member);
        keywordRepository.save(keyword);
    }

    @Transactional
    public void delete(Long id) {
        keywordRepository.deleteById(id);
    }
}
