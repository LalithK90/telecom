package lk.crystal.asset.item.entity.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MainCategory {
    PT("Processed Meat"),
    BR("Beverages"),
    CF("Canned Foods"),
    CI("Confectionery Items"),
    RP("Rice Pulses"),
    CS("Condiments Spices"),
    MF("Meat, Fish & Eggs"),
    OF("Oil Fats"),
    VF("Vegetables & Fruits"),
    MP("Milk Powder"),
    DP("Dairy Products"),
    BP("Baby Products"),
    PC("Personal Care"),
    HH("Household"),
    BC("Breakfast Cerals"),
    GI("Gourmet Ingredients"),
    DI("Dessert Ingredients"),
    SF("Special Sea Foods"),
    PF("Party Favors"),
    EP("Essential Grocery Packs");

    private final String mainCategory;
}