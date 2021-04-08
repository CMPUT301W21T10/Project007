package com.example.project007;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class DatabaseControllerTest {


    private Experiment mockExperiment() {
        return new Experiment("name","description","2021-04-08","Count-based",DatabaseController.generateExperimentId(), new ArrayList<>(),
                new ArrayList<>(),false,true,2,"China",false, new ArrayList<>()) ;
    }

    @Test
    void testAdd() {
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DatabaseController.setDb(db);
        DatabaseController.setUserId("testUserId");
        DatabaseController.modify_experiment("Experiments",mockExperiment());

    }
/*
    @Test
    void testAddException() {
        CityList cityList = mockCityList();

        City city = new City("Yellowknife", "Northwest Territories");
        cityList.add(city);

        assertThrows(IllegalArgumentException.class, () -> {
            cityList.add(city);
        });
    }

    @Test
    void testGetCities() {
        CityList cityList = mockCityList();

        assertEquals(0, mockCity().compareTo(cityList.getCities().get(0)));

        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);

        assertEquals(0, city.compareTo(cityList.getCities().get(0)));
        assertEquals(0, mockCity().compareTo(cityList.getCities().get(1)));
    }

    @Test
    void testHasCity(){
        CityList cityList = mockCityList();

        City cityA = new City("Yellowknife", "Northwest Territories");
        cityList.add(cityA);

        assertTrue(cityList.hasCity(cityA));

        City cityB = new City("Toronto", "ABC");

        assertFalse(cityList.hasCity(cityB));

    }

    @Test
    void testDeleteCity(){
        CityList cityList = mockCityList();

        City cityA = new City("Yellowknife", "Northwest Territories");
        cityList.add(cityA);

        cityList.deleteCity(cityA);

        assertFalse(cityList.hasCity(cityA));

    }

    @Test
    void testDeleteCityException(){
        CityList cityList = mockCityList();

        City cityA = new City("Yellowknife", "Northwest Territories");
        cityList.add(cityA);

        cityList.deleteCity(cityA);

        assertThrows(IllegalArgumentException.class, () -> {
            cityList.deleteCity(cityA);
        });
    }

    @Test
    void testCountCites(){
        CityList cityList = mockCityList();
        assertEquals(1, cityList.countCities());

        City cityA = new City("Yellowknife", "Northwest Territories");
        cityList.add(cityA);
        City cityB = new City("Toronto", "ABC");
        cityList.add(cityB);

        assertEquals(3, cityList.countCities());

    }*/
}