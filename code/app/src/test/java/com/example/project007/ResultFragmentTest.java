//package com.example.project007;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class ResultFragmentTest {
//    private com.example.project007.ResultFragment mockResultFragment(){
//        CityList cityList = new CityList();
//        cityList.add(mockCity());
//        return cityList;
//    }
//    private City mockCity(){
//        return new City("Edmonton","Alberta");
//    }
//
//    @Test
//    void testAdd(){
//        CityList cityList = mockCityList();
//        assertEquals(1,cityList.getCities().size());
//        City city = new City("Regina","Saskatchewan");
//        cityList.add(city);
//        assertEquals(2,cityList.getCities().size());
//        assertTrue(cityList.getCities().contains(city));
//    }
//    @Test
//    void testAddException(){
//        final CityList cityList = mockCityList();
//        final City city = new City("Yellowknife","Northwest Territories");
//        cityList.add(city);
//        assertThrows(IllegalArgumentException.class, () -> {
//            cityList.add(city);
//        });
//    }
//    @Test
//    void testGetCities(){
//        CityList cityList = mockCityList();
//        assertEquals(0,mockCity().compareTo(cityList.getCities().get(0)));
//        City city = new City("Charlottetown","Prince Edward Island");
//        cityList.add(city);
//        assertEquals(0,city.compareTo(cityList.getCities().get(0)));
//        assertEquals(0,mockCity().compareTo(cityList.getCities().get(1)));
//    }
//    @Test
//    void testhasCity(){
//        CityList cityList = mockCityList();
//        City city1 = new City("Shenyang","Liaoning");
//        City city2 = new City("Shanghai","Zhejiang");
//        cityList.add(city1);
//        assertTrue(cityList.hasCity(city1));
//        assertFalse(cityList.hasCity(city2));
//    }
//    @Test
//    void testdeleteCity(){
//        CityList cityList = mockCityList();
//        City city1 = new City("Beijing","BJ");
//        cityList.add(city1);
//        cityList.delete(city1);
//        assertFalse(cityList.hasCity(city1));
//    }
//    @Test
//    void testDeleteException(){
//        final CityList cityList = mockCityList();
//        final City city = new City("Wuxi","Zhejiang");
//        assertThrows(IllegalArgumentException.class,()->{
//            cityList.delete(city);
//        });
//    }
//    @Test
//    void testcountCities(){
//        CityList cityList = mockCityList();
//        assertEquals(1,cityList.getCities().size());
//        City city = new City("Wuxi","Zhejiang");
//        cityList.add(city);
//        assertEquals(2,cityList.getCities().size());
//    }
//}