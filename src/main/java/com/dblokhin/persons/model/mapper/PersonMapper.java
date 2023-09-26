package com.dblokhin.persons.model.mapper;

import com.dblokhin.persons.domain.Person;
import com.dblokhin.persons.model.PersonRequest;
import com.dblokhin.persons.model.PersonResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonResponse toResponse(Person person);
    List<PersonResponse> toResponse(List<Person> person);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    Person toEntity(@MappingTarget Person entity, PersonRequest request);

    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonRequest request);


}
