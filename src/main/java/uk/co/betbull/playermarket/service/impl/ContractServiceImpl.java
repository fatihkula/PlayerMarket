package uk.co.betbull.playermarket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.betbull.playermarket.exception.EntityNotFound;
import uk.co.betbull.playermarket.model.Contract;
import uk.co.betbull.playermarket.model.Player;
import uk.co.betbull.playermarket.model.Team;
import uk.co.betbull.playermarket.repository.ContractRepository;
import uk.co.betbull.playermarket.service.ContractService;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Contract findById(Long id) {
        return ifContractExists(id);
    }

    @Override
    public void delete(Long id) throws EntityNotFound {
        Contract existingContract = ifContractExists(id);
        contractRepository.deleteById(id);
    }

    @Override
    public Contract save(Player player, Team team) {
        Contract existingContract = contractRepository.findByPlayerIdAndActiveTrue(player.getId());

        if (existingContract != null) {
            existingContract.setActive(false);
            contractRepository.save(existingContract);
        }

        Contract newContract = new Contract();
        newContract.setPlayer(player);
        newContract.setTeam(team);
        newContract.setCurrencyCode(team.getCurrencyCode());
        newContract.setContractDate(LocalDate.now());
        newContract.setContractPrice(calculateContractFee(player, team));
        newContract.setActive(true);
        contractRepository.save(newContract);

        return newContract;
    }

    @Override
    public List<Contract> findContractsByPlayerId(Long playerId) throws EntityNotFound {
        return contractRepository.findByPlayerId(playerId);
    }

    @Override
    public List<Team> findTeamsByPlayerId(Long playerId) throws EntityNotFound {
        return contractRepository.findByPlayerId(playerId).stream().map(Contract::getTeam).collect(Collectors.toList());
    }

    public double calculateContractFee(Player player, Team team) {
        double transferFee = 1.0 * player.getExperience() * 100000 / calculateAge(player.getBirthday());
        double teamCommission = 1.0 * transferFee * team.getCommissionPercent() / 100;

        return transferFee + teamCommission;
    }

    private int calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    private Contract ifContractExists(Long id) throws EntityNotFound {
        Contract existingContract = contractRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(String.format("No contract found with id: %s", id)));
        return existingContract;
    }
}
