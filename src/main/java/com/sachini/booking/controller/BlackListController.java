package com.sachini.booking.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class BlackListController {
	
	private static final String CSVFILE = "D:/Project/Eclipse/Backend/src/main/resources/assets/user.csv";

    @RequestMapping(value = "/read_csv_file")
    public List<String> readData() throws IOException {

        List<String> list = new ArrayList<String>();
        String line = "";
        BufferedReader bufferReader = new BufferedReader(
                new FileReader(CSVFILE));
        

        while ((line = bufferReader.readLine()) != null) // returns a Boolean value
        {
            list.add(line);
        }
        bufferReader.close();

        return list;
    }

}
