package louie.hanse.shareplate.web.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.EntryService;
import louie.hanse.shareplate.service.NotificationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shares/{shareId}/entry")
public class EntryController {

    private final EntryService entryService;
    private final NotificationService notificationService;

    @PostMapping
    public void entryShare(@PathVariable Long shareId, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.entry(shareId, memberId);
        notificationService.saveActivityNotificationAndSend(shareId, memberId);
    }

    @DeleteMapping
    public void cancelEntry(@PathVariable Long shareId, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.cancel(shareId, memberId);
    }

}
