package thales.atm.airlab.airLab.service;

import thales.atm.airlab.airLab.model.Account;
import thales.atm.airlab.airLab.model.AirportList;
import thales.atm.airlab.airLab.model.SID;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIHandler {

    private RestTemplate restTemplate = new RestTemplate();

    public List<AirportList> getAirportList() throws Exception {

        String url = Account.URL + "airports";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("api-key", Account.apikey);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try{
            ResponseEntity<AirportList[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AirportList[].class);
            AirportList[] response=responseEntity.getBody();

            return Arrays.asList(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<SID> getSIDList(String icao) throws Exception {

        System.out.println("icao: "+icao);
        String url = Account.URL+"sids/airport/"+icao;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("api-key", Account.apikey);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try{
            ResponseEntity<SID[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, SID[].class);
            SID[] response=responseEntity.getBody();

            return Arrays.asList(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    public List<SID> getSIDListIcao(String icao) throws Exception {

        System.out.println("icao: "+icao);
        String url = Account.URL+"sids/airport/"+icao;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("api-key", Account.apikey);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try{
            ResponseEntity<SID[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, SID[].class);
            SID[] response=responseEntity.getBody();

            return Arrays.asList(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<SID> getStarList(String icao){

        System.out.println("icao: "+icao);
        String url=Account.URL+"stars/airport/"+icao;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("api-key", Account.apikey);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try{

            ResponseEntity<SID[]> responseEntity=restTemplate.exchange(url,HttpMethod.GET, httpEntity, SID[].class);
            SID[] response = responseEntity.getBody();

            return Arrays.asList(response);
        } catch(Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
