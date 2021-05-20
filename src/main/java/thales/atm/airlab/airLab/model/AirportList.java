package thales.atm.airlab.airLab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AirportList {
    private String uid;
    private String name;
    private String icao;
    private Double lat;
    private Double lng;
    private Integer alt;
    private String iata;
}
