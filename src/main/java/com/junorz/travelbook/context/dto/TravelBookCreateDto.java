package com.junorz.travelbook.context.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Dto for Travel book's creation.<br>
 * The data will be set by UI form's value.
 */
@Data
public class TravelBookCreateDto {
    
    @NotNull(message = "{TRAVELBOOK_NAME_CANNOT_BE_NULL}")
    private String name;
    
    @NotNull(message = "{TRAVELBOOK_PWD_CANNOT_BE_NULL}")
    private String adminPassword;
    
    @NotNull(message = "{TRAVELBOOK_PWD_CANNOT_BE_NULL}")
    private String confirmPassword;
    
    @NotNull(message = "{TRAVELBOOK_CURRENCY_CANNOT_BE_NULL}")
    private String currency;
    
}
