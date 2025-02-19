package com.mockserver.repository.relational.contract;

import java.util.UUID;

public interface IBaseModel {
    UUID getId();

    boolean isDeleted();

    void setDeleted(boolean isDeleted);
}
