package com.example.project007;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrailsDataBaseUnitTest {
    //using this junit test for testing all define function in Trails Database controller class

   /* db = FirebaseFirestore.getInstance();
    TrailsDatabaseController.setTrail_db(db);
    final CollectionReference collectionReference = db.collection("Trails");

            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("ShowToast")
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    trails_DataList.clear();
                    int trailId = 0;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Trail_title")));
                        String trail_title = (String) doc.getData().get("Trail_title");
                        String date = (String) doc.getData().get("Date");
                        String type = (String) doc.getData().get("Type");
                        String time = (String) doc.getData().get("Time");
                        //uncertain value
                        String success = (String) doc.getData().get("Success");
                        String failure = (String) doc.getData().get("Failure");
                        String variesData = (String) doc.getData().get("VariesData");
                        String longitude = (String) doc.getData().get("longitude");
                        String latitude = (String) doc.getData().get("latitude");
                        String ignore = (String)doc.getData().get("IgnoreCondition");
                        String UserId = (String)doc.getData().get("UserId");
                        Location location;

                        boolean ignoreCondition = Boolean.valueOf(ignore);

                        if (longitude != null & latitude != null) {
                            location = new Location(Double.parseDouble(longitude), Double.parseDouble(latitude));//error prone
                        } else {
                            location = null;
                        }

                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);
                        if (ID > trailId) {
                            trailId = ID;
                        }
                        if (experiment.getTrailsId().contains(idString)) {
                            trails_DataList.add(new Trails(trail_title, date, type, time, variesData, ID, location, ignoreCondition, UserId));
                        }
                    }

                    TrailsDatabaseController.setMaxTrailId(trailId);
                }
            }
        });*/
    private Trails mockTrails(){
        //mock object for Non-Geo Location Success/Failure trail
        Trails trails = new Trails("Binomial", "2021-4-4", "Binomial", "23:42", "12", "33", 8, true, "1230c8eff18ea881");
        return trails;
    }

    @Test
    void testModifyTrail(){final FirebaseFirestore db;
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
