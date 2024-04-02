package com.br.totvs.service;

import com.br.totvs.exception.CustomException;
import com.br.totvs.exception.InvalidFieldException;
import com.br.totvs.model.Cliente;
import com.br.totvs.model.TelefoneCliente;
import com.br.totvs.repository.ClienteRepository;
import com.br.totvs.service.message.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private TelefoneClienteService telefoneClienteService;

    @Mock
    private ClienteRepository repository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ClienteService clienteService;


    @Test
    public void testSave() {
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste 1").build();
        when(clienteService.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);
        assertEquals(cliente, result);
        verify(repository).save(cliente);
    }

    @Test
    public void testUpdate() throws InvalidFieldException {
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste 2").build();
        when(repository.existsById(any())).thenReturn(true);
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        Cliente result = clienteService.update(cliente);
        assertEquals(cliente, result);
        verify(repository).existsById(cliente.getId());
        verify(repository).save(cliente);
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(any());
        when(repository.existsById(any())).thenReturn(true);
        clienteService.delete(1L);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    public void testFindById() {
        Cliente cliente = new Cliente();
        when(repository.existsById(any())).thenReturn(true);
        when(repository.findById(any())).thenReturn(Optional.of(cliente));
        Optional<Cliente> result = clienteService.findById(1L);
        assertEquals(Optional.of(cliente), result);
    }

    @Test
    public void testFindAll() {
        Cliente cliente = new Cliente();
        when(repository.findAll()).thenReturn(Arrays.asList(cliente));
        assertEquals(Arrays.asList(cliente), clienteService.findAll());
    }

    @Test
    public void testarValidacaoAntesDeSalvarSemNomeNull() {
        Cliente cliente = new Cliente();
        when(messageService.getMessage("error.invalidName")).thenReturn("O nome está vazio");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateNome(cliente));

        assertEquals("O nome está vazio", exception.getMessage());
    }

    @Test
    public void testarValidacaoAntesDeSalvarSemNomeEmpty() {
        Cliente cliente = Cliente.builder().nome("").build();
        when(messageService.getMessage("error.invalidName")).thenReturn("O nome está vazio");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateNome(cliente));

        assertEquals("O nome está vazio", exception.getMessage());
    }

    @Test
    public void testarValidacaoAntesDeSalvarComNomeInvalidoLength() {
        Cliente cliente = new Cliente();
        cliente.setNome("Test");

        when(messageService.getMessage("error.invalidName.lengh")).thenReturn("O nome do cliente deve ter mais de 10 caracteres");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateNome(cliente));

        assertEquals("O nome do cliente deve ter mais de 10 caracteres", exception.getMessage());
    }

    @Test
    public void testarValidacaoDoNomeComNomeDuplicado() {
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste 2").build();
        Cliente existingCliente = Cliente.builder().id(2l).nome("Guilherme Teste 2").build();

        lenient().when(clienteService.findByNome(cliente.getNome())).thenReturn(Optional.of(existingCliente));
        when(messageService.getMessage("error.duplicateName")).thenReturn("Já existe um cliente com o mesmo nome");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateNome(cliente));

        assertEquals("Já existe um cliente com o mesmo nome", exception.getMessage());
    }

    @Test
    void testarValidacaoTelefonesComFormatoTelefoneInvalido() {
        TelefoneCliente telefoneInvalido = TelefoneCliente.builder().numero("123568").build();
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste").telefoneClienteList(Arrays.asList(telefoneInvalido)).build();

        when(messageService.getMessage("error.invalidPhone.format")).thenReturn("Formato de telefone inválido.");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateTelefones(cliente));

        assertEquals("Formato de telefone inválido.", exception.getMessage());
    }

    @Test
    void testarValidacaoTelefonesComTelefoneDuplicado() {
        TelefoneCliente telefone = TelefoneCliente.builder().numero("62990909090").build();
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste").telefoneClienteList(Arrays.asList(telefone)).build();
        Cliente existingCliente = Cliente.builder().id(2l).nome("Guilherme Teste 2").build();

        when(telefoneClienteService.findClienteByNumero("62990909090")).thenReturn(Optional.of(existingCliente));
        when(messageService.getMessage("error.duplicatePhone")).thenReturn("Número de telefone duplicado.");

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> clienteService.validateTelefones(cliente));

        assertEquals("Número de telefone duplicado.", exception.getMessage());
    }

    @Test
    void testConfiguraReferenciasPaisAutoConfiguraPaiCorretamente() {
        lenient().when(messageService.getMessage(anyString(), any())).thenReturn("Error message");

        TelefoneCliente telefoneCliente = TelefoneCliente.builder().build();
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste").telefoneClienteList(Arrays.asList(telefoneCliente)).build();

        clienteService.configParentReferencesAuto(cliente);

        assertNotNull(telefoneCliente.getCliente(), "O EnderecoCliente deve ter uma referência para Cliente como seu pai.");
        assertSame(cliente, telefoneCliente.getCliente(), "A referência ao objeto pai no EnderecoCliente não está correta.");

        verify(messageService, never()).getMessage(eq("error.accessFieldFailure"), any());
    }

    @Test
    void testSalvarComDataIntegrityViolationException() {
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste").build();
        when(repository.save(any(Cliente.class))).thenThrow(DataIntegrityViolationException.class);
        when(messageService.getMessage("error.dataIntegrityViolation", Cliente.class.getSimpleName())).thenReturn("Violação de integridade de dados");

        CustomException thrown = assertThrows(CustomException.class, () -> clienteService.salvar(cliente));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.getErrorCode());
        assertEquals("Violação de integridade de dados", thrown.getMessage());
    }

    @Test
    void testSalvarComExceptionGenerica() {
        Cliente cliente = Cliente.builder().id(1l).nome("Guilherme Teste").build();
        RuntimeException exceptionSimulada = new RuntimeException("Erro inesperado");
        when(repository.save(cliente)).thenThrow(exceptionSimulada);
        when(messageService.getMessage("error.unexpectedError", new Object[]{exceptionSimulada.getMessage()}))
                .thenReturn("Erro inesperado ao salvar a entidade");

        CustomException thrown = assertThrows(CustomException.class, () -> clienteService.salvar(cliente));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getErrorCode());
        assertEquals("Erro inesperado ao salvar a entidade", thrown.getMessage());
    }
}
