package com.example.dashboardapi.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

public abstract class BaseRestController {

    protected <S, ID> ResponseEntity<S> create(S entity, JpaRepository<S, ID> repository) {
        S saved = repository.save(entity);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    protected <S, ID> ResponseEntity<S> update(ID id, S entity, JpaRepository<S, ID> repository) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        S saved = repository.save(entity);
        return ResponseEntity.ok(saved);
    }

    protected <ID> ResponseEntity<Void> delete(ID id, JpaRepository<?, ID> repository) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
