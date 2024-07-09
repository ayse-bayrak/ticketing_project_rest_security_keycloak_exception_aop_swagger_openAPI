package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

//if you put @RestController you can return the data to HTTP method
@RestController // if you put only @Controller you need to return view
@RequestMapping ("/api/v1/user")  // general endpoints
 // this is make up stuff
public class UserController {
    private final UserService userService; // all the time we are injecting intercae not implementation class
    private final MapperUtil mapperUtil;

    public UserController(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }
// we want to modify our status code or we might need to pass header to Json, that's why we use ResponseEntity
    // ResponseEntity is generic class we need to provide something class
    // I want to see that custom output as a Json

    // in the most of the company for all CRUD operation (get something, create, update, delete something is only work with one base endpoint (in the class level))
    ///api/v1/user this endpint will work for all get, put, post, delete
    @ExecutionTime
    @GetMapping
    @RolesAllowed({"Manager", "Admin"})
    @Operation(summary = "Get users")
    public ResponseEntity<ResponseWrapper> getUsers(){
       // ResponseWrapper responseWrapper = new ResponseWrapper("All users are retrieved", userService.listAllUsers(), HttpStatus.OK);
        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .success(true)
                .message("All users are retrieved")
                .code(HttpStatus.OK.value())
                .data(userService.listAllUsers()).build();

       return ResponseEntity.ok(responseWrapper);
    }
//ResponseEntity. ok -- ok means StatusCode in the Postman Response part
    //Http.StatusCode.ok -- ok means we are gonna see the body  also HTTP status
    @ExecutionTime
    @GetMapping("/{username}")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Get by userName")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable ("username")String userName){

        UserDTO foundUserDTO = userService.findByUserName(userName);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("User is retrieved")
                .code(HttpStatus.OK.value())
                .data(foundUserDTO).build());

    }


    @PostMapping()
    @RolesAllowed({"Admin"})
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO){ // How am I gonna catch this username ? with @PathVariable
        userService.save(userDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseWrapper.builder()
                            .success(true)
                            .code(HttpStatus.CREATED.value())
                            .message("one user is created")
                            .build());
    }

    @PutMapping("/{userName}") // OZZY  don't put endpoint here.. he use only @RequestBody UserDTO userDTO as a parameter
    @RolesAllowed({"Admin"})
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO, @PathVariable("userName") String userName){

        //UserDTO findUser = userService.findByUserName(userName);
       // userDTO.setUserName(userName);
        UserDTO updatedUser = userService.update(userDTO);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Updated")
                .data(updatedUser)
                .build());
    }
    @DeleteMapping("/{userName}")
    @RolesAllowed({"Admin"})
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("userName") String userName) throws TicketingProjectException {

        userService.delete(userName);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("deleted")
                .build());
    }





}
/*
IQ : you have a one interface but you might have a bunch of implementation classes
How you specify which one?
-we can do this one different way,
-we can create bunch of classes and obly we can @Service the one class without defining the @component the other classes
-if we put @component to all classes , we can specify one of them the @Qualifier or @Primary
 */