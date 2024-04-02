package com.br.totvs.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe representa a entidade Cliente no banco de dados.
 * Ela inclui informações como id, nome, endereço, bairro e uma lista de telefones do cliente.
 *
 * @author guilherme-oliveria
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cliente")
@SequenceGenerator(name = Cliente.SEQUENCE_NAME,
        sequenceName = Cliente.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Cliente implements Entity {

    public static final String SEQUENCE_NAME = "seq_cliente";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "id")
    private Long id;
    @Column(name = "nome", nullable = false,length = 500)
    private String nome;
    @Column(name = "endereco")
    private String endereco;
    @Column(name = "bairro")
    private String bairro;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TelefoneCliente> telefoneClienteList;


    /**
     * Adiciona um telefone à lista de telefones do cliente.
     *
     * @param telefoneCliente o telefone a ser adicionado
     */
    public void addTelefone(TelefoneCliente telefoneCliente) {
        if(this.telefoneClienteList ==null){
            this.telefoneClienteList = new ArrayList<>();
        }
        telefoneClienteList.add(telefoneCliente);
    }


    /**
     * Adiciona uma lista de telefones à lista de telefones do cliente.
     *
     * @param telefoneClientea a lista de telefones a ser adicionada
     */
    public void addAllTelefonea(List<TelefoneCliente> telefoneClientea) {
        if(this.telefoneClienteList ==null){
            this.telefoneClienteList = new ArrayList<>();
        }
        telefoneClienteList.addAll(telefoneClientea);
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
