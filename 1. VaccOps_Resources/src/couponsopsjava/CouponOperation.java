package couponsopsjava;

import java.util.*;
import java.util.stream.Collectors;


public class CouponOperation implements ICouponOperation {
    private Map<String, Website> websiteMap;
    private Map<String, Coupon> couponMap;
    private Map<String, List<Coupon>> websitesWithCouponsMap;
    private Map<String, Website> couponWithWebsite;

    public CouponOperation() {
        websiteMap = new HashMap<>();
        couponMap = new HashMap<>();
        websitesWithCouponsMap = new HashMap<>();
        couponWithWebsite = new HashMap<>();
    }

    public void registerSite(Website w) {
        if (exist(w)) {
            throw new IllegalArgumentException();
        }
        websiteMap.put(w.domain, w);
        websitesWithCouponsMap.put(w.domain, new ArrayList<>());
    }

    public void addCoupon(Website w, Coupon c) {
        if (!exist(w) || exist(c)) {
            throw new IllegalArgumentException();
        }
        couponMap.put(c.code, c);
        couponWithWebsite.put(c.code, w);
        websitesWithCouponsMap.get(w.domain).add(c);
    }

    public Website removeWebsite(String domain) {
        if (!websiteMap.containsKey(domain)) {
            throw new IllegalArgumentException();
        }
        websitesWithCouponsMap.remove(domain);
        return websiteMap.remove(domain);
    }

    public Coupon removeCoupon(String code) {
        if (!couponMap.containsKey(code)) {
            throw new IllegalArgumentException();
        }
        Website website = couponWithWebsite.get(code);
        websitesWithCouponsMap.get(website.domain).removeIf(c -> c.code.equals(code));
        couponWithWebsite.remove(code);
        return couponMap.remove(code);
    }

    public boolean exist(Website w) {
        return websiteMap.containsKey(w.domain);
    }

    public boolean exist(Coupon c) {
        return couponMap.containsKey(c.code);
    }

    public Collection<Website> getSites() {
        return new ArrayList<>(websiteMap.values());
    }

    public Collection<Coupon> getCouponsForWebsite(Website w) {
        if (!exist(w)) {
            throw new IllegalArgumentException();
        }
        return websitesWithCouponsMap.get(w.domain);
    }

    public void useCoupon(Website w, Coupon c) {
        if (!exist(w) || !exist(c)) {
            throw new IllegalArgumentException();
        }
        if (!websitesWithCouponsMap.get(w.domain).contains(c)) {
            throw new IllegalArgumentException();
        }
        websitesWithCouponsMap.get(w.domain).remove(c);
        couponMap.remove(c.code);
        couponWithWebsite.remove(c.code);
    }

    public Collection<Coupon> getCouponsOrderedByValidityDescAndDiscountPercentageDesc() {
        return couponMap.values()
                .stream()
                .sorted((f, s) -> {
                    int result = s.validity - f.validity;
                    if (result == 0) {
                        result = s.discountPercentage - f.discountPercentage;
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    public Collection<Website> getWebsitesOrderedByUserCountAndCouponsCountDesc() {
        return websiteMap.values()
                .stream()
                .sorted((f, s) -> {
                    int result = f.usersCount - s.usersCount;
                    if (result == 0) {
                        result = websitesWithCouponsMap.get(s.domain).size() - websitesWithCouponsMap.get(f.domain).size();
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }
}
