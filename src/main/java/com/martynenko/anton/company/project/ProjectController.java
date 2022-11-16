package com.martynenko.anton.company.project;

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

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

  private final ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
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
  public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO created, HttpServletRequest request){
    created =  projectService.create(created).toDTO();
    return ResponseEntity.created(URI.create(request.getRequestURI() + created.id())).build();
  }

  @PutMapping("")
  public ResponseEntity<ProjectDTO> update(@RequestBody ProjectDTO updated){
    return ResponseEntity.ok(projectService.update(updated).toDTO());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> getOne(@PathVariable Long id){
    return ResponseEntity.ok(projectService.get(id).toDTO());
  }

  @GetMapping("")
  public ResponseEntity<Collection<ProjectDTO>> getAll(){
    List<ProjectDTO> ProjectDTOList = projectService.listAll().stream().map(Project::toDTO).toList();
    return ResponseEntity.ok(ProjectDTOList);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    projectService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
