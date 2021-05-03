package thales.atm.airlab.airLab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Waypoint {

    private String uid;
    private String name;
    private double lat;
    private double lng;


}
