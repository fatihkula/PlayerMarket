package uk.co.betbull.playermarket.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.betbull.playermarket.model.Player;

import java.util.List;

@Repository
public interface PlayerRespository extends CrudRepository<Player, Long> {
    List<Player> findAll();
}
