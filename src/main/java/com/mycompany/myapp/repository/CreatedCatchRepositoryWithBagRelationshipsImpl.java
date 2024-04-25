package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CreatedCatch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CreatedCatchRepositoryWithBagRelationshipsImpl implements CreatedCatchRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CREATEDCATCHES_PARAMETER = "createdCatches";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CreatedCatch> fetchBagRelationships(Optional<CreatedCatch> createdCatch) {
        return createdCatch.map(this::fetchBaitdata);
    }

    @Override
    public Page<CreatedCatch> fetchBagRelationships(Page<CreatedCatch> createdCatches) {
        return new PageImpl<>(
            fetchBagRelationships(createdCatches.getContent()),
            createdCatches.getPageable(),
            createdCatches.getTotalElements()
        );
    }

    @Override
    public List<CreatedCatch> fetchBagRelationships(List<CreatedCatch> createdCatches) {
        return Optional.of(createdCatches).map(this::fetchBaitdata).orElse(Collections.emptyList());
    }

    CreatedCatch fetchBaitdata(CreatedCatch result) {
        return entityManager
            .createQuery(
                "select createdCatch from CreatedCatch createdCatch left join fetch createdCatch.baitdata where createdCatch.id = :id",
                CreatedCatch.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<CreatedCatch> fetchBaitdata(List<CreatedCatch> createdCatches) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, createdCatches.size()).forEach(index -> order.put(createdCatches.get(index).getId(), index));
        List<CreatedCatch> result = entityManager
            .createQuery(
                "select createdCatch from CreatedCatch createdCatch left join fetch createdCatch.baitdata where createdCatch in :createdCatches",
                CreatedCatch.class
            )
            .setParameter(CREATEDCATCHES_PARAMETER, createdCatches)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
