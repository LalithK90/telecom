package lk.crystal.asset.report.model;


import lk.crystal.asset.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSellPriceQuantityBuyingPrice {
private Item item;
private BigDecimal sellPrice;
private BigDecimal sellTotalPrice;
private long itemCounter;
private BigDecimal buyingPrice;
private BigDecimal buyingTotalPrice;


}
