package uk.co.betbull.playermarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.co.betbull.playermarket.dto.ContractDTO;
import uk.co.betbull.playermarket.model.Contract;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.service.ContractService;
import uk.co.betbull.playermarket.service.PlayerService;
import uk.co.betbull.playermarket.service.TeamService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "player/v1")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<Player> findAllPlayers() {
        return playerService.findAllPlayers();
    }

    @GetMapping(path = "{id}")
    public Player findPlayerById(@PathVariable Long id) {
        return playerService.findById(id);
    }

    @PostMapping
    public Player save(@Valid @RequestBody Player player) {
        return playerService.save(player);
    }

    @PutMapping(path = "{id}")
    public void update(@PathVariable Long id, @RequestBody Player player) {
        playerService.update(id, player);
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable Long id) {
        playerService.delete(id);
    }

    @GetMapping("{id}/team")
    public List<Team> findTeamsByPlayerId(@PathVariable Long id) {
        return contractService.findTeamsByPlayerId(id);
    }

    @PostMapping("{id}/contract")
    public Contract saveContract(@PathVariable Long id, @Valid @RequestBody ContractDTO contractDTO) {
        Player player = playerService.findById(id);
        Team team = teamService.findById(contractDTO.getTeamId());

        return contractService.save(player, team);
    }
}
