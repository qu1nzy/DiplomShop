package by.tms.Diplom.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPassword {

    @NotBlank
    @NotNull
    @Pattern(regexp = "[A-Za-z0-9]+")
    @Size(min = 5, max = 15)
    private String password;
}
