package uk.co.betbull.playermarket.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Player;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerServiceTest {
    private final Player testPlayer = new Player();

    @Autowired
    private PlayerService playerService;

    @Before
    public void setUp(){
        testPlayer.setId(1L);
        testPlayer.setFirstName("Marcus");
        testPlayer.setLastName("Rashford");
        testPlayer.setBirthday(LocalDate.of(1997, 10, 31));
        testPlayer.setExperience(5);
    }

    @Test
    public void shouldReturnSavedPlayer () {
        testPlayer.setId(null);
        Player savedPlayer = playerService.save(testPlayer);
        assertNotNull(savedPlayer);
        assertEquals(testPlayer.getFirstName(), savedPlayer.getFirstName());
        assertEquals(testPlayer.getLastName(), savedPlayer.getLastName());
        assertEquals(testPlayer.getBirthday(), savedPlayer.getBirthday());
        assertEquals(testPlayer.getExperience(), savedPlayer.getExperience());
        assertNotNull(savedPlayer.getId());
    }

    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenGettingNonExistingId() {
        playerService.findById(0L);
    }

    @Test
    public void shouldReturnSinglePlayerWhenIdFound() {
        Player savedPlayer = playerService.save(testPlayer);
        Player foundPlayer = playerService.findById(savedPlayer.getId());
        assertNotNull(foundPlayer);
        assertEquals(savedPlayer.getId(), foundPlayer.getId());
    }

    @Test
    public void shouldReturnMultiplePlayer() {
        playerService.save(testPlayer);

        Player paulPogba = new Player();
        paulPogba.setId(2L);
        paulPogba.setFirstName("Paul");
        paulPogba.setLastName("Pogba");
        paulPogba.setBirthday(LocalDate.of(1993, 3, 15));
        paulPogba.setExperience(2);
        playerService.save(paulPogba);

        Player davidDeGea = new Player();
        davidDeGea.setId(3L);
        davidDeGea.setFirstName("David");
        davidDeGea.setLastName("De Gea");
        davidDeGea.setBirthday(LocalDate.of(1990, 7, 11));
        davidDeGea.setExperience(12);
        playerService.save(davidDeGea);

        List<Long> playersIds = playerService.findAllPlayers().stream().map(Player::getId).collect(Collectors.toList());
        assertTrue(playersIds.containsAll(Arrays.asList(testPlayer.getId(), paulPogba.getId(), davidDeGea.getId())));
    }

    @Test
    public void shouldUpdatePlayer() {
        playerService.save(testPlayer);
        testPlayer.setFirstName("Updated FirstName");
        playerService.update(1L, testPlayer);
        Player updatedPlayer = playerService.findById(1L);
        assertEquals(testPlayer.getFirstName(), updatedPlayer.getFirstName());
    }

    @Test(expected = EntityNotFound.class)
    public void shouldThrowsExceptionWhenDeletingNonExistingId() {
        playerService.delete(0L);
    }

    @Test
    public void shouldDeleteExistingPlayerId() {
        playerService.save(testPlayer);
        playerService.delete(testPlayer.getId());
    }


}
