package com.mockserver.repository.relational.contract;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IRepo<T> {
    T create(T data);

    Page<T> getAll(int pageNum, int pageSize);

    T getById(UUID id);

    T update(T data);

    void delete(UUID id);

    void deleteAll(List<T> entities);
}
