package uk.co.betbull.playermarket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.repository.PlayerRespository;
import uk.co.betbull.playermarket.service.PlayerService;

import uk.co.betbull.playermarket.exception.EntityNotFound;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRespository playerRespository;

    public PlayerServiceImpl(PlayerRespository playerRespository) {
        this.playerRespository = playerRespository;
    }

    @Override
    public Player save(Player player) {
        player.setId(null);
        return playerRespository.save(player);
    }

    @Override
    public Player findById(Long id) throws EntityNotFound {
        return ifPlayerExists(id);
    }

    @Override
    public List<Player> findAllPlayers() {
        return playerRespository.findAll();
    }

    @Override
    public void update(Long id, Player player) throws EntityNotFound {
        Player existingPlayer = ifPlayerExists(id);

        existingPlayer.setFirstName(player.getFirstName());
        existingPlayer.setLastName(player.getLastName());
        existingPlayer.setBirthday(player.getBirthday());
        existingPlayer.setExperience(player.getExperience());

        playerRespository.save(existingPlayer);
    }

    @Override
    public void delete(Long id) throws EntityNotFound {
        Player existingPlayer = ifPlayerExists(id);
        playerRespository.deleteById(id);
    }

    private Player ifPlayerExists(Long id) throws EntityNotFound {
        Player existingPlayer = playerRespository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(String.format("No player found with id: %s", id)));
        return existingPlayer;
    }
}
