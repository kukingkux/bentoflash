package com.tup.bentoflash.pricing;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tup.bentoflash.core.model.CatalogItem;
import com.tup.bentoflash.core.model.LocalCultureBento;
import com.tup.bentoflash.core.repository.IPerishable;

@Service
public class FlashPricingEngine {

    private static final Logger logger = Logger.getLogger(FlashPricingEngine.class.getName());

    private volatile int discountStartHour = 14;

    public void setDiscountStartHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Jam tidak valid: " + hour + ". Gunakan format 0-23."
            );
        }
        this.discountStartHour = hour;
        logger.info("FlashPricingEngine: jam start diskon diubah ke " + hour + ":00 oleh admin.");
    }

    public int getDiscountStartHour() {
        return discountStartHour;
    }

    @Scheduled(cron = "0 * * * * *")
    public void jadwalFlashPricingOtomatis() {
        LocalTime now = LocalTime.now();
        if (now.getHour() == discountStartHour && now.getMinute() == 0) {
            logger.info("FlashPricingEngine: jadwal otomatis aktif jam "
                    + discountStartHour + ":00 → memulai flash pricing.");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void jadwalResetHarga() {
        logger.info("FlashPricingEngine: reset harga tengah malam.");
    }

    public List<CatalogItem> applyFlashDiscounts(List<CatalogItem> catalogItems) {
        List<CatalogItem> hasilDiskon = new ArrayList<>();

        if (!isFlashPricingActive()) {
            logger.info("FlashPricingEngine: belum jam " + discountStartHour + ":00, harga tetap normal.");
            return hasilDiskon;
        }

        if (catalogItems == null || catalogItems.isEmpty()) {
            logger.warning("FlashPricingEngine: catalog kosong atau null.");
            return hasilDiskon;
        }

        for (CatalogItem item : catalogItems) {
            if (item instanceof IPerishable perishableItem) {
                if (perishableItem.isExpired()) {
                    logger.warning("FlashPricingEngine: item expired dilewati → " + item);
                    continue;
                }
                perishableItem.applyEndOfDayDiscount();
                hasilDiskon.add(item);
                logger.info("FlashPricingEngine: diskon diterapkan → " + item);
            }
        }

        logger.info(String.format(
                "FlashPricingEngine: %d dari %d item berhasil didiskon.",
                hasilDiskon.size(), catalogItems.size()
        ));

        return hasilDiskon;
    }

    public void resetAllDailyPrices(List<CatalogItem> catalogItems) {
        if (catalogItems == null) return;
        for (CatalogItem item : catalogItems) {
            if (item instanceof LocalCultureBento bento) {
                bento.resetDailyPrice();
                logger.info("FlashPricingEngine: harga di-reset → " + bento);
            }
        }
        logger.info("FlashPricingEngine: semua harga sudah di-reset ke normal.");
    }

    public boolean isFlashPricingActive() {
        return !LocalTime.now().isBefore(LocalTime.of(discountStartHour, 0));
    }
}