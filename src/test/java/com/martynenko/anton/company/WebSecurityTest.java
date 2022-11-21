package com.martynenko.anton.company;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityTest {
  static List<String> crudContexts = List.of(
      "/api/departments/",
      "/api/projects/",
      "/api/users/",
      "/api/project-positions/"
  );

  @Autowired
  MockMvc mockMvc;


  @Test
  void onUnauthorizedRequestsShouldReturnForbidden()
      throws Exception {
    for (String contextPath: crudContexts){
      this.mockMvc.perform(post(contextPath))
          .andDo(print())
          .andExpect(status().isForbidden());

      this.mockMvc.perform(put(contextPath))
          .andDo(print())
          .andExpect(status().isForbidden());

      this.mockMvc.perform(delete(contextPath))
          .andDo(print())
          .andExpect(status().isForbidden());
    }
  }

  @Test
  void onUnauthorizedRequestsShouldReturnUnauthorized()
      throws Exception {
    for (String contextPath: crudContexts){
      this.mockMvc.perform(get(contextPath ))
          .andDo(print())
          .andExpect(status().isUnauthorized());
    }
  }

  @Test
  void onAuthorizedRequestsShouldNotReturnForbidden() throws Exception {

    for (String contextPath: crudContexts){
      assertThat(this.mockMvc.perform(post(contextPath).with(jwt()))
          .andDo(print()).andReturn().getResponse()
          .getStatus()).isNotEqualTo(HttpStatus.FORBIDDEN.value());

      assertThat(this.mockMvc.perform(put(contextPath).with(jwt()))
          .andDo(print()).andReturn().getResponse()
          .getStatus()).isNotEqualTo(HttpStatus.FORBIDDEN.value());

      assertThat(this.mockMvc.perform(delete(contextPath).with(jwt()))
          .andDo(print()).andReturn().getResponse()
          .getStatus()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }
  }

  @Test
  void onAuthorizedRequestsShouldNotReturnUnauthorized() throws Exception {
    for (String contextPath: crudContexts){
      assertThat(this.mockMvc.perform(get(contextPath).with(jwt()))
          .andDo(print()).andReturn().getResponse()
          .getStatus()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }
  }

}
