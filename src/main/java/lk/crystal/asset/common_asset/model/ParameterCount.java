package lk.crystal.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterCount {
    private String name;
    private Integer count;
    private BigDecimal itemPrice;
    private long itemSellCount;
}
