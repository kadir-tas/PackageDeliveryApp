package com.huawei.probation.packagedeliveryapp.dummy;

import com.huawei.probation.packagedeliveryapp.data.Pair;
import com.huawei.probation.packagedeliveryapp.data.ProductData;

import java.util.ArrayList;
import java.util.List;

public class DummyContent {

    public static final List<Pair<ProductData, Integer>> BREAD_TYPES = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> WORKSHOP = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> PATISSERIE = new ArrayList<Pair<ProductData, Integer>>();


    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            BREAD_TYPES.add(createDummyItem(i,"Bread Types"));
        }
        for (int i = COUNT; i < COUNT * 2; i++) {
            WORKSHOP.add(createDummyItem(i,"Workshop"));
        }
        for (int i = COUNT * 2; i < COUNT * 3; i++) {
            PATISSERIE.add(createDummyItem(i,"Patisserie"));
        }
    }

    private static Pair<ProductData, Integer> createDummyItem(int position, String category) {
        Pair<ProductData, Integer> pair = new Pair<>();
        pair.first = new ProductData(String.valueOf(position), category + " " + position , Float.parseFloat ( Integer.toString(position) + ".57"),category);
        pair.second = 0;
        return pair;
    }
}
