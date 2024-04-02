package com.br.totvs.model;

import com.br.totvs.model.interfaces.ChildEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;

/**
 * Esta classe representa a entidade TelefoneCliente no banco de dados.
 * Ela inclui informações como id, número do telefone e a referência para a entidade Cliente.
 * A classe implementa a interface ChildEntity, indicando que ela é uma entidade filha de Cliente.
 *
 * @author guilherme-oliveria
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_telefone_cliente")
public class TelefoneCliente implements ChildEntity<Cliente>, Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero", nullable = false)
    private String numero;
    @ManyToOne
    @JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(name = "fk_telefone_cliente"))
    @JsonBackReference
    private Cliente cliente;

    @Override
    public void setParent(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return this.getClass();
    }
}
