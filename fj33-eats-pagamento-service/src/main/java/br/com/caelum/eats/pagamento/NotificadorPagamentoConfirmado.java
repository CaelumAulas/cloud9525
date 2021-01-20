package br.com.caelum.eats.pagamento;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import br.com.caelum.eats.pagamento.AmqpPagamentoConfig.PagamentoSource;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class NotificadorPagamentoConfirmado {

  private PagamentoSource source;

  void notificaPagamentoConfirmado(Pagamento pagamento) {
    Long pagamentoId = pagamento.getId();
    Long pedidoId = pagamento.getPedidoId();
    PagamentoConfirmado confirmado = new PagamentoConfirmado(pagamentoId, pedidoId);
    //json
    //Jacksson 2.0
    //dsl = domain specific languague
    source.pagamentosConfirmados().send(MessageBuilder.withPayload(confirmado).build());
  }

}