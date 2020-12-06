package uk.co.betbull.playermarket.service;

import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Contract;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.model.Team;

import java.util.List;

public interface ContractService {
    Contract findById(Long id) throws EntityNotFound;

    void delete(Long id) throws EntityNotFound;

    Contract save(Player player, Team team);

    List<Contract> findContractsByPlayerId(Long id);

    List<Team> findTeamsByPlayerId(Long id);

    double calculateContractFee(Player player, Team team);
}
