package org.foxminded.rymarovych.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foxminded.rymarovych.model.Lesson;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonsForDayDto {

    private List<Lesson> lessons;

    private Date date;

}
