package louie.hanse.shareplate.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.KeywordRepository;
import louie.hanse.shareplate.web.dto.keyword.KeywordDetailResponse;
import louie.hanse.shareplate.web.dto.keyword.KeywordListResponse;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterRequest;
import louie.hanse.shareplate.web.dto.keyword.KeywordRegisterResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final MemberService memberService;

    @Transactional
    public KeywordRegisterResponse register(KeywordRegisterRequest request, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Keyword keyword = request.toEntity(member);
        keywordRepository.save(keyword);
        return new KeywordRegisterResponse(keyword);
    }

    @Transactional
    public void delete(Long id) {
        keywordRepository.deleteById(id);
    }

    public List<KeywordListResponse> getKeywords(Long memberId) {
        return keywordRepository.getKeywords(memberId);
    }

    @Transactional
    public void deleteAll(Long memberId, String location) {
        keywordRepository.deleteAllByMemberIdAndLocation(memberId, location);
    }

    public List<KeywordDetailResponse> getLocations(Long memberId, String location) {
        List<Keyword> keywordList = keywordRepository.findAllByMemberIdAndLocation(memberId,
            location);
        return keywordList.stream()
            .map(KeywordDetailResponse::new)
            .collect(Collectors.toList());
    }
}
