package com.huawei.probation.packagedeliveryapp.dummy;

import com.huawei.probation.packagedeliveryapp.data.Pair;
import com.huawei.probation.packagedeliveryapp.data.ProductData;

import java.util.ArrayList;
import java.util.List;

public class DummyContent {


    public static final List<Pair<ProductData, Integer>> MEYVE_SEBZE = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> UNLU_MAMUL = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> ATISTIRMALIK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> DONDURMA = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> YIYECEK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> ICECEK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> FIT_FORM = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> SUT_KAHVALTI = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> TEMEL_GIDA = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> KISISEL_BAKIM = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> EV_TEMIZLIK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> EV_YASAM = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> TEKNOLOJI = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> EVCIL_HAYVAN = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> BEBEK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> CINSEL_SAGLIK = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> GIYIM = new ArrayList<Pair<ProductData, Integer>>();

    public static final List<Pair<ProductData, Integer>> EKMEK_CESITLERI = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> ATOLYE = new ArrayList<Pair<ProductData, Integer>>();
    public static final List<Pair<ProductData, Integer>> PASTANE = new ArrayList<Pair<ProductData, Integer>>();


    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            EKMEK_CESITLERI.add(createDummyItem(i,"Ekmek Çeşitleri"));
        }
        for (int i = COUNT; i < COUNT * 2; i++) {
            ATOLYE.add(createDummyItem(i,"240 Derece Atölye"));
        }
        for (int i = COUNT * 2; i < COUNT * 3; i++) {
            PASTANE.add(createDummyItem(i,"Pastane"));
        }

        for (int i = COUNT * 3; i < COUNT * 4; i++) {
            DONDURMA.add(createDummyItem(i,"Dondurma"));
        }

        for (int i = COUNT * 4; i < COUNT * 5; i++) {
            YIYECEK.add(createDummyItem(i,"Yiyecek"));
        }

        for (int i = COUNT * 5; i < COUNT * 6; i++) {
            ICECEK.add(createDummyItem(i,"İçecek"));
        }

        for (int i = COUNT * 6; i < COUNT * 7; i++) {
            FIT_FORM.add(createDummyItem(i,"Fit & Form"));
        }

        for (int i = COUNT * 7; i < COUNT * 8; i++) {
            SUT_KAHVALTI.add(createDummyItem(i,"Süt & Kahvaltı"));
        }
        for (int i = COUNT * 8; i < COUNT * 9; i++) {
            TEMEL_GIDA.add(createDummyItem(i,"Temel Gıda"));
        }
        for (int i = COUNT * 9; i < COUNT * 10; i++) {
            KISISEL_BAKIM.add(createDummyItem(i,"Kişisel Bakım"));
        }
        for (int i = COUNT * 10; i < COUNT * 11; i++) {
            EV_TEMIZLIK.add(createDummyItem(i,"Ev & Temizlik"));
        }
        for (int i = COUNT * 11; i < COUNT * 12; i++) {
            EV_YASAM.add(createDummyItem(i,"Ev & Yaşam"));
        }
        for (int i = COUNT * 12; i < COUNT * 13; i++) {
            TEKNOLOJI.add(createDummyItem(i,"Teknoloji"));
        }
        for (int i = COUNT * 13; i < COUNT * 14; i++) {
            EVCIL_HAYVAN.add(createDummyItem(i,"Evcil Hayvan"));
        }
        for (int i = COUNT * 14; i < COUNT * 15; i++) {
            BEBEK.add(createDummyItem(i,"Bebek"));
        }
        for (int i = COUNT * 15; i < COUNT * 16; i++) {
            CINSEL_SAGLIK.add(createDummyItem(i,"Cinsel Sağlık"));
        }
        for (int i = COUNT * 16; i < COUNT * 17; i++) {
            GIYIM.add(createDummyItem(i,"Giyim"));
        }
    }

    private static Pair<ProductData, Integer> createDummyItem(int position, String category) {
        Pair<ProductData, Integer> pair = new Pair<>();
        pair.first = new ProductData(String.valueOf(position), category + " " + position , Float.parseFloat ( Integer.toString(position) + ".57"),category);
        pair.second = 0;
        return pair;
    }
}
