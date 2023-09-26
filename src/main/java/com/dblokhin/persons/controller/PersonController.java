package com.dblokhin.persons.controller;

import com.dblokhin.persons.model.ErrorResponse;
import com.dblokhin.persons.model.ErrorWithMessage;
import com.dblokhin.persons.model.PersonRequest;
import com.dblokhin.persons.model.PersonResponse;
import com.dblokhin.persons.model.ValidationErrorResponse;
import com.dblokhin.persons.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/persons")
public class PersonController {
    private final PersonService personService;

    @Operation(
            summary = "Get person by ID",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Person for ID",
                        content = {@Content(schema = @Schema(implementation = PersonResponse.class))}
                ),

                @ApiResponse(
                        responseCode = "404",
                        description = "Not found Person for ID",
                        content = {@Content(schema = @Schema(implementation = ErrorWithMessage.class))}
                )
            }
    )

    @GetMapping(value = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonResponse getPerson(@PathVariable Integer id) {
        return personService.getPerson(id);
    }

    @Operation(
            summary = "Create a new person",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created a new person",
                            headers = { @Header(name = "Location", description = "Path to a new person") }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = { @Content(schema = @Schema(implementation = ValidationErrorResponse.class)) }
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPerson(@Valid @RequestBody PersonRequest request) {
        var id = personService.createPerson(request);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(id)
                        .toUri()
        ).build();
    }

    @Operation(
            summary = "Update person by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Person with ID was updated",
                            content = { @Content(schema = @Schema(implementation = PersonResponse.class)) }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = { @Content(schema = @Schema(implementation = ValidationErrorResponse.class)) }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found person for ID",
                            content = { @Content(schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PersonResponse editPerson(@PathVariable Integer id, @Valid @RequestBody PersonRequest request) {
        return personService.editPerson(id, request);
    }

    @Operation(
            summary = "Remove Person by ID",
            responses = @ApiResponse(responseCode = "204", description = "Person for ID was removed")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void editPerson(@PathVariable Integer id) {
        personService.deletePerson(id);
    }
}
