package uk.co.betbull.playermarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.service.TeamService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "team/v1")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<Team> findAllTeams() {
        return teamService.findAllTeams();
    }

    @GetMapping(path = "{id}")
    public Team findTeamById(@PathVariable("id") Long id) {
        return teamService.findById(id);
    }

    @PostMapping
    public Team save( @Valid @RequestBody Team team) {
        return teamService.save(team);
    }

    @PutMapping(path = "{id}")
    public void update(@PathVariable("id") Long id, @RequestBody Team team) {
        teamService.update(id, team);
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable("id") Long id) {
        teamService.delete(id);
    }
}

