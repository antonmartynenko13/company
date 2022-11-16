package com.martynenko.anton.company.user;

import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.department.DepartmentDTO;
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
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
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
  public ResponseEntity<UserDTO> create(@RequestBody UserDTO created, HttpServletRequest request){
    log.info("CREATED CONTROLLER");
    created =  userService.create(created).toDTO();
    return ResponseEntity.created(URI.create(request.getRequestURI() + created.id())).build();
  }

  @PutMapping("")
  public ResponseEntity<UserDTO> update(@RequestBody UserDTO updated){
    return ResponseEntity.ok(userService.update(updated).toDTO());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getOne(@PathVariable Long id){
    return ResponseEntity.ok(userService.get(id).toDTO());
  }

  @GetMapping("")
  public ResponseEntity<Collection<UserDTO>> getAll(){
    List<UserDTO> userDTOList = userService.listAll().stream().map(User::toDTO).toList();
    return ResponseEntity.ok(userDTOList);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}