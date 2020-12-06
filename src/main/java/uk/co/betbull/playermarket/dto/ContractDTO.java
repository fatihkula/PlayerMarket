package uk.co.betbull.playermarket.dto;

import javax.validation.constraints.NotNull;

public class ContractDTO {
    @NotNull
    private Long playerId;

    @NotNull
    private Long teamId;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
