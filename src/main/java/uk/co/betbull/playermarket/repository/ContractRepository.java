package uk.co.betbull.playermarket.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.betbull.playermarket.model.Contract;

import java.util.List;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    List<Contract> findByPlayerId(Long id);
    Contract findByPlayerIdAndActiveTrue(Long id);
}
