package com.gtel.api.application.service.mappers;

import java.util.List;
import java.util.Set;

public interface BaseMapper<E, D> {
    E toEntity(D d);
    D toDto(E e);
    List<E> toEntity(List<D> ds);
    List<D> toDto(List<E> es);
    Set<D> toDto(Set<E> es);
}
