package com.example.restpost.dtos.address_commands;


import com.example.restpost.model.address.County;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIrishCommand implements UpdateCommand {

    @NotNull@NotBlank @Schema(example = "The Square")
    private String streetAddress;
    @NotNull@NotBlank @Schema(example = "Sir William Courtenay")
    private String name;
    @NotNull@NotBlank @Schema(example = "Newcastle West")
    private String city;
    @NotNull @Schema(example = "Limerick")
    private County county;
}
