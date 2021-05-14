package by.tms.Diplom.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWallet {

    @NotBlank
    @NotNull
    @Pattern(regexp = "\\d+([.]\\d+)?")
    @Size(max = 20)
    private String wallet;
}