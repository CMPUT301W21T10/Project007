package com.example.project007;
import android.annotation.SuppressLint;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResultUnitTest {
    private double[] mockValueList() {
        double[] valList = new double[4];
        valList[0] = 1;
        valList[1] = 2;
        valList[2] = 3;
        valList[3] = 4;
        return valList;
    }


    private ArrayList<Double> mockValueArrayList() {
        ArrayList<Double> doubleArray = new ArrayList<Double>();
        for (int i = 1; i < 5; i++) {
            double value = (double) i;
            doubleArray.add(value);
        }
        return doubleArray;
    }

    /**
     * This function is meant to testing Quartiles in result
     */
    @Test
    void testQuartiles() {
        double[] valList = mockValueList();
        assertEquals(1, valList[0]);
        double[] answer = ResultFragment.Quartiles(valList);

        assertEquals(1.50, answer[0]);
        assertEquals(2.50, answer[1]);
        assertEquals(3.50, answer[2]);

    }

    /**
     * This function is meant to testing Average in result
     */
    @Test
    void testAvg() {
        double[] valList = mockValueList();
        double answer = ResultFragment.avg(valList);
        assertEquals(2.50, answer);
    }
}
