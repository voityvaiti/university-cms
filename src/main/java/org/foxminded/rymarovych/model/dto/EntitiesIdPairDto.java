package org.foxminded.rymarovych.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntitiesIdPairDto {

    private Long entityId;

    private Long relatedEntityId;

    private boolean needToBeRelated;

}
