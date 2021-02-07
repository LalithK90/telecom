package lk.crystal.asset.item.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MainCategory {
    MOBILE_PHONE("Mobile Phone"),
    ACCESSORIES("Accessories");



    /*CANNED_FOODS("Canned Foods"),
    CONFECTIONERY_ITEMS("Confectionery Items"),
    RICE_PULSES("Rice Pulses"),
    CONDIMENTS_SPICES("Condiments Spices"),
    MEAT_FISH_EGGS("Meat, Fish & Eggs"),
    OIL_FATS("Oil Fats"),
    VEGETABLES_FRUITS("Vegetables & Fruits"),
    MILK_POWDER("Milk Powder"),
    DAIRY_PRODUCTS("Dairy Products"),
    BABY_PRODUCTS("Baby Products"),
    PERSONAL_CARE("Personal Care"),
    HOUSEHOLD("Household"),
    BREAKFAST_CERALS("Breakfast Cerals"),
    GOURMET_INGREDIENTS("Gourmet Ingredients"),
    DESSERT_INGREDIENTS("Dessert Ingredients"),
    SPECIAL_SEA_FOODS("Special Sea Foods"),
    PARTY_FAVORS("Party Favors"),
    ESSENTIAL_GROCERY_PACKS("Essential Grocery Packs");*/


    private final String mainCategory;
}
