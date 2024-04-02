package com.br.totvs.repository;

import com.br.totvs.model.Cliente;
import com.br.totvs.model.TelefoneCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para Telefone Cliente.
 *
 * @author guilherme-oliveria
 */
@Repository
public interface TelefoneClienteRepository extends JpaRepository<TelefoneCliente, Long> {
    Optional<TelefoneCliente> findByNumero(String numero);

    @Query("SELECT c FROM Cliente c JOIN c.telefoneClienteList t WHERE t.numero = :numero")
    Optional<Cliente> findClienteByNumero(@Param("numero") String numero);


}
