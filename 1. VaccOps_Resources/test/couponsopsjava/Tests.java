package couponsopsjava;

import couponsopsjava.Coupon;
import couponsopsjava.CouponOperation;
import couponsopsjava.ICouponOperation;
import couponsopsjava.Website;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Tests {

    private ICouponOperation couponOperations;
    private Website w1 = new Website("a", 1);
    private Website w2 = new Website("b", 2);
    private Website w3 = new Website("c", 2);
    private Website w4 = new Website("d", 4);
    private Website w5 = new Website("e", 5);
    private Website w6 = new Website("f", 6);
    private Coupon c1 = new Coupon("a", 1, 3);
    private Coupon c2 = new Coupon("b", 1, 2);
    private Coupon c3 = new Coupon("c", 1, 1);
    private Coupon c4 = new Coupon("d", 2, 3);
    private Coupon c5 = new Coupon("e", 0, 3);

    @Before
    public void Setup()
    {
        this.couponOperations = new CouponOperation();
    }

    // 1
    @Test
    public void TestRegisterWebsite()
    {
        this.couponOperations.registerSite(w1);

        assertTrue(this.couponOperations.exist(w1));
    }

    // 2
    @Test(expected = IllegalArgumentException.class)
    public void TestRegisteringWebsiteTwiceThrowException()
    {
        this.couponOperations.registerSite(w1);
        this.couponOperations.registerSite(w1);
    }

    // 3
    @Test
    public void TestRegisteringManyWebsites()
    {
        this.couponOperations.registerSite(w1);
        this.couponOperations.registerSite(w2);
        this.couponOperations.registerSite(w3);

        assertTrue(this.couponOperations.getSites().size() == 3);
    }

    // 4
    @Test
    public void TestAddingCoupon()
    {
        this.couponOperations.registerSite(w1);
        this.couponOperations.addCoupon(w1, c1);

        assertTrue(this.couponOperations.exist(c1));
    }

    // 5
    @Test(expected = IllegalArgumentException.class)
    public void TestAddingCouponTwice()
    {
        this.couponOperations.registerSite(w1);
        this.couponOperations.addCoupon(w1, c1);
        this.couponOperations.addCoupon(w1, c1);
    }

    // 7
    @Test(expected = IllegalArgumentException.class)
    public void TestAddingCouponForNonExistentSite()
    {
        this.couponOperations.registerSite(w2);
        this.couponOperations.addCoupon(w1, c1);
    }

    // Performance
        @Test
    public void RegisterSitePerf()
    {
        for (int i = 0; i < 10000; i++)
        {
            this.couponOperations.registerSite(new Website(i + "", i));
        }

        long start = System.currentTimeMillis();
        this.couponOperations.registerSite(new Website("test", 1));
        long stop = System.currentTimeMillis();
        assertTrue(stop - start <=20);
    }

}

