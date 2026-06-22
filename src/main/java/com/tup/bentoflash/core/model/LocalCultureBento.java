package com.tup.bentoflash.core.model;

import java.util.List;

import com.tup.bentoflash.core.repository.IPerishable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("BENTO")
public class LocalCultureBento extends CatalogItem implements IPerishable {
    private int calorieCount;
    private boolean discountApplied;

    @ManyToMany
    @JoinTable(
            name = "bento_ingredients",
            joinColumns = @JoinColumn(name = "bento_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    public LocalCultureBento() {}

    @Override
    public String getPackagingType() {
        return "Eco-friendly Cardboard Box";
    }

    @Override
    public void applyEndOfDayDiscount() {
        if (discountApplied) {
            return;
        }
        double potongan = 0.40 * getBasePrice();
        setCurrentPrice(getBasePrice() - potongan);
        discountApplied = true;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    public void resetDailyPrice() {
        setCurrentPrice(getBasePrice());
        this.discountApplied = false;
    }

    public boolean isDiscountApplied() { return discountApplied; }
    public int getCalorieCount() { return calorieCount; }
    public void setCalorieCount(int calorieCount) { this.calorieCount = calorieCount; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}