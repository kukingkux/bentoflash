package com.tup.bentoflash.calculator;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tup.bentoflash.core.model.Ingredient;
import com.tup.bentoflash.core.model.LocalCultureBento;

@Service
public class NutritionalCalculator {

    public double calculateTotalCalories(LocalCultureBento bento) {
        double totalCalories = 0.0;

        List<Ingredient> ingredients = bento.getIngredients();

        for (Ingredient ingredient : ingredients) {
            totalCalories += ingredient.getCalories(); // calories bertipe int, auto-widening ke double
        }

        return totalCalories;
    }

    public String calculateMacros(LocalCultureBento bento) {
        double totalProtein = 0.0;
        double totalCarbs   = 0.0;
        double totalFat     = 0.0;

        List<Ingredient> ingredients = bento.getIngredients();

        for (Ingredient ingredient : ingredients) {
            totalProtein += ingredient.getProtein();
            totalCarbs   += ingredient.getCarbs();
            totalFat     += ingredient.getFat();
        }

        return String.format(
            "Protein: %.2fg | Carbs: %.2fg | Fat: %.2fg",
            totalProtein,
            totalCarbs,
            totalFat
        );
    }
}