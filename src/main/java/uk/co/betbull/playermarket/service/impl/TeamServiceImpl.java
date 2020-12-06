package uk.co.betbull.playermarket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.repository.TeamRepository;
import uk.co.betbull.playermarket.service.TeamService;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team save(Team team) {
        team.setId(null);
        return teamRepository.save(team);
    }

    @Override
    public Team findById(Long id) throws EntityNotFound {
        return ifTeamExists(id);
    }

    @Override
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public void update(Long id, Team team) throws EntityNotFound {
        Team existingTeam = ifTeamExists(id);

        existingTeam.setName(team.getName());
        existingTeam.setCurrencyCode(team.getCurrencyCode());
        existingTeam.setCommissionPercent(team.getCommissionPercent());

        teamRepository.save(existingTeam);
    }

    @Override
    public void delete(Long id) throws EntityNotFound {
        Team existingTeam = ifTeamExists(id);
        teamRepository.deleteById(id);
    }

    private Team ifTeamExists(Long id) throws EntityNotFound {
        Team existingTeam = teamRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(String.format("No team found with id: %s", id)));
        return existingTeam;
    }

}
