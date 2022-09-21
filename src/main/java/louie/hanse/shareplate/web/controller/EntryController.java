package louie.hanse.shareplate.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.EntryService;
import louie.hanse.shareplate.service.NotificationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class EntryController {

    private final EntryService entryService;
    private final NotificationService notificationService;

    @GetMapping("/entries")
    public Map<String, List<Long>> getIdList(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<Long> idList = entryService.getIdList(memberId);
        return Collections.singletonMap("idList", idList);
    }

    @PostMapping("/shares/{shareId}/entry")
    public void entryShare(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 shareId가 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long shareId,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.entry(shareId, memberId);
        notificationService.saveActivityNotificationAndSend(shareId, memberId);
    }

    @DeleteMapping("/shares/{shareId}/entry")
    public void cancelEntry(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 shareId가 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long shareId,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.cancel(shareId, memberId);
    }

}
