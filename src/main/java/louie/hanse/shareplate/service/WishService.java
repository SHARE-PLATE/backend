package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.domain.Wish;
import louie.hanse.shareplate.repository.ShareRepository;
import louie.hanse.shareplate.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

    private final MemberService memberService;
    private final WishRepository wishRepository;
    private final ShareRepository shareRepository;

    @Transactional
    public void register(Long memberId, Long shareId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareRepository.findById(shareId).orElseThrow();

        Wish wish = new Wish(share, member);
        wishRepository.save(wish);
    }

    @Transactional
    public void delete(Long memberId, Long shareId) {
        wishRepository.deleteByMemberIdAndShareId(memberId, shareId);
    }
}
