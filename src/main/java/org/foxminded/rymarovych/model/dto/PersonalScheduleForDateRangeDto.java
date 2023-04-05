package org.foxminded.rymarovych.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalScheduleForDateRangeDto {

    private Long entityId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rangeStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rangeEndDate;

}
