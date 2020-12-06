package uk.co.betbull.playermarket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Contract {
    @Id
    @SequenceGenerator(name = "CONTRACT_GENERATOR", sequenceName = "CONTRACT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACT_GENERATOR")
    private Long id;

    @Column(name = "CONTRACT_DATE", columnDefinition = "DATE")
    private LocalDate contractDate;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "CONTRACT_PRICE")
    private double contractPrice;

    @Column(name = "ACTIVE")
    private Boolean active;

    @ManyToOne(targetEntity = Player.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @ManyToOne(targetEntity = Team.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(double contractPrice) {
        this.contractPrice = contractPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(contractDate);
        hcb.append(player.getFirstName());
        hcb.append(player.getLastName());
        hcb.append(player.getBirthday());
        hcb.append(team.getName());
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contract)) {
            return false;
        }
        Contract contract = ((Contract) obj);

        EqualsBuilder eb = new EqualsBuilder();
        eb.append(contractDate, contract.getContractDate());
        eb.append(player.getFirstName(), contract.player.getFirstName());
        eb.append(player.getLastName(), contract.player.getLastName());
        eb.append(player.getBirthday(), contract.player.getBirthday());
        eb.append(team.getName(), contract.team.getName());
        return eb.isEquals();
    }
}
