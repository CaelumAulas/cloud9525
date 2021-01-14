package br.com.caelum.eats.pagamento;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.eats.pagamento.PagamentoDto;
import br.com.caelum.eats.exception.ResourceNotFoundException;
import br.com.caelum.eats.pagamento.Pagamento;
import br.com.caelum.eats.model.Pedido;
import br.com.caelum.eats.pagamento.PagamentoRepository;
import br.com.caelum.eats.repository.PedidoRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
public class PagamentoController {

	private PagamentoRepository pagamentoRepo;
	private PedidoRepository pedidoRepo;

	@GetMapping("/{id}")
	public PagamentoDto detalha(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		return new PagamentoDto(pagamento);
	}

	@PostMapping
	public ResponseEntity<PagamentoDto> cria(@RequestBody Pagamento pagamento, UriComponentsBuilder uriBuilder) {
		pagamento.setStatus(Pagamento.Status.CRIADO);
		Pagamento salvo = pagamentoRepo.save(pagamento);
		URI path = uriBuilder.path("/pagamentos/{id}").buildAndExpand(salvo.getId()).toUri();
		return ResponseEntity.created(path).body(new PagamentoDto(salvo));
	}

	@PutMapping("/{id}")
	public PagamentoDto confirma(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		pagamento.setStatus(Pagamento.Status.CONFIRMADO);
		pagamentoRepo.save(pagamento);
		Long pedidoId = pagamento.getPedido().getId();
		Pedido pedido = pedidoRepo.porIdComItens(pedidoId);
		pedido.setStatus(Pedido.Status.PAGO);
		pedidoRepo.atualizaStatus(Pedido.Status.PAGO, pedido);
		return new PagamentoDto(pagamento);
	}

	@DeleteMapping("/{id}")
	public PagamentoDto cancela(@PathVariable("id") Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		pagamento.setStatus(Pagamento.Status.CANCELADO);
		pagamentoRepo.save(pagamento);
		return new PagamentoDto(pagamento);
	}

}
