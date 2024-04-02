package com.br.totvs.mapper;

import java.util.List;

/**
 * Interface para mapeamento de entidades e DTOs.
 * @param <E> Entidade
 * @param <D> DTO
 */
public interface EntityMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);
    List<E> toEntities(List<D> dtoList);
    List<D> toDtos(List<E> entityList);
}