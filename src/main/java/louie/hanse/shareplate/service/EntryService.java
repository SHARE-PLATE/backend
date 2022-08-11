package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.EntryRepository;
import louie.hanse.shareplate.repository.ShareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class EntryService {

    private final MemberService memberService;
    private final ShareRepository shareRepository;
    private final EntryRepository entryRepository;

    @Transactional
    public void entry(Long shareId, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        //TODO 커스텀 예외처리
        Share share = shareRepository.findById(shareId).orElseThrow();
        Entry entry = new Entry(share, member);
        entryRepository.save(entry);
    }
}
