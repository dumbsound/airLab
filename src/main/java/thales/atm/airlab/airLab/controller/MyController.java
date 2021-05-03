package thales.atm.airlab.airLab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thales.atm.airlab.airLab.model.*;
import thales.atm.airlab.airLab.service.APIHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MyController {

    APIHandler apiHandler = new APIHandler();

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/airportList")
    public List<AirportList> getAirportList() {

        List<AirportList> airportList = new ArrayList<>();

        try {
            airportList = apiHandler.getAirportList();

            System.out.println("Airport List is \n"+ objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(airportList));

        } catch (Exception e) {
            System.out.println("Error is " + e.getMessage());
        }

        return airportList;

    }

    @GetMapping("/sids")
    public Map<String,List<SID>> getSIDResponse() throws Exception{

        Map<String,List<SID>> allSidResponse=new HashMap<>();

        List<AirportList> airportList = apiHandler.getAirportList();

        for(AirportList listOfAirport: airportList){

            String icao=listOfAirport.getIcao();

            try {
                List<SID> sidList= apiHandler.getSIDList(icao);

                allSidResponse.put(icao,sidList);

                System.out.println("Response is " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allSidResponse));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error is " + e.getMessage());
            }
        }

        return allSidResponse;

    }

    @GetMapping("/sidwaypoints")
    public Map<String, List<WaypointNameCount>> getSIDForEachAirport() throws Exception {

        List<AirportList> airportListResponse = apiHandler.getAirportList();

        Map<String, List<WaypointNameCount>> topWayPointMap = new HashMap<>();

        for(AirportList airportList: airportListResponse){

            List<SID> sidList= apiHandler.getSIDList(airportList.getIcao());

            Map<String, Set<String>> waypointMap = new HashMap<>();

            for(SID sid: sidList){

                List<Waypoint> listofWayPoints = sid.getWaypoints();

                for(Waypoint waypoint : listofWayPoints){

                    String name = waypoint.getName();

                    Set<String> nameList;
                    if(waypointMap.containsKey(name)) {
                        nameList = waypointMap.get(name);
                    }
                    else {
                        nameList = new HashSet<>();
                    }

                    nameList.add(sid.getName());
                    waypointMap.put(name, nameList);

                }
            }

            String top1 = null;
            int max1 = 0;
            for(String key : waypointMap.keySet()) {
                if(waypointMap.get(key).size() > max1) {
                    max1 = waypointMap.get(key).size();
                    top1 = key;
                }
            }

            waypointMap.remove(top1);

            String top2 = null;
            int max2 = 0;
            for(String key : waypointMap.keySet()) {
                if(waypointMap.get(key).size() > max2) {
                    max2 = waypointMap.get(key).size();
                    top2 = key;
                }
            }

            List<WaypointNameCount> waypointNameCounts = new ArrayList<>();

            if(top1 != null) {
                waypointNameCounts.add(new WaypointNameCount(top1, max1));
            }
            if(top2 != null) {
                waypointNameCounts.add(new WaypointNameCount(top2, max2));
            }
            topWayPointMap.put(airportList.getIcao(), waypointNameCounts);

        }

        System.out.println("Response is " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topWayPointMap));

        return topWayPointMap;
    }

    @GetMapping("/stars")
    public Map<String,List<SID>> getStarByAirportResponse() throws Exception{

        Map<String,List<SID>> allStarResponse=new HashMap<>();

        List<AirportList> airportList = apiHandler.getAirportList();

        for(AirportList listOfAirport: airportList){

            String icao=listOfAirport.getIcao();

            try {
                List<SID> sidList= apiHandler.getStarList(icao);

                allStarResponse.put(icao, sidList);

                System.out.println("Response is " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allStarResponse));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error is " + e.getMessage());
            }
        }

        return allStarResponse;
    }

    @GetMapping("/starwaypoints")
    public TopWaypoints getStarForEachAirport() throws Exception {

        List<AirportList> airportListResponse = apiHandler.getAirportList();

        List<String> starWaypoints=new ArrayList<>();

        for(AirportList listOfAirport: airportListResponse){

            List<SID> sidList= apiHandler.getStarList(listOfAirport.getIcao());

            for(SID sid: sidList){

                List<Waypoint> waypoints= sid.getWaypoints();

                for(Waypoint ListofWaypoints: waypoints){

                    String waypts=ListofWaypoints.getName();
                    starWaypoints.add(waypts);

                }
            }
        }

        Map<String, Long> groupByMap = starWaypoints.stream().collect(Collectors.groupingBy(e -> e.toString(),Collectors.counting()));

        Map<String, Long> sortedMap = new LinkedHashMap<>();

        groupByMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()).forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

        String firstKey = sortedMap.keySet().stream().findFirst().get();
        Long firstValue=sortedMap.get(firstKey);

        TopWaypoints topWaypoints=new TopWaypoints();
        topWaypoints.setIcao("WSSS");

        WaypointNameCount waypointNameCount1=new WaypointNameCount();
        waypointNameCount1.setName(firstKey);
        waypointNameCount1.setCount(Math.toIntExact(firstValue));

        WaypointNameCount waypointNameCount2=new WaypointNameCount();
        waypointNameCount2.setName((String) sortedMap.keySet().toArray()[1]);
        waypointNameCount2.setCount(Math.toIntExact((Long) sortedMap.values().toArray()[1]));

        List<WaypointNameCount> waypointNameCountsList=new ArrayList<>();
        waypointNameCountsList.add(waypointNameCount1);
        waypointNameCountsList.add(waypointNameCount2);

        topWaypoints.setTopWaypoints(waypointNameCountsList);

        System.out.println("Response is " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topWaypoints));

       return topWaypoints;

    }
}

