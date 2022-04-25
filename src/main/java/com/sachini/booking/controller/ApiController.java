package com.sachini.booking.controller;

import com.sachini.booking.dao.WebScraped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/api")
public class ApiController {
	
	private static final String POST_URL = "http://localhost:8080/api/read_csv_file";
    private static final String CSV_FILE_PATH = "D:/Project/Eclipse/Backend/src/main/resources/assets/user.csv";
    
    @RequestMapping(value = "/admin/get_csv_file", method = RequestMethod.GET)
    public static List<String> getCsv() throws IOException {

        URL apiURl = new URL(POST_URL);

        HttpURLConnection linec = (HttpURLConnection) apiURl.openConnection();
        linec.setDoInput(true);
        linec.setDoOutput(true);
        linec.setRequestMethod("GET");

        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(linec.getInputStream()));

        String fileName = "";
        String inputLine;
        while ((inputLine = bufferReader.readLine()) != null) {
            fileName = inputLine;
        }
        bufferReader.close();
        fileName = fileName.replace("[", "");
        fileName = fileName.replace("]", "");
        fileName = fileName.replace("\"", "");

        PrintWriter writer = new PrintWriter(
                new File(CSV_FILE_PATH));

        List<String> list = new ArrayList<>(Arrays.asList(fileName.split(",")));
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append('\n');
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            sb.append('\n');
        }

        writer.write(sb.toString());
        writer.flush();
        writer.close();

        return list;

    }

@RequestMapping(value = "/web_scrape",method = RequestMethod.GET)
public List<WebScraped> webScraping(){

	List<WebScraped> webScrapedVehicleList = new ArrayList<>();

	final String url = "https://www.malkey.lk/rates/self-drive-rates.html";

	try {
		final Document document = Jsoup.connect(url).get();

		for(Element row: document.select("table.table.selfdriverates tr")) {

			WebScraped vehicle = new WebScraped();

			final String vehicleName= row.select("td.text-left.percent-40").text();
			if(!vehicleName.contentEquals("")) {
				vehicle.setVehicleName(vehicleName);
			}
			final String rates = row.select("td.text-center.percent-22").text();
			if(!rates.contentEquals("")) {

				String[] priceList = rates.split(" ");

				vehicle.setMonthlyRate(priceList[0]);
				vehicle.setWeeklyRate(priceList[1]);
				vehicle.setMillagePerDay(priceList[2]);

			}

			if(vehicle.getVehicleName()!=null) {
				webScrapedVehicleList.add(vehicle);
			}

		}

	}catch(Exception e) {
		e.getMessage();
	}

	return webScrapedVehicleList;
}
}
