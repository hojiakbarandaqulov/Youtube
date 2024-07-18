package org.example.service.history;

import org.example.dto.history.EmailDTO;
import org.example.dto.history.EmailFilterDTO;
import org.example.entity.history.EmailHistoryEntity;
//import org.example.enums.EmailHistoryStatus;
import org.example.enums.LanguageEnum;
import org.example.exp.AppBadException;
import org.example.repository.EmailHistoryRepository;
import org.example.entity.history.EmailHistoryEntity;
import org.example.exp.AppBadException;
import org.example.repository.EmailHistoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;

    public EmailHistoryService(EmailHistoryRepository emailHistoryRepository) {
        this.emailHistoryRepository = emailHistoryRepository;
    }

    public void crete(String toEmail, String text) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        if (toEmail.equals(entity.getEmail())) {
            throw new AppBadException("EmailHistory already exists");
        }
        entity.setEmail(toEmail);
        entity.setMessage(text);
        emailHistoryRepository.save(entity);
    }

    public void checkEmailLimit(String email) { // 1 minute -3 attempt
        // 23/05/2024 19:01:13
        // 23/05/2024 19:01:23
        // 23/05/2024 19:01:33

        // 23/05/2024 19:00:55 -- (current -1)
        // 23/05/2024 19:01:55 -- current

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        long count = emailHistoryRepository.countByEmailAndCreatedDateBetween(email, from, to);
        if (count >= 3) {
            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

    public void isNotExpiredEmail(String email) {
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty()) {
            throw new AppBadException("Email history not found");
        }
        EmailHistoryEntity entity = optional.get();
        if (entity.getCreatedDate().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Confirmation time expired");
        }
    }

    private EmailDTO emailDTO(EmailHistoryEntity entity) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setCreatedDate(entity.getCreatedDate());
        emailDTO.setEmail(entity.getEmail());
        emailDTO.setMessage(entity.getMessage());
        return emailDTO;
    }

    public PageImpl<EmailDTO> paginationEmail(int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmailHistoryEntity> all = emailHistoryRepository.findAll(pageable);

        List<EmailDTO> email = new LinkedList<>();
        for (EmailHistoryEntity emailEntity : all.getContent()) {
            email.add(emailDTO(emailEntity));
        }
        Long totalCount = all.getTotalElements();
        return new PageImpl<EmailDTO>(email, pageable, totalCount);
    }

    public PageImpl<EmailDTO> filter(EmailFilterDTO filterDTO, int page, int size) {

        return null;
    }
}