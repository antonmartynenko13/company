package com.martynenko.anton.company.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.martynenko.anton.company.user.UserDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CsvHelper<T> {

  public Collection<T> readAll(MultipartFile file, Class<T> dtoType) {
    CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
    CsvMapper mapper = new CsvMapper();
    try(BufferedReader fileReader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

      MappingIterator<T> readValues
          = mapper.readerFor(dtoType).with(bootstrapSchema).readValues(fileReader);
      return readValues.readAll();

    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
