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

    // Optional helpers that return standard response DTOs
    protected <S, ID> ResponseEntity<com.example.dashboardapi.dto.PostResponse<S>> createWithResponse(S entity, JpaRepository<S, ID> repository) {
        S saved = repository.save(entity);
        com.example.dashboardapi.dto.PostResponse<S> resp = new com.example.dashboardapi.dto.PostResponse<>(true, saved, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    protected <S, ID> ResponseEntity<com.example.dashboardapi.dto.UpdateResponse<S>> updateWithResponse(ID id, S entity, JpaRepository<S, ID> repository) {
        if (!repository.existsById(id)) {
            com.example.dashboardapi.dto.UpdateResponse<S> resp = new com.example.dashboardapi.dto.UpdateResponse<>(false, null, "Not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        S saved = repository.save(entity);
        com.example.dashboardapi.dto.UpdateResponse<S> resp = new com.example.dashboardapi.dto.UpdateResponse<>(true, saved, "Updated");
        return ResponseEntity.ok(resp);
    }

    protected <ID> ResponseEntity<com.example.dashboardapi.dto.DeleteResponse> deleteWithResponse(ID id, JpaRepository<?, ID> repository) {
        if (!repository.existsById(id)) {
            com.example.dashboardapi.dto.DeleteResponse resp = new com.example.dashboardapi.dto.DeleteResponse(false, id, "Not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        repository.deleteById(id);
        com.example.dashboardapi.dto.DeleteResponse resp = new com.example.dashboardapi.dto.DeleteResponse(true, id, "Deleted");
        return ResponseEntity.ok(resp);
    }
}
