package com.dblokhin.persons.unit;

import com.dblokhin.persons.controller.PersonController;
import com.dblokhin.persons.domain.Person;
import com.dblokhin.persons.model.PersonRequest;
import com.dblokhin.persons.model.PersonResponse;
import com.dblokhin.persons.model.mapper.PersonMapper;
import com.dblokhin.persons.model.mapper.PersonMapperImpl;
import com.dblokhin.persons.repository.PersonRepository;
import com.dblokhin.persons.service.PersonService;
import com.dblokhin.persons.service.PersonServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SpringBootTest(classes = { PersonController.class, PersonService.class, PersonServiceImpl.class, PersonMapper.class, PersonMapperImpl.class})
public class PersonControllerTest {
    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonController personController;

    @Test
    public void on_find_person_return_id_test() {
        Integer id = 1;
        Person persistedPerson = Person.builder()
                .id(id)
                .name("TestName")
                .age(13)
                .address("TestAddress")
                .work("TestWork")
                .build();

        PersonResponse expectedResponse = new PersonResponse(
                persistedPerson.getId(),
                persistedPerson.getName(),
                persistedPerson.getAge(),
                persistedPerson.getAddress(),
                persistedPerson.getWork()
        );

        Mockito.when(personRepository.findById(id))
                .thenReturn(Optional.of(persistedPerson));

        PersonResponse actualResponse = personController.getPerson(id);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void on_get_unexsisting_person_throws_entity_not_found() {
        Integer id = 1;
        Mockito.when(personRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                personController.getPerson(id)
        );
    }

    @Test
    public void on_get_person_with_ID_equals_null() {
        Integer id = 1;
        Person persistedPerson = Person.builder()
                .id(null)
                .name("TestName")
                .age(15)
                .address("TestAddress")
                .work("Student")
                .build();

        Mockito.when(personRepository.findById(id))
                .thenReturn(Optional.of(persistedPerson));

        Assertions.assertThrows(ValidationException.class,
                () -> personController.getPerson(id)
        );
    }

    @Test
    public void on_create_person_return_created_id_test() {
        PersonRequest sampleRequest = new PersonRequest("TestName", 13, "Saratov", "Worker");
        Integer expectedId = 1;
        Person.PersonBuilder testPersonTemplate = Person.builder()
                .name("TestName")
                .age(13)
                .address("Saratov")
                .work("Worker");

        Mockito.when(personRepository.save(testPersonTemplate.build()))
                .thenReturn(testPersonTemplate.id(expectedId).build());


        Integer generatedId = Integer.parseInt(personController.createPerson(sampleRequest).getHeaders().getLocation().getPath().substring(1));

        Assertions.assertNotNull(generatedId);
        Assertions.assertEquals(expectedId, generatedId);
    }

    @Test
    public void on_create_if_ID_equals_null_throw_exception_test() {
        PersonRequest sampleRequest = new PersonRequest("TestName", 13, "Saratov", "Worker");
        Mockito.when(personRepository.save(Person.builder().build()))
                .thenReturn(Person.builder().id(null).build());

        Assertions.assertThrows(NullPointerException.class, () ->
                personController.createPerson(sampleRequest));
    }

    @Test
    public void on_create_if_null_throw_exception_test() {
        PersonRequest sampleRequest = new PersonRequest("TestName", 13, "Saratov", "Worker");
        Mockito.when(personRepository.save(Person.builder().build()))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () ->
                personController.createPerson(sampleRequest));
    }
}
