// Clase genérica que implementa el patrón Facade para operaciones CRUD básicas.
// Permite reutilizar la lógica CRUD en los servicios del microservicio.


package com.javareto.client.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public abstract class CrudFacade<T, ID> {
    protected abstract JpaRepository<T, ID> getRepository();

    public T create(T entity) {
        return getRepository().save(entity);
    }

    public Optional<T> read(ID id) {
        return getRepository().findById(id);
    }

    public List<T> readAll() {
        return getRepository().findAll();
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }
} 