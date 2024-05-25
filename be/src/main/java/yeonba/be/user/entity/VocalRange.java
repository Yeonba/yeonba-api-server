package yeonba.be.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Table(name = "vocals_ranges")
@Getter
@Entity
@NoArgsConstructor
public class VocalRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String classification;

    public VocalRange(String classification) {
        this.classification = classification;
    }

    public boolean hasSameClassificationAs(String classification) {

        return StringUtils.equals(this.classification, classification);
    }
}
