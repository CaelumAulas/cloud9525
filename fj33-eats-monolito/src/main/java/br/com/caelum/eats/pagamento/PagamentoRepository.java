package br.com.caelum.eats.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.eats.pagamento.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
