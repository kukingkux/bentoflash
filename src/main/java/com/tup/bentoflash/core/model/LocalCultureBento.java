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
    @ManyToMany
    @JoinTable(
        name = "bento_ingredients",
        joinColumns = @JoinColumn(name = "bento_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @Override
    public void applyEndOfDayDiscount() {
        // Elfan: Modul 2 Logic
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    // Getter Setter
}
