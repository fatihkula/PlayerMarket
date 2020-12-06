package uk.co.betbull.playermarket.service;

import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Team;

import java.util.List;

public interface TeamService {
    Team save(Team team);

    Team findById(Long id) throws EntityNotFound;

    List<Team> findAllTeams();

    void update(Long id, Team team) throws EntityNotFound;

    void delete(Long id) throws EntityNotFound;
}
