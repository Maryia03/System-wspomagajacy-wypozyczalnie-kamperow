package pl.pollub.camp.Models.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.pollub.camp.Models.InspectionType;

import java.sql.Date;

@Getter
@Setter
public class InspectionRequest {
    @NotBlank
    private String name;
    @NotNull
    private InspectionType inspectionType;
    @NotNull
    @Future
    private Date validUntil;
    @NotNull
    private Date startDate;
    @NotNull
    private Integer vehicleId;
}

