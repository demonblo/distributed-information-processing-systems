package com.dblokhin.persons.service;

import com.dblokhin.persons.model.PersonRequest;
import com.dblokhin.persons.model.PersonResponse;
import com.dblokhin.persons.model.mapper.PersonMapper;
import com.dblokhin.persons.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPerson(Integer id) {
        PersonResponse response = personRepository.findById(id)
                .map(personMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Person with id: %d not found", id)));

        return validatePersonResponse(response);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonResponse> getPersons() {
        return personMapper.toResponse(
                personRepository.findAll());
    }

    @Override
    @Transactional
    public Integer createPerson(PersonRequest request) {
        return requireNonNull(
                personRepository.save(
                        personMapper.toEntity(request)).getId());
    }

    @Override
    @Transactional
    public PersonResponse editPerson(Integer id, PersonRequest personRequest) {
        PersonResponse response = personMapper.toResponse(personRepository.save(personMapper.toEntity(
                personRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("Person with id: %d not found", id))),
                personRequest)));

        return validatePersonResponse(response);
    }

    private PersonResponse validatePersonResponse(PersonResponse response) {
        var violations = validator.validate(response);
        for (var violation : violations) {
            log.error("{} {}",violation.getPropertyPath().toString(), violation.getMessage());
        }

        if (violations.size() != 0) {
            throw new ValidationException("Response is not valid");
        }

        return response;
    }

    @Override
    @Transactional
    public void deletePerson(Integer id) {
        personRepository.deleteById(id);
    }
}
