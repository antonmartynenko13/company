package com.martynenko.anton.company.csv;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class CsvHelperTest {

  record DTO(@JsonProperty("value1") String value1,
             @JsonProperty("value2") String value2) {
    public DTO {
      Objects.requireNonNull(value1);
      Objects.requireNonNull(value2);
    }
  }

  MockMultipartFile file
      = new MockMultipartFile(
      "file",
      "file.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "value1, value2\nSome dummy value1,Some dummy value2".getBytes()
  );

  CsvHelper<DTO> csvHelper = new CsvHelper<>();

  @Test
  void shouldReturnNotEmptyCollectionOfDto() {
    Collection<DTO> dtos = csvHelper.readAll(file, DTO.class);
    assertThat(dtos).hasSize(1);
    DTO dto = dtos.iterator().next();
    assertThat(dto.value1).isEqualTo("Some dummy value1");
    assertThat(dto.value2).isEqualTo("Some dummy value2");
  }
}