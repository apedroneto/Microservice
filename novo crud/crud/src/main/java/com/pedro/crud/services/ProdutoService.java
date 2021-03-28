package com.pedro.crud.services;

import com.pedro.crud.data.vo.ProdutoVO;
import com.pedro.crud.entity.Produto;
import com.pedro.crud.exception.ResourceNotFoundException;
import com.pedro.crud.message.ProdutoSendMessage;
import com.pedro.crud.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final ProdutoSendMessage produtoSendMessage;

	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.produtoRepository = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}
	
	public ProdutoVO create(ProdutoVO produtoVO) {
	 	ProdutoVO proVoRetorno = ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
	 	this.produtoSendMessage.sendMessage(proVoRetorno);
		return proVoRetorno;
	}
	
	public Page<ProdutoVO> findAll(Pageable pageable) {
		return produtoRepository.findAll(pageable).map(this::convertToProdutoVO);
	}

	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}

	public ProdutoVO findById(Long id) {
		return ProdutoVO.create(produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));
	}
	
	public ProdutoVO update(ProdutoVO produtoVO) {
		final Optional<Produto> optionalProduto = produtoRepository.findById(produtoVO.getId());
		
		if(!optionalProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		
		return ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
	}
	
	public void delete(Long id) {
		produtoRepository.delete(produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));
	}
}
