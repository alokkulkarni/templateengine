package com.alok.engine.templateengine;

import com.alok.engine.templateengine.entities.TemplateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateMasterRepository extends JpaRepository<TemplateMaster, Long> {
}
