package uk.co.betbull.playermarket.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Team;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {

    private final Team testTeam = new Team();

    @Autowired
    private TeamService teamService;

    @Before
    public void setUp() {
        testTeam.setId(1L);
        testTeam.setName("testTeam");
        testTeam.setCurrencyCode("USD");
        testTeam.setCommissionPercent(10);
    }

    @Test
    public void shouldReturnSavedTeam() {
        testTeam.setId(null);
        Team savedTeam = teamService.save(testTeam);
        assertNotNull(savedTeam);
        assertEquals(testTeam.getName(), savedTeam.getName());
        assertEquals(testTeam.getCurrencyCode(), savedTeam.getCurrencyCode());
        assertEquals(testTeam.getCommissionPercent(), savedTeam.getCommissionPercent());
        assertNotNull(savedTeam.getId());
    }

    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenGettingNonExistingId() {
        teamService.findById(0L);
    }

    @Test
    public void shouldReturnSingleTeamWhenIdFound() {
        Team savedTeam = teamService.save(testTeam);
        Team foundTeam = teamService.findById(savedTeam.getId());
        assertNotNull(foundTeam);
        assertEquals(savedTeam.getId(), foundTeam.getId());
    }

    @Test
    public void shouldReturnMultipleTeam() {
        teamService.save(testTeam);

        Team chelsea = new Team();
        chelsea.setId(2L);
        chelsea.setName("Chelsea");
        chelsea.setCurrencyCode("USD");
        chelsea.setCommissionPercent(7);
        teamService.save(chelsea);

        Team fulham = new Team();
        fulham.setId(3L);
        fulham.setName("Fulham");
        fulham.setCurrencyCode("USD");
        fulham.setCommissionPercent(10);
        teamService.save(fulham);

        List<Long> teamsIds = teamService.findAllTeams().stream().map(Team::getId).collect(Collectors.toList());
        assertTrue(teamsIds.containsAll(Arrays.asList(testTeam.getId(), chelsea.getId(), fulham.getId())));
    }

    @Test
    public void shouldUpdateTeam() {
        teamService.save(testTeam);
        testTeam.setName("Updated testTeam");
        teamService.update(1L, testTeam);
        Team updatedTeam = teamService.findById(1L);
        assertEquals(testTeam.getName(), updatedTeam.getName());
    }


    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenDeletingNonExistingId() {
        teamService.delete(0L);
    }

    @Test
    public void shouldDeleteExistingTeamId() {
        teamService.save(testTeam);
        teamService.delete(testTeam.getId());
    }

}
