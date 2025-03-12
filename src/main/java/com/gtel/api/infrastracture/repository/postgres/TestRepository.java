package com.gtel.api.infrastracture.repository.postgres;

import com.gtel.api.domains.models.postgres.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, String > {
    Test findByName(String name);
}
