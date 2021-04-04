package com.example.project007;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrailsDataBaseUnitTest {
    //using this junit test for testing all define function in Trails Database controller class

    private Trails mockTrails(){
        //mock object for Non-Geo Location Success/Failure trail
        Trails trails = new Trails("Binomial", "2021-4-4", "Binomial", "23:42", "12", "33", 8, true, "1230c8eff18ea881");
        return trails;
    }

    @Test
    void testModifyTrail(){final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        TrailsDatabaseController.setTrail_db(db);
        final CollectionReference collectionReference = db.collection("Trails");

        boolean testResult;
        testResult = TrailsDatabaseController.modify_Trails("Trails", mockTrails());
        assertFalse(testResult, "Trails add on database success!");
    }

    @Test
    void testDeleteTrail(){
        boolean testResult;
        testResult = TrailsDatabaseController.delete_Trails("Trails", mockTrails());
        assertFalse(testResult, "Trails add on database success!");
    }

}
