package com.martynenko.anton.company.projectposition;

import com.martynenko.anton.company.openapi.CrudCreate;
import com.martynenko.anton.company.openapi.CrudCreateWithRelations;
import com.martynenko.anton.company.openapi.CrudDelete;
import com.martynenko.anton.company.openapi.CrudGetAll;
import com.martynenko.anton.company.openapi.CrudGetOne;
import com.martynenko.anton.company.openapi.CrudUpdate;
import com.martynenko.anton.company.openapi.CrudUpdateWithRelations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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

@Tag(name = "project-positions")
@RestController
@RequestMapping("/api/project-positions")
public class ProjectPositionController {
  private final ProjectPositionService projectPositionService;
  
  @Autowired
  public ProjectPositionController(ProjectPositionService projectPositionService) {
    this.projectPositionService = projectPositionService;
  }

  @CrudCreateWithRelations
  @PostMapping("")
  public ResponseEntity<ProjectPositionDTO> create(@RequestBody ProjectPositionDTO created, HttpServletRequest request){
    created =  projectPositionService.create(created).toDTO();
    return ResponseEntity.created(URI.create(request.getRequestURI() + created.id())).build();
  }

  @CrudUpdateWithRelations
  @PutMapping("/{id}")
  public ResponseEntity<ProjectPositionDTO> update(@PathVariable Long id, @RequestBody ProjectPositionDTO updated){
    return ResponseEntity.ok(projectPositionService.update(id, updated).toDTO());
  }

  @CrudGetOne
  @GetMapping("/{id}")
  public ResponseEntity<ProjectPositionDTO> getOne(@PathVariable Long id){
    return ResponseEntity.ok(projectPositionService.get(id).toDTO());
  }

  @CrudGetAll
  @GetMapping("")
  public ResponseEntity<Collection<ProjectPositionDTO>> getAll(){
    List<ProjectPositionDTO> ProjectPositionDTOList
        = projectPositionService.listAll().stream().map(ProjectPosition::toDTO).toList();
    return ResponseEntity.ok(ProjectPositionDTOList);
  }

  @CrudDelete
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    projectPositionService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
