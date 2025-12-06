package com.example.tp4integrado.application.catalogo;

import com.example.tp4integrado.application.BusinessException;
import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.produto.ProdutoRepository;
import com.example.tp4integrado.domain.shared.Money;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do catálogo refatorada e coesa.
 */
@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final ProdutoRepository repository;

    private static final int MAX_REGISTROS_PERMITIDOS = 2000;

    public CatalogoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        try {
            long total = repository.count();
            if (total > MAX_REGISTROS_PERMITIDOS) {
                throw new BusinessException("Sistema temporariamente indisponível devido à alta carga. Tente novamente mais tarde.");
            }
            return repository.findAll();
        } catch (DataAccessException ex) {
            throw new BusinessException("Falha ao acessar os dados. Tente novamente mais tarde.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Produto salvar(Produto produto) {
        validarFailEarly(produto);
        try {
            return repository.save(produto);
        } catch (DataAccessException ex) {
            throw new BusinessException("Erro ao salvar produto. Tente novamente mais tarde.");
        }
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Produto existente = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
        try {
            repository.delete(existente);
        } catch (DataAccessException ex) {
            throw new BusinessException("Erro ao excluir produto. Tente novamente mais tarde.");
        }
    }

    private void validarFailEarly(Produto produto) {
        if (produto.getNome() != null && produto.getNome().toLowerCase().contains("<script")) {
            throw new BusinessException("Nome do produto contém caracteres inválidos.");
        }
        // usa Money para validar preço de forma mais clara
        if (produto.getPrecoRaw() != null) {
            Money preco = Money.of(produto.getPrecoRaw());
            if (preco.toBigDecimal().compareTo(new BigDecimal("1000000")) > 0) {
                throw new BusinessException("Preço muito alto para o produto. Verifique os dados.");
            }
        }
    }
}
