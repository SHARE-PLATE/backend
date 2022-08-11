package louie.hanse.shareplate.web.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.service.EntryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shares/{id}/entry")
@Slf4j
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public void entryShare(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.entry(id, memberId);
    }

    @DeleteMapping
    public void cancelEntry(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        entryService.cancel(id, memberId);
    }

}
