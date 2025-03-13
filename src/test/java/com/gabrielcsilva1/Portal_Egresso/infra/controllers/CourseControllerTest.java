package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.course.RequestCreateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.course.RequestUpdateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduateCourse.RequestGraduateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.course.ResponseShortCourseJson;
import com.gabrielcsilva1.utils.faker.FakeCoordinatorFactory;
import com.gabrielcsilva1.utils.faker.FakeCourseFactory;
import com.gabrielcsilva1.utils.faker.FakeGraduateFactory;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CourseControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private GraduateCourseRepository graduateCourseRepository;

  @Autowired
  private TokenService tokenService;

  private Coordinator coordinator;

  private Cookie cookie;

  @BeforeEach
  public void setup() {
    Coordinator coordinatorToSave = FakeCoordinatorFactory.makeCoordinator();
    this.coordinator = coordinatorRepository.save(coordinatorToSave);
  }

  @AfterEach
  public void clean() {
    coordinatorRepository.deleteAll();
  }

  private void authenticateCoordinator() {
    var token = tokenService.generateToken(coordinator.getId().toString(), coordinator.getRoles());
    this.cookie = new Cookie("jwtToken", token);
  }

  @Test
  public void create_course_controller_success() throws Exception {
    authenticateCoordinator();

    RequestCreateCourseJson courseDTO = new RequestCreateCourseJson("Ciências da Computação", "Graduação");

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/course")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(courseDTO))
        .cookie(cookie)
    );

    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Course> coursesOnDatabase = courseRepository.findAll();

    assertThat(coursesOnDatabase).hasSize(1);
    assertThat(coursesOnDatabase.get(0).getName()).isEqualTo(courseDTO.getName());
  }

  @Test
  public void fetch_courses_controller_success() throws Exception {
    Course courseInDatabase1 = FakeCourseFactory.makeCourse(coordinator);
    courseRepository.save(courseInDatabase1);

    Course courseInDatabase2 = FakeCourseFactory.makeCourse(coordinator);
    courseRepository.save(courseInDatabase2);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/course")
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isOk());
    
    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    List<ResponseShortCourseJson> fetchCourseResponses = objectMapper.readValue(
      jsonResponse,
      new TypeReference<List<ResponseShortCourseJson>>() {}
    );

    assertThat(fetchCourseResponses).hasSize(2);
  }

  @Test
  public void register_graduate_in_course_controller_success() throws Exception {
    authenticateCoordinator();

    Course courseInDatabase = FakeCourseFactory.makeCourse(coordinator);
    courseInDatabase = courseRepository.save(courseInDatabase);
  
    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();
    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    RequestGraduateCourseJson graduateCourseDTO = RequestGraduateCourseJson.builder()
      .courseId(courseInDatabase.getId())
      .graduateId(graduateInDatabase.getId())
      .startYear(2010)
      .endYear(2013)
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/course/graduate")
       .contentType(MediaType.APPLICATION_JSON)
       .content(objectMapper.writeValueAsString(graduateCourseDTO))
       .cookie(cookie)
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<GraduateCourse> graduateCoursesInDatabase = graduateCourseRepository.findAll();

    assertThat(graduateCoursesInDatabase).hasSize(1);
    assertThat(graduateCoursesInDatabase.get(0).getCourse().getId()).isEqualTo(courseInDatabase.getId());
    assertThat(graduateCoursesInDatabase.get(0).getGraduate().getId()).isEqualTo(graduateInDatabase.getId());
  }

  @Test
  public void unregister_graduate_in_course_controller_success() throws Exception {
    authenticateCoordinator();
    Course courseInDatabase = FakeCourseFactory.makeCourse(coordinator);
    
    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();

    courseInDatabase = courseRepository.save(courseInDatabase);
    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    GraduateCourse graduateCourseInDatabase = GraduateCourse.builder()
      .course(courseInDatabase)
      .graduate(graduateInDatabase)
      .startYear(2010)
      .build();
    
    graduateCourseInDatabase = graduateCourseRepository.save(graduateCourseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/course/{courseId}/graduate/{graduateId}", courseInDatabase.getId(),graduateInDatabase.getId())
       .cookie(cookie)
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    List<GraduateCourse> graduateCoursesInDatabase = graduateCourseRepository.findAll();

    assertThat(graduateCoursesInDatabase).hasSize(0);
  }

  @Test
  public void update_course_controller_success() throws Exception {
    authenticateCoordinator();
    RequestUpdateCourseJson courseDTO = RequestUpdateCourseJson.builder()
      .name("New name")
      .level("New level")
      .build();
    
    Course courseInDatabase = FakeCourseFactory.makeCourse(coordinator);

    courseInDatabase = courseRepository.save(courseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/course/{id}", courseInDatabase.getId())
       .cookie(cookie)
       .contentType(MediaType.APPLICATION_JSON)
       .content(objectMapper.writeValueAsString(courseDTO))
    );

    // Test
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    courseInDatabase = courseRepository.findById(courseInDatabase.getId()).orElse(null);

    assertThat(courseInDatabase.getName()).isEqualTo(courseDTO.getName());
    assertThat(courseInDatabase.getLevel()).isEqualTo(courseDTO.getLevel());
  }

  @Test
  public void delete_course_controller_success() throws Exception {
    authenticateCoordinator();

    Course courseInDatabase = FakeCourseFactory.makeCourse(coordinator);

    courseInDatabase = courseRepository.save(courseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/course/{id}", courseInDatabase.getId())
       .cookie(cookie)
    );

    // Test
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    Optional<Course> databaseResponse = courseRepository.findById(courseInDatabase.getId());

    assertThat(databaseResponse.isEmpty()).isTrue();
  }

}
