package com.dblokhin.persons.service;

import com.dblokhin.persons.model.PersonRequest;
import com.dblokhin.persons.model.PersonResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonService {
    PersonResponse getPerson(Integer id);
    List<PersonResponse> getPersons();
    Integer createPerson(PersonRequest request);
    PersonResponse editPerson(Integer id, PersonRequest personRequest);
    void deletePerson(Integer id);
}
