package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

//if you put @RestController you can return the data to HTTP method
@RestController // if you put only @Controller you need to return view
@RequestMapping("/api/v1/task")  // general endpoints
@Tag(name="TaskController", description = "Task API")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get tasks")
    public ResponseEntity<ResponseWrapper> getTasks(){

        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get one task")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable ("id") Long id  ){

        return ResponseEntity.ok(new ResponseWrapper("Task is successfully retrieved", taskService.findById(id), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Create task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully created", HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Delete task")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully created", HttpStatus.OK));
    }

    @PutMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Update task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks") // give me all the task assign to me
    @RolesAllowed({"Employee"})
    @Operation(summary = "Employee pending task")
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
        List<TaskDTO> taskDTOS = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskDTOS, HttpStatus.OK));
    }

    @PutMapping("/employee/update/")
    @RolesAllowed({"Employee"})
    @Operation(summary = "Employee Update Task")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed({"Employee"})
    @Operation(summary = "Employee Archive Task")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks(){
        List<TaskDTO> taskDTOS = taskService.listAllTasksByStatus(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Archive Tasks are successfully retrieved", taskDTOS, HttpStatus.OK));
    }
}
