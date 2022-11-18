package com.martynenko.anton.company.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/departments/")
public class DepartmentController {

  private DepartmentService departmentService;

  @Autowired
  public DepartmentController(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  @Operation(summary = "Create new",
      description = "Create new")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
  @ApiResponses(value = {
      @ApiResponse(responseCode = HttpURLConnection.HTTP_CREATED + "",
          description = "Created",
          headers = @Header(name = "Location", description = "Location of created"),
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
  })

  @PostMapping("")
  public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentDTO created, HttpServletRequest request){
    created =  departmentService.create(created).toDTO();
    return ResponseEntity.created(URI.create(request.getRequestURI() + created.id())).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<DepartmentDTO> update(@PathVariable Long id, @RequestBody DepartmentDTO updated){
    return ResponseEntity.ok(departmentService.update(id, updated).toDTO());
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDTO> getOne(@PathVariable Long id){
    return ResponseEntity.ok(departmentService.get(id).toDTO());
  }

  @GetMapping("")
  public ResponseEntity<Collection<DepartmentDTO>> getAll(){
    List<DepartmentDTO> departmentDTOList = departmentService.listAll().stream().map(Department::toDTO).toList();
    return ResponseEntity.ok(departmentDTOList);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    departmentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
