package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Lesson;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.LessonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LessonService lessonService;


    private static Lesson lesson;

    @BeforeAll
    static void setUp() throws ParseException {
        Course course = new Course(3L, "coursename", null, null, null);
        Teacher teacher = new Teacher(12L, "firstname", "lastname", "degree", null, null);

        lesson = new Lesson(4L, 2, "Zoom", new SimpleDateFormat("yyyyMMdd").parse("20200424"), course, teacher, null);
    }


    @Test
    void create_shouldAddLessonAndLinkGroup_ifGroupIdParamIsPresent() throws Exception {
        String groupId = "5";

        when(lessonService.add(lesson)).thenReturn(lesson);

        mvc.perform(post("/lessons/new")
                .flashAttr("lesson", lesson)
                .param("groupId", groupId));

        verify(lessonService).add(lesson);
        verify(lessonService).linkGroup(lesson.getId(), Long.parseLong(groupId));

    }

    @Test
    void create_shouldAddLessonAndDoNotLinkGroup_ifGroupIdParamIsAbsent() throws Exception {

        when(lessonService.add(lesson)).thenReturn(lesson);

        mvc.perform(post("/lessons/new")
                .flashAttr("lesson", lesson));

        verify(lessonService).add(lesson);
        verify(lessonService, never()).linkGroup(any(), any());

    }

    @Test
    void editIfCourseIsPresent() throws Exception {

        when(lessonService.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        mvc.perform(get("/lessons/edit/" + lesson.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lesson"))
                .andExpect(model().attribute("lesson", lesson));

        verify(lessonService).findById(lesson.getId());
    }

    @Test
    void editIfCourseIsAbsent() throws Exception {
        Long id = 10L;

        when(lessonService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/lessons/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));

        verify(lessonService).findById(id);
    }

    @Test
    void linkGroupIfActionIsLink() throws Exception {
        String action = "link";

        String lessonId = "8";
        String groupId = "1";

        mvc.perform(post("/lessons/group-relation/" + action)
                .param("lessonId", lessonId)
                .param("groupId", groupId));

        verify(lessonService).linkGroup(Long.parseLong(lessonId), Long.parseLong(groupId));
    }

    @Test
    void unlinkGroupIfActionIsUnlink() throws Exception {
        String action = "unlink";

        String lessonId = "21";
        String groupId = "8";

        mvc.perform(post("/lessons/group-relation/" + action)
                .param("lessonId", lessonId)
                .param("groupId", groupId));

        verify(lessonService).unlinkGroup(Long.parseLong(lessonId), Long.parseLong(groupId));
    }
}