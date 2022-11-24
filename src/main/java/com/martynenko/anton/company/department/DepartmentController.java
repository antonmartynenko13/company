package com.martynenko.anton.company.department;

import com.martynenko.anton.company.openapi.CrudCreate;
import com.martynenko.anton.company.openapi.CrudDelete;
import com.martynenko.anton.company.openapi.CrudGetAll;
import com.martynenko.anton.company.openapi.CrudGetOne;
import com.martynenko.anton.company.openapi.CrudUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "departments")
@RestController
@RequestMapping("/api/departments/")
public class DepartmentController {

  private DepartmentService departmentService;

  @Autowired
  public DepartmentController(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  @CrudCreate
  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity create(@RequestBody DepartmentDTO created, HttpServletRequest request){
    created =  departmentService.create(created).toDTO();
    return ResponseEntity.created(URI.create(request.getRequestURI() + created.id())).build();
  }

  @CrudUpdate
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DepartmentDTO> update(@PathVariable Long id, @RequestBody DepartmentDTO updated){
    return ResponseEntity.ok(departmentService.update(id, updated).toDTO());
  }

  @CrudGetOne
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DepartmentDTO> getOne(@PathVariable Long id){
    return ResponseEntity.ok(departmentService.get(id).toDTO());
  }

  @CrudGetAll
  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<DepartmentDTO>> getAll(){
    List<DepartmentDTO> departmentDTOList = departmentService.listAll().stream().map(Department::toDTO).toList();
    return ResponseEntity.ok(departmentDTOList);
  }

  @CrudDelete
  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable Long id){
    departmentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
