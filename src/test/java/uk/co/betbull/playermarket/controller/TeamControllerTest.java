package uk.co.betbull.playermarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.service.TeamService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamServiceMock;

    @Autowired
    ObjectMapper objectMapper;

    private final Team testTeam = new Team();

    @Before
    public void setUp() {
        Mockito.reset(teamServiceMock);

        testTeam.setId(1L);
        testTeam.setName("testTeam");
        testTeam.setCurrencyCode("USD");
        testTeam.setCommissionPercent(10);
    }

    @Test
    public void shouldReturn404StatusWithFindByIdNotFound() throws Exception {
        when(teamServiceMock.findById(2L)).thenThrow(new EntityNotFound(""));

        mockMvc.perform(get("/team/v1/{id}", 2L))
                .andExpect(status().isNotFound());
        verify(teamServiceMock, times(1)).findById(2L);
        verifyNoMoreInteractions(teamServiceMock);
    }

    @Test
    public void shouldReturnSingleTeam() throws Exception {
        when(teamServiceMock.findById(1L)).thenReturn(testTeam);

        this.mockMvc.perform(get("/team/v1/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value("testTeam"))
                .andExpect(jsonPath("currencyCode").value("USD"))
                .andExpect(jsonPath("commissionPercent").value(10));

        verify(teamServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(teamServiceMock);

    }

    @Test
    public void shouldReturnMultipleTeam() throws Exception {
        Team chelsea = new Team();
        chelsea.setId(2L);
        chelsea.setName("Chelsea");
        chelsea.setCurrencyCode("USD");
        chelsea.setCommissionPercent(7);

        Team fulham = new Team();
        fulham.setId(3L);
        fulham.setName("Fulham");
        fulham.setCurrencyCode("USD");
        fulham.setCommissionPercent(10);

        List<Team> teams = new ArrayList<Team>() {
            {
                add(testTeam);
                add(chelsea);
                add(fulham);
            }
        };

        when(teamServiceMock.findAllTeams()).thenReturn(teams);
        this.mockMvc.perform(get("/team/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(is(in(teams.stream().map(Team::getName).collect(Collectors.toList())))))
                .andExpect(jsonPath("$[1].name").value(is(in(teams.stream().map(Team::getName).collect(Collectors.toList())))))
                .andExpect(jsonPath("$[2].name").value(is(in(teams.stream().map(Team::getName).collect(Collectors.toList())))));

    }

    @Test
    public void ShouldReturnSavedTeam() throws Exception {
        when(teamServiceMock.save(testTeam)).thenReturn(testTeam);
        this.mockMvc
                .perform(post("/team/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTeam)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void shouldReturnOKWhenUpdateTeam() throws Exception {
        testTeam.setName("Updated testTeam");
        this.mockMvc
                .perform(put("/team/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTeam)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOKWhenDeleteTeam() throws Exception {
        this.mockMvc
                .perform(delete("/team/v1/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
