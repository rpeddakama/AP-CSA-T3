package com.example.sping_portfolio.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.*;


@Controller  
public class Map {
    FileInputStream serviceAccount = new FileInputStream("serviceAccount.json");
    FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://nolan-4b453.firebaseio.com/")
            .build();

            ArrayList<String> result = new ArrayList<String>();
            String elementResult = "";


    public Map() throws IOException {
        FirebaseApp.initializeApp(options);
    }

    @GetMapping("/map")    
    public String getMap() {
        return "map"; 
}



    // Fetches GeoJSON data from Firebase, returns in a String. Adds to an ArrayList locally.
    @GetMapping("/fetchFromFirebase")
    @ResponseBody
    public String fetchFromFirebase(@RequestParam(name = "index", required = false, defaultValue = "0") int index) throws IOException, InterruptedException {
        elementResult = "";
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("map/" + index);

        System.out.println("ref: " + "map/" + index);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 result.add(dataSnapshot.getValue(String.class));
                 elementResult += (dataSnapshot.getValue(String.class));
                System.out.println("Value: " + elementResult);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
        //Thread.sleep(1500);
        return elementResult;
    }

}