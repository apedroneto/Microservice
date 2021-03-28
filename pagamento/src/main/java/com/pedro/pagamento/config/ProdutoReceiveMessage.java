package com.pedro.pagamento.config;

import com.pedro.pagamento.entity.Produto;
import com.pedro.pagamento.repository.ProdutoRepository;
import com.pedro.pagamento.vo.ProdutoVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ProdutoReceiveMessage {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoReceiveMessage(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @RabbitListener(queues = {"${crud.rabbitmq.queue}"})
    public void receive(@Payload ProdutoVO produtoVO) {
        this.produtoRepository.save(Produto.create(produtoVO));
    }

}
