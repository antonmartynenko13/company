package com.martynenko.anton.company.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.department.DepartmentDTO;
import com.martynenko.anton.company.department.DepartmentRepository;
import com.martynenko.anton.company.project.ProjectDTO;
import com.martynenko.anton.company.project.ProjectRepository;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
  final String contextPath = "/api/users/";

  final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  DepartmentRepository departmentRepository;

  @Test
  void onCreateShouldReturnCreatedWithLocationHeaderOn() throws Exception {
    long departmentId = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance()).getId();
    Map<String, String> payloadMap = Map.of(
        "firstName", "First",
        "lastName", "Last",
        "email", "email@domain.com",
        "password", "PASSWORD",
        "jobTitle", "employee",
        "departmentId", String.valueOf(departmentId)
    );

    this.mockMvc.perform(post(contextPath)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern(contextPath + "*"));
  }

  @Test
  void onCreateWithDuplicationShouldReturnBadRequest() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    userRepository.save(new UserDTO(
        null,
        "First",
        "Last",
        "email@domain.com",
        "PASSWORD",
        "employee",
        department.getId()
        ).createInstance(department));

    //duplication of unique title
    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        //email is unique
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(post(contextPath)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void onCreateWithoutMandatoryFieldShouldReturnBadRequest() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        //no last name
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(post(contextPath)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void onCreateWithoutMandatoryRelationShouldReturnNotFound() throws Exception {

    //duplication of unique title
    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        //no such department
        "departmentId", "100"
    );

    this.mockMvc.perform(post(contextPath)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void onUpdateShouldReturnOkWithSameEntity() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    long id = userRepository.save(new UserDTO(
        null,
        "First",
        "Last",
        "email@domain.com",
        "PASSWORD",
        "employee",
        department.getId()
    ).createInstance(department)).getId();

    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(put(contextPath + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
  }

  /*
   * Need this workaround to prevent update with duplicate value inside transaction
   * And I still want to use declaration initialization, so @DirtiesContext will crush context
   * */

  @Test
  @Transactional(propagation = Propagation.NEVER)
  void onUpdateWithDuplicationShouldReturnBadRequest() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    userRepository.save(new UserDTO(
        null,
        "First",
        "Last",
        "email@domain.com",
        "PASSWORD",
        "employee",
        department.getId()
    ).createInstance(department)).getId();

    long id = userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        //another email
        "email2@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department)).getId();

    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        //duplication
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(put(contextPath + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isBadRequest());

    userRepository.deleteAll();
    departmentRepository.deleteAll();
  }

  @Test
  void onUpdateWithoutRequiredFieldShouldReturnBadRequest() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    long id = userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        "email@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department)).getId();

    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        //no lastname
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(put(contextPath + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void onUpdateWithoutMandatoryRelationShouldReturnNotFound() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    long id = userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        "email@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department)).getId();

    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        //no such department
        "departmentId", "100"
    );

    this.mockMvc.perform(put(contextPath + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void onUpdateWithMissingIdShouldReturnNotFound() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());
    long missingId = 0;
    Map<String, String> payloadMap = Map.of(
        "firstName", "First2",
        "lastName", "Last2",
        "email", "email@domain.com",
        "password", "PASSWORD2",
        "jobTitle", "employee2",
        "departmentId", String.valueOf(department.getId())
    );

    this.mockMvc.perform(put(contextPath + missingId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(payloadMap)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void onGetShouldReturnOkWithSingleEntity() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    long id = userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        "email@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department)).getId();

    this.mockMvc.perform(get(contextPath + id))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
  }

  @Test
  void onGetWithMissingIdShouldReturnNotFound() throws Exception {
    long missingId = 0;

    this.mockMvc.perform(get(contextPath + missingId))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void onDeleteShouldReturnNoContent() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    long id = userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        "email@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department)).getId();

    this.mockMvc.perform(delete(contextPath + id))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void onDeleteWithMissingIdShouldReturnNotFound() throws Exception {
    long missingId = 0;

    this.mockMvc.perform(delete(contextPath + missingId))
        .andDo(print())
        .andExpect(status().isNotFound());
  }


  @Test
  void onGetAllShouldReturnOkWithJsonListOfEntities() throws Exception {
    Department department = departmentRepository.save(new DepartmentDTO(null,"Department1").createInstance());

    userRepository.save(new UserDTO(
        null,
        "First2",
        "Last2",
        "email@domain.com",
        "PASSWORD2",
        "employee2",
        department.getId()
    ).createInstance(department));

    this.mockMvc.perform(get(contextPath))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty());
  }

}