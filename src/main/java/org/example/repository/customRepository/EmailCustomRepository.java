package org.example.repository.customRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.dto.filter.FilterResponseDTO;
import org.example.dto.history.EmailFilterDTO;
import org.example.entity.history.EmailHistoryEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmailCustomRepository {
    private final EntityManager entityManager;

    public EmailCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public FilterResponseDTO<EmailHistoryEntity> filter(EmailFilterDTO filter, int page, int size) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        if (filter.getEmail() != null) {
            query.append(" and s.email :email ");
            params.put("email", filter.getEmail());
        }
        if (filter.getCreatedDate() !=null){
            query.append(" and s.created_date :createdDate ");
            params.put("createdDate", filter.getCreatedDate());
        }
        StringBuilder selectSql=new StringBuilder("From EmailHistoryEntity s where visible=:true ");
        StringBuilder countSql = new StringBuilder("select count(s) From EmailHistoryEntity s where s.visible = true ");

        selectSql.append(query);
        countSql.append(query);

        // select
        Query selectQuery = entityManager.createQuery(selectSql.toString());
        Query countQuery = entityManager.createQuery(countSql.toString());

        for (Map.Entry<String, Object> entity : params.entrySet()) {
            selectQuery.setParameter(entity.getKey(), entity.getValue());
            countQuery.setParameter(entity.getKey(), entity.getValue());
        }
        selectQuery.setFirstResult(page * size); // offset
        selectQuery.setMaxResults(size); // limit
        List<EmailHistoryEntity> studentEntityList = selectQuery.getResultList();
        // count
        Long totalCount = (Long) countQuery.getSingleResult();

        return new FilterResponseDTO<EmailHistoryEntity>(studentEntityList, totalCount);
    }
}
