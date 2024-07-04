package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

//if you put @RestController you can return the data to HTTP method
@RestController // if you put only @Controller you need to return view
@RequestMapping("/api/v1/project")  // general endpoints
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjects() {

        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("All project")
                .success(true)
                .code(HttpStatus.OK.value())
                .data(projectService.listAllProjects()).build());
    }
    @GetMapping("{code}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("code") String code) {
        ProjectDTO byProjectCode = projectService.getByProjectCode(code);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("project is retrieve")
                .code(HttpStatus.OK.value())
                .success(true)
                .data(byProjectCode).build());
    }

    @PostMapping
    @RolesAllowed({"Admin", "Manager"})
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("Project is successfully created")
                .code(HttpStatus.CREATED.value())
                .success(true)
                .data(projectDTO).build());

    }

    @PutMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("Project is Updated")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());
    }

    @DeleteMapping("{projectCode}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable ("projectCode") String projectCode) {
    projectService.delete(projectCode);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("Proje is UPDATED")
                .code(HttpStatus.OK.value())
                .success(true)
                .build());

    }

    @GetMapping("/manager/project-status")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjectByManager(@PathVariable UserDTO manager) {
        List<ProjectDTO> collect = projectService.listAllProjectDetails();
    return ResponseEntity.ok(ResponseWrapper.builder()
            .success(true)
            .message("get project by manager")
            .code(HttpStatus.OK.value())
            .data(collect).build());
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.complete(projectCode); // when you go to company you start this one, see what needs service to be put as parameter and put as endpoints parameter and put endpoints ..reverse engineering
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Project is completed")
                .code(HttpStatus.OK.value())
                .build());
    }
}
