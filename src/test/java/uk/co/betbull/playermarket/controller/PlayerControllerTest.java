package uk.co.betbull.playermarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.betbull.playermarket.dto.ContractDTO;
import uk.co.betbull.playermarket.model.Contract;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.service.ContractService;
import uk.co.betbull.playermarket.service.PlayerService;
import uk.co.betbull.playermarket.service.TeamService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private ContractService contractService;

    @MockBean
    private TeamService teamService;

    @Autowired
    ObjectMapper objectMapper;

    private final Player testPlayer = new Player();

    @Before
    public void setUp() {
        testPlayer.setId(1L);
        testPlayer.setFirstName("Marcus");
        testPlayer.setLastName("Rashford");
        testPlayer.setBirthday(LocalDate.of(1997, 10, 31));
        testPlayer.setExperience(5);
    }

    @Test
    public void shouldReturnSinglePlayer() throws Exception {
        when(playerService.findById(1L)).thenReturn(testPlayer);
        this.mockMvc.perform(get("/player/v1/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Marcus"))
                .andExpect(jsonPath("lastName").value("Rashford"))
                .andExpect(jsonPath("birthday").value(LocalDate.of(1997, 10, 31).toString()))
                .andExpect(jsonPath("experience").value(5));

    }

    @Test
    public void shouldReturnMultiplePlayer() throws Exception {
        Player paulPogba = new Player();
        paulPogba.setId(2L);
        paulPogba.setFirstName("Paul");
        paulPogba.setLastName("Pogba");
        paulPogba.setBirthday(LocalDate.of(1993, 3, 15));
        paulPogba.setExperience(2);

        Player davidDeGea = new Player();
        davidDeGea.setId(3L);
        davidDeGea.setFirstName("David");
        davidDeGea.setLastName("De Gea");
        davidDeGea.setBirthday(LocalDate.of(1990, 7, 11));
        davidDeGea.setExperience(12);

        List<Player> players = new ArrayList<Player>() {
            {
                add(testPlayer);
                add(paulPogba);
                add(davidDeGea);
            }
        };

        when(playerService.findAllPlayers()).thenReturn(players);
        this.mockMvc.perform(get("/player/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value(is(in(players.stream().map(Player::getFirstName).collect(Collectors.toList())))))
                .andExpect(jsonPath("$[1].firstName").value(is(in(players.stream().map(Player::getFirstName).collect(Collectors.toList())))))
                .andExpect(jsonPath("$[2].firstName").value(is(in(players.stream().map(Player::getFirstName).collect(Collectors.toList())))));

    }

    @Test
    public void ShouldReturnSavedPlayer() throws Exception {
        when(playerService.save(testPlayer)).thenReturn(testPlayer);
        this.mockMvc
                .perform(post("/player/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOKWhenUpdatePlayer() throws Exception {
        testPlayer.setFirstName("Updated FirstName");
        this.mockMvc
                .perform(put("/player/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOKWhenDeletePlayer() throws Exception {
        this.mockMvc
                .perform(delete("/player/v1/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnContract() throws Exception {
        when(playerService.findById(1L)).thenReturn(testPlayer);

        final Team testTeam = new Team();
        testTeam.setId(1L);
        testTeam.setName("testTeam");
        testTeam.setCurrencyCode("USD");
        testTeam.setCommissionPercent(10);
        when(teamService.findById(1L)).thenReturn(testTeam);

        final Contract testContract = new Contract();
        testContract.setId(1L);
        testContract.setContractDate(LocalDate.now());
        testContract.setCurrencyCode("USD");
        testContract.setContractPrice(contractService.calculateContractFee(testPlayer, testTeam));
        testContract.setActive(true);
        testContract.setPlayer(testPlayer);
        testContract.setTeam(testTeam);
        when(contractService.save(testPlayer, testTeam)).thenReturn(testContract);

        ContractDTO testContractDTO = new ContractDTO();
        testContractDTO.setPlayerId(testPlayer.getId());
        testContractDTO.setTeamId(testTeam.getId());

        this.mockMvc
                .perform(post("/player/v1/1/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testContractDTO))
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnTeamsByPlayerId() throws Exception {
        when(playerService.findById(1L)).thenReturn(testPlayer);

        final Team testTeam = new Team();
        testTeam.setId(1L);
        testTeam.setName("testTeam");
        testTeam.setCurrencyCode("USD");
        testTeam.setCommissionPercent(10);
        when(teamService.findById(1L)).thenReturn(testTeam);

        final Contract testContract = new Contract();
        testContract.setId(1L);
        testContract.setContractDate(LocalDate.now());
        testContract.setCurrencyCode("USD");
        testContract.setContractPrice(contractService.calculateContractFee(testPlayer, testTeam));
        testContract.setActive(true);
        testContract.setPlayer(testPlayer);
        testContract.setTeam(testTeam);
        when(contractService.save(testPlayer, testTeam)).thenReturn(testContract);

        final Team fulham = new Team();
        fulham.setId(2L);
        fulham.setName("Fulham");
        fulham.setCurrencyCode("GBP");
        fulham.setCommissionPercent(10);
        when(teamService.findById(2L)).thenReturn(fulham);

        final Contract fulhamContract = new Contract();
        fulhamContract.setId(2L);
        fulhamContract.setContractDate(LocalDate.now());
        fulhamContract.setCurrencyCode("USD");
        fulhamContract.setContractPrice(contractService.calculateContractFee(testPlayer, fulham));
        fulhamContract.setActive(true);
        fulhamContract.setPlayer(testPlayer);
        fulhamContract.setTeam(fulham);
        when(contractService.save(testPlayer, fulham)).thenReturn(fulhamContract);

        List<Contract> contracts = new ArrayList<Contract>() {
            {
                add(testContract);
                add(fulhamContract);
            }
        };

        when(contractService.findContractsByPlayerId(testPlayer.getId())).thenReturn(contracts);

        when(contractService.findTeamsByPlayerId(testPlayer.getId())).thenReturn(contracts.stream().map(Contract::getTeam).collect(Collectors.toList()));

        this.mockMvc.perform(get("/player/v1/1/team"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
