package com.martynenko.anton.company.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiWebSecurityTest {

  String contextPath = "/api/users/";

  @Autowired
  MockMvc mockMvc;


  @Test
  void onUnauthorizedGetUsersAvailableShouldReturnUnauthorized() throws Exception {
    this.mockMvc.perform(get(contextPath + "available"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void onAuthorizedGetUsersAvailableShouldReturnNotUnauthorized() throws Exception {
    assertThat(this.mockMvc.perform(get(contextPath + "available").with(jwt()))
        .andDo(print()).andReturn().getResponse().getStatus())
        .isNotEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void onUnauthorizedImportUsersShouldReturnForbidden() throws Exception {
    mockMvc.perform(multipart(contextPath + "import"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void onAuthorizedImportUsersShouldReturnNoUnauthorized() throws Exception {
    assertThat(this.mockMvc.perform(multipart(contextPath + "import")
            .with(jwt()))
        .andDo(print()).andReturn().getResponse().getStatus())
        .isNotEqualTo(HttpStatus.UNAUTHORIZED.value());
  }
}