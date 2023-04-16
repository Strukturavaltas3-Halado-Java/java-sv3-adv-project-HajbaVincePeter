package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import com.example.restpost.validator.ValidatePostalAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidatePostalAddress
public class UpdatePostalCommand implements UpdateCommand {

    @NotNull @Schema(example = "HU")
    private Country country;
    @NotNull@NotBlank @Schema(example = "Sas utca 1.")
    private String streetAddress;
    @NotNull@NotBlank @Schema(example = "Guszev Alekszej")
    private String name;
    @NotNull@NotBlank @Schema(example = "Budapest")
    private String city;
    @NotNull@NotBlank @Schema(example = "1051")
    private String postalCode;

}
