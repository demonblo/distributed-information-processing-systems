package com.dblokhin.persons.model;

import jakarta.validation.constraints.NotNull;

public record PersonResponse(
        @NotNull Integer id,
        @NotNull String name,
        Integer age,
        String address,
        String work
) {}
