package com.br.totvs.model.interfaces;

/**
 * Esta interface define um contrato para entidades que são filhas de uma entidade pai.
 * Ela inclui um método para definir a entidade pai de uma entidade filha.
 *
 * @param <PARENT> o tipo da entidade pai
 */
public interface ChildEntity<PARENT> {
    /**
     * Define a entidade pai desta entidade filha.
     *
     * @param parent a entidade pai
     */
    void setParent(PARENT parent);
}