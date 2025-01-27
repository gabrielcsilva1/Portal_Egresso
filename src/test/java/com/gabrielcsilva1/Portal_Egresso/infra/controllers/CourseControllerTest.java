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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.CourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;

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
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenService tokenService;

  private Coordinator coordinator;

  private String accessToken;

  @BeforeEach
  public void setup() {
    Coordinator coordinatorToSave = Coordinator.builder()
      .login("admin")
      .password(passwordEncoder.encode("admin"))
      .build();

    this.coordinator = coordinatorRepository.save(coordinatorToSave);
  }

  @AfterEach
  public void clean() {
    coordinatorRepository.deleteAll();
  }

  @Test
  public void create_course_controller_success() throws Exception {
    accessToken = tokenService.generateToken(coordinator.getId().toString());

    CourseDTO courseDTO = new CourseDTO("Ciências da Computação", "Graduação");

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/course")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(courseDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Course> coursesOnDatabase = courseRepository.findAll();

    assertThat(coursesOnDatabase).hasSize(1);
    assertThat(coursesOnDatabase.get(0).getName()).isEqualTo(courseDTO.getName());
  }

  @Test
  public void fetch_courses_controller_success() throws Exception {
    Course courseToSave = Course.builder()
      .name("Ciências da computação")
      .coordinator(coordinator)
      .level("Graduação")
      .build();

    courseRepository.save(courseToSave);

    courseToSave = Course.builder()
      .name("Ciências da computação")
      .coordinator(coordinator)
      .level("Pós-Graduação")
      .build();
    courseRepository.save(courseToSave);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/course")
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isOk());
    
    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    List<CourseResponse> fetchCourseResponses = objectMapper.readValue(
      jsonResponse,
      new TypeReference<List<CourseResponse>>() {}
    );

    assertThat(fetchCourseResponses).hasSize(2);
  }

  @Test
  public void register_graduate_in_course_controller_success() throws Exception {
    accessToken = tokenService.generateToken(coordinator.getId().toString());

    Course courseInDatabase = Course.builder()
      .coordinator(coordinator)
      .name("Ciências da computação")
      .level("Graduação")
      .build();
    
    Graduate graduateInDatabase = Graduate.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    courseInDatabase = courseRepository.save(courseInDatabase);
    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    GraduateCourseDTO graduateCourseDTO = GraduateCourseDTO.builder()
      .courseId(courseInDatabase.getId())
      .graduateId(graduateInDatabase.getId())
      .startYear(2010)
      .endYear(2013)
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/course/graduate")
       .contentType(MediaType.APPLICATION_JSON)
       .content(objectMapper.writeValueAsString(graduateCourseDTO))
       .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
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
    accessToken = tokenService.generateToken(coordinator.getId().toString());

    Course courseInDatabase = Course.builder()
      .coordinator(coordinator)
      .name("Ciências da computação")
      .level("Graduação")
      .build();
    
    Graduate graduateInDatabase = Graduate.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    courseInDatabase = courseRepository.save(courseInDatabase);
    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    GraduateCourse graduateCourseInDatabase = GraduateCourse.builder()
      .course(courseInDatabase)
      .graduate(graduateInDatabase)
      .startYear(2010)
      .build();
    
    graduateCourseInDatabase = graduateCourseRepository.save(graduateCourseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/course/graduate/{id}", graduateCourseInDatabase.getId())
       .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    List<GraduateCourse> graduateCoursesInDatabase = graduateCourseRepository.findAll();

    assertThat(graduateCoursesInDatabase).hasSize(0);
  }

  @Test
  public void update_course_controller_success() throws Exception {
    String accessToken = tokenService.generateToken(coordinator.getId().toString());

    UpdateCourseDTO courseDTO = UpdateCourseDTO.builder()
      .name("New name")
      .level("New level")
      .build();
    
    Course courseInDatabase = Course.builder()
      .coordinator(coordinator)
      .name("Ciências da computação")
      .level("Graduação")
      .build();

    courseInDatabase = courseRepository.save(courseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/course/{id}", courseInDatabase.getId())
       .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
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
    String accessToken = tokenService.generateToken(coordinator.getId().toString());
    
    Course courseInDatabase = Course.builder()
      .coordinator(coordinator)
      .name("Ciências da computação")
      .level("Graduação")
      .build();

    courseInDatabase = courseRepository.save(courseInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/course/{id}", courseInDatabase.getId())
       .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
    );

    // Test
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    Optional<Course> databaseResponse = courseRepository.findById(courseInDatabase.getId());

    assertThat(databaseResponse.isEmpty()).isTrue();
  }

}
