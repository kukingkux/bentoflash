package com.tup.bentoflash.core.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BEVERAGE")
public class GrabAndGoBeverage extends CatalogItem {
    private boolean isRefrigerated;

    public GrabAndGoBeverage() {}

    // @Override
    // public String getPackagingType() {
    //     return "Recyclable PET Bottle";
    // }

    public boolean isIsRefrigerated() { return isRefrigerated; }
    public void setIsRefrigerated(boolean isRefrigerated) { this.isRefrigerated = isRefrigerated;}
}
