package com.pedro.crud.message;

import com.pedro.crud.data.vo.ProdutoVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProdutoSendMessage {

    @Value("${crud.rabbitmq.exchange}")
    String exchange;

    @Value("${crud.rabbitmq.routingkey}")
    String routingkey;

    public final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProdutoSendMessage(RabbitTemplate rabbitTemplate) {
        super();
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ProdutoVO produtoVO) {
        this.rabbitTemplate.convertAndSend(exchange, routingkey, produtoVO);
    }

}
