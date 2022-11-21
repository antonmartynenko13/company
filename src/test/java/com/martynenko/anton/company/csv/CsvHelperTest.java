package com.martynenko.anton.company.csv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class CsvHelperTest {

  @ToString
  final static class Dto {
    private final String value1;
    private final String value2;

    public Dto(@JsonProperty("value1") String value1, @JsonProperty("value2") String value2) {
      this.value1 = value1;
      this.value2 = value2;
    }
  }

  MockMultipartFile file
      = new MockMultipartFile(
      "file",
      "file.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "value1, value2\nSome dummy value1,Some dummy value2".getBytes()
  );

  CsvHelper<Dto> csvHelper = new CsvHelper<>();

  @Test
  void shouldReturnNotEmptyCollectionOfDto() {
    Collection<Dto> dtos = csvHelper.readAll(file, Dto.class);
    assertThat(dtos).hasSize(1);
    Dto dto = dtos.iterator().next();
    assertThat(dto.value1).isEqualTo("Some dummy value1");
    assertThat(dto.value2).isEqualTo("Some dummy value2");
  }
}