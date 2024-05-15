package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {

    private UUID id;

    private String username;

    private Date spendDate;

    private String currency;

    private Double amount;

    private String description;

    private String category;

}
