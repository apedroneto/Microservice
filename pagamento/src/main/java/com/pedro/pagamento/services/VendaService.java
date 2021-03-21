package com.pedro.pagamento.services;

import com.pedro.pagamento.entity.Produto;
import com.pedro.pagamento.entity.ProdutoVenda;
import com.pedro.pagamento.entity.Venda;
import com.pedro.pagamento.exception.ResourceNotFoundException;
import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.repository.ProdutoVendaRepository;
import com.pedro.pagamento.repository.VendaRepository;
import com.pedro.pagamento.vo.VendaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoVendaRepository produtoVendaRepository;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoVendaRepository produtoVendaRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoVendaRepository = produtoVendaRepository;
    }

    public VendaVO create(VendaVO vendaVO) {
        Venda venda = vendaRepository.save(Venda.create(vendaVO));

        List<ProdutoVenda> produtosSalvos = new ArrayList<>();

        vendaVO.getProdutos().forEach(p -> {
            ProdutoVenda pv = ProdutoVenda.create(p);
            pv.setVenda(venda);
            produtosSalvos.add(produtoVendaRepository.save(pv));
        });

        venda.setProdutos(produtosSalvos);

        return VendaVO.create(venda);
    }

    public Page<VendaVO> findAll(Pageable pageable) {
        Page<Venda> page = vendaRepository.findAll(pageable);
        return page.map(this::convertToProdutoVO);
    }

    private VendaVO convertToProdutoVO(Venda produto) {
        return VendaVO.create(produto);
    }

    public VendaVO findById(Long id) {
        return VendaVO.create(vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));
    }
}
