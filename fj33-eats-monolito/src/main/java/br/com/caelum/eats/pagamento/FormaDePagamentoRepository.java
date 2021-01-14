package br.com.caelum.eats.pagamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.eats.pagamento.FormaDePagamento;

public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamento, Long> {

	List<FormaDePagamento> findAllByOrderByNomeAsc();

}
