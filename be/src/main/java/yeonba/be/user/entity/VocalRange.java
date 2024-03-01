package yeonba.be.user.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "vocals_ranges")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VocalRange {

    @Id
    @GeneratedValue
    private Long id;
    private String classification;
}
