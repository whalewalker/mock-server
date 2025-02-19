package com.mockserver.repository.relational.contract;

import com.mockserver.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public abstract class RelationalBaseRepo<T extends IBaseModel, R extends JpaRepository<T, UUID>> implements IRepo<T> {
    private final R repository;
    private final String entityName;

    protected RelationalBaseRepo(R repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T update(T entity) {
        return repository.findById(entity.getId())
                .filter(existing -> !existing.isDeleted())
                .map(existing -> repository.save(entity))
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " not found"));

    }

    public T getById(UUID id) {
        return repository.findById(id)
                .filter(entity -> !entity.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(entityName + "not found"));
    }

    public void delete(UUID id) {
        T entity = getById(id);
        entity.setDeleted(true);
        repository.save(entity);
    }

    public void deleteAll(List<T> entities) {
        entities.forEach(entity -> {
            T existingEntity = getById(entity.getId());
            existingEntity.setDeleted(true);
            repository.save(existingEntity);
        });
    }

    public Page<T> getAll(int pageNum, int pageSize) {
        int normalizedPageNum = Math.max(1, pageNum);
        Page<T> page = repository.findAll(
                PageRequest.of(normalizedPageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"))
        );
        return new PageImpl<>(filterDeleted(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    private List<T> filterDeleted(List<T> entities) {
        return entities.stream()
                .filter(entity -> !entity.isDeleted())
                .toList();
    }
}
