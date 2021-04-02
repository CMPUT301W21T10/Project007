package com.example.project007;

import org.junit.Test;

import java.util.ArrayList;

class ExperimentTest {

    private ExperimentAdapter mockExperimentList() {
        ExperimentAdapter experimentList = new ExperimentAdapter(getContext(), new ArrayList<Experiment>());
        experimentList.add(mockExperiment());
        return experimentList;
    }

    private Experiment mockExperiment() {
        return new Experiment();
    }

    @Test
    void testAdd() {
        CityList cityList = mockCityList();

        assertEquals(1, cityList.getCities().size());

        City city = new City("Regina", "Saskatchewan");
        cityList.add(city);

        assertEquals(2, cityList.getCities().size());
        assertTrue(cityList.getCities().contains(city));
    }

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

    }
}