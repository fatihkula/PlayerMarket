package uk.co.betbull.playermarket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "player")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Player {

    @Id
    @SequenceGenerator(name = "PLAYER_GENERATOR", sequenceName = "PLAYER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLAYER_GENERATOR")
    private Long id;

    @Column(name = "FIRST_NAME")
    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @Column(name = "BIRTHDAY", columnDefinition = "date")
    @NotNull(message = "Birthday is mandatory")
    private LocalDate birthday;

    @Column(name = "EXPERIENCE", nullable = false)
    @NotNull(message = "Experience is mandatory")
    private Integer experience;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
