package com.martynenko.anton.company.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martynenko.anton.company.csv.CsvHelper;
import com.martynenko.anton.company.projectposition.ProjectPosition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  private final CsvHelper<UserDTO> csvHelper;

  @Autowired
  public UserController(UserService userService, CsvHelper<UserDTO> csvHelper) {
    this.userService = userService;
    this.csvHelper = csvHelper;
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

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO updated){
    return ResponseEntity.ok(userService.update(id, updated).toDTO());
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

  @GetMapping("/available")
  public ResponseEntity<Collection<Map<String, Object>>> getAvailable(@RequestParam(defaultValue = "0") final long period){
    List<Map<String, Object>> userDTOList
        = userService.listAvailable(period).stream().map(user -> convertUserToView(user, period)).toList();
    return ResponseEntity.ok(userDTOList);
  }

  public Map<String, Object> convertUserToView(User user, long period) {

    Map<String, Object> view = new LinkedHashMap<>();
    view.put("user_details", user.toDTO());

    ProjectPosition projectPosition = user.getProjectPosition();

    LocalDate startPeriodDate = LocalDate.now();
    LocalDate endPeriodDate = startPeriodDate.plusDays(period);

    if (projectPosition == null) {
      // if no project position start date is now and user is available till the end
      view.put("available_from", startPeriodDate);
    } else {
      LocalDate positionStartDate = projectPosition.getPositionStartDate();
      LocalDate positionEndDate = projectPosition.getPositionEndDate();

      if (startPeriodDate.isBefore(positionStartDate)) {
        // if project position start date is during period he will be available from now

        view.put("available_from", startPeriodDate);

        if (endPeriodDate.isAfter(positionStartDate)) {
          // if project position start date is during period he will be available till planned project starts
          view.put("available_to", positionStartDate);
        }
      } else {
        // if project position start date is in the past he will be available after project ends

        view.put("available_from", positionEndDate);
      }
    }
    return view;
  }

  @PostMapping("/import")
  public ResponseEntity<?> create(@RequestParam MultipartFile file){
    log.info("IMPORT CONTROLLER");

    Collection<UserDTO> userDTOS = csvHelper.readAll(file, UserDTO.class);
    userService.create(userDTOS);
    return ResponseEntity.ok().build();
  }
}
