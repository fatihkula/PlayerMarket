package uk.co.betbull.playermarket.service;

import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Player;

import java.util.List;

public interface PlayerService {
    Player save(Player player);

    Player findById(Long id) throws EntityNotFound;

    List<Player> findAllPlayers();

    void update(Long id, Player player) throws EntityNotFound;

    void delete(Long id) throws EntityNotFound;
}
