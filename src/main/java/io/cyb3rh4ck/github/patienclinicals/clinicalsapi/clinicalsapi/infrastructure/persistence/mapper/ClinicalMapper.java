package io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.mapper;

import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.domain.model.Clinical;
import io.cyb3rh4ck.github.patienclinicals.clinicalsapi.clinicalsapi.infrastructure.persistence.entity.ClinicalEntity;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class ClinicalMapper {
    
    public Clinical toDomain(ClinicalEntity entity) {
        if (entity == null) return null;
        
        Clinical clinical = new Clinical();
        clinical.setId(entity.getId());
        clinical.setPatientId(entity.getPatient() != null ? entity.getPatient().getId() : null);
        clinical.setComponentName(entity.getComponentName());
        clinical.setComponentValue(entity.getComponentValue());
        clinical.setMeasuredDateTime(entity.getMeasuredDateTime() != null ? 
            entity.getMeasuredDateTime().toLocalDateTime() : null);
        
        return clinical;
    }

    public ClinicalEntity toEntity(Clinical domain) {
        if (domain == null) return null;
        
        ClinicalEntity entity = new ClinicalEntity();
        entity.setId(domain.getId());
        entity.setComponentName(domain.getComponentName());
        entity.setComponentValue(domain.getComponentValue());
        entity.setMeasuredDateTime(domain.getMeasuredDateTime() != null ? 
            Timestamp.valueOf(domain.getMeasuredDateTime()) : null);
        
        return entity;
    }
}