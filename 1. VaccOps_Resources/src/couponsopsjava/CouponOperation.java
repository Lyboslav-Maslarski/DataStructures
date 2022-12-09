package couponsopsjava;

import java.util.*;


public class CouponOperation implements ICouponOperation {

    public CouponOperation() {
    }

    public void registerSite(Website w) {
    }

    public void addCoupon(Website w, Coupon c) {
    }

    public Website removeWebsite(String domain) {
        return null;
    }

    public Coupon removeCoupon(String code) {
        return null;
    }

    public boolean exist(Website w) {
        return false;
    }

    public boolean exist(Coupon c) {
        return false;
    }

    public Collection<Website> getSites() {
        return null;
    }

    public Collection<Coupon> getCouponsForWebsite(Website w) {
        return null;
    }

    public void useCoupon(Website w, Coupon c) {
    }

    public Collection<Coupon> getCouponsOrderedByValidityDescAndDiscountPercentageDesc() {
        return null;
    }

    public Collection<Website> getWebsitesOrderedByUserCountAndCouponsCountDesc() {
        return null;
    }
}
