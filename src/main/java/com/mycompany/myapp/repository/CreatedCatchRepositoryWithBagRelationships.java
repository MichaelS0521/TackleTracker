package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CreatedCatch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CreatedCatchRepositoryWithBagRelationships {
    Optional<CreatedCatch> fetchBagRelationships(Optional<CreatedCatch> createdCatch);

    List<CreatedCatch> fetchBagRelationships(List<CreatedCatch> createdCatches);

    Page<CreatedCatch> fetchBagRelationships(Page<CreatedCatch> createdCatches);
}
