package uk.co.betbull.playermarket.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Contract;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.repository.ContractRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractServiceTest {

    private final Contract testContract = new Contract();
    private final Player testPlayer = new Player();
    private final Team testTeam = new Team();
    private static final double DELTA = 1e-15;

    @Autowired
    private ContractService contractService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Before
    public void setUp() {
        testTeam.setId(1L);
        testTeam.setName("testTeam");
        testTeam.setCurrencyCode("USD");
        testTeam.setCommissionPercent(10);

        testPlayer.setId(1L);
        testPlayer.setFirstName("Marcus");
        testPlayer.setLastName("Rashford");
        testPlayer.setBirthday(LocalDate.of(1997, 10, 31));
        testPlayer.setExperience(5);

        testContract.setId(1L);
        testContract.setContractDate(LocalDate.now());
        testContract.setCurrencyCode("USD");
        testContract.setContractPrice(contractService.calculateContractFee(testPlayer, testTeam));
        testContract.setActive(true);
        testContract.setPlayer(testPlayer);
        testContract.setTeam(testTeam);
    }

    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenGettingNonExistingId() {
        contractService.findById(0L);
    }

    @Test
    public void shouldReturnSingleContractWhenIdFound() {
        playerService.save(testPlayer);
        teamService.save(testTeam);
        Contract savedContract = contractService.save(testPlayer, testTeam);
        Contract foundContract = contractService.findById(savedContract.getId());
        assertNotNull(foundContract);
        assertEquals(savedContract.getId(), foundContract.getId());
    }

    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenDeletingNonExistingId() {
        contractService.delete(0L);
    }

    @Test
    public void shouldDeleteExistingContractId() {
        playerService.save(testPlayer);
        teamService.save(testTeam);
        contractService.save(testPlayer, testTeam);
        contractService.delete(testContract.getId());
    }

    @Test
    public void shouldReturnSavedContract() {
        playerService.save(testPlayer);
        teamService.save(testTeam);

        Contract savedContract = contractService.save(testPlayer, testTeam);
        assertNotNull(savedContract);
        assertEquals(testContract.getActive(), savedContract.getActive());
        assertEquals(testContract.getContractDate(), savedContract.getContractDate());
        assertEquals(testContract.getCurrencyCode(), savedContract.getCurrencyCode());
        assertEquals(testContract.getContractPrice(), savedContract.getContractPrice(), DELTA);
        assertNotNull(savedContract.getId());
    }

    @Test
    public void ShouldReturnContractsByPlayerId() {
        playerService.save(testPlayer);
        teamService.save(testTeam);
        Contract currentContract = contractService.save(testPlayer, testTeam);

        final Team fulham = new Team();
        fulham.setId(2L);
        fulham.setName("Fulham");
        fulham.setCurrencyCode("GBP");
        fulham.setCommissionPercent(10);
        teamService.save(testTeam);

        Contract newContract = contractService.save(testPlayer, fulham);

        List<Long> contractsIds = contractService.findContractsByPlayerId(testPlayer.getId()).stream().map(Contract::getId).collect(Collectors.toList());
        assertTrue(contractsIds.containsAll(Arrays.asList(currentContract.getId(), newContract.getId())));
    }

    @Test
    public void shouldReturnTeamsByPlayerId() {
        Player savedPlayer = playerService.save(testPlayer);
        teamService.save(testTeam);
        final Contract currentContract = contractService.save(savedPlayer, testTeam);

        final Team fulham = new Team();
        fulham.setId(2L);
        fulham.setName("Fulham");
        fulham.setCurrencyCode("GBP");
        fulham.setCommissionPercent(10);
        teamService.save(fulham);
        final Contract newContract = contractService.save(savedPlayer, fulham);

        List<Long> teamIds = contractService.findTeamsByPlayerId(savedPlayer.getId()).stream().map(Team::getId).collect(Collectors.toList());
        assertTrue(teamIds.containsAll(Arrays.asList(currentContract.getTeam().getId(), newContract.getTeam().getId())));
    }

    @Test
    public void shouldReturnContractFee() {
        Double calculatedFee = contractService.calculateContractFee(testPlayer, testTeam);

        double transferFee = 1.0 * testPlayer.getExperience() * 100000 / (Period.between(testPlayer.getBirthday(), LocalDate.now()).getYears());
        double teamCommission = 1.0 * testTeam.getCommissionPercent() / 100 * transferFee;
        double contractFee = transferFee + teamCommission;
        assertEquals(0, calculatedFee.compareTo(contractFee));
    }

}
