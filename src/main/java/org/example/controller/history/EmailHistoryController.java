package org.example.controller.history;

import org.example.dto.history.EmailDTO;
import org.example.dto.history.EmailFilterDTO;
import org.example.service.AuthorizationService;
import org.example.service.EmailHistoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailHistoryController {
    private final AuthorizationService authorizationService;
    private final EmailHistoryService emailHistoryService;

    public EmailHistoryController(AuthorizationService authorizationService, EmailHistoryService emailHistoryService) {
        this.authorizationService = authorizationService;
        this.emailHistoryService = emailHistoryService;
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl<EmailDTO>> emailPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        PageImpl<EmailDTO> body = emailHistoryService.paginationEmail(page - 1, size);
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/filter")
    public ResponseEntity<PageImpl<EmailDTO>> filterEmail(@RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                          @RequestBody @Valid EmailFilterDTO filterDTO) {
        PageImpl<EmailDTO> filter = emailHistoryService.filter(filterDTO, page - 1, size);
        return ResponseEntity.ok().body(filter);
    }
}
