package com.floodresponse.seeder;

import com.floodresponse.model.*;
import com.floodresponse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private HelplineRepository helplineRepository;
    @Autowired
    private ForecastRepository forecastRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        if (alertRepository.count() == 0) {
            seedAlerts();
            seedShelters();
            seedHelplines();
            seedForecasts();
            seedAdmins();
        }
    }

    private void seedAlerts() {
        Alert alert1 = new Alert();
        alert1.setTitle("High Flood Warning - River Indus");
        alert1.setDescription("Water levels at Guddu Barrage have reached critical levels. Residents in low-lying areas are advised to evacuate immediately.");
        alert1.setSeverity("Critical");
        alert1.setRegion("Sindh");
        alert1.setExpiresAt(LocalDateTime.now().plusDays(2));
        alertRepository.save(alert1);

        Alert alert2 = new Alert();
        alert2.setTitle("Heavy Rainfall Alert");
        alert2.setDescription("Heavy monsoon rains expected in Lahore and surrounding areas for the next 48 hours.");
        alert2.setSeverity("Warning");
        alert2.setRegion("Punjab");
        alert2.setExpiresAt(LocalDateTime.now().plusDays(1));
        alertRepository.save(alert2);
    }

    private void seedShelters() {
        // Existing Lahore shelters
        Shelter shelter1 = new Shelter();
        shelter1.setName("Government High School Model Town");
        shelter1.setAddress("Model Town, Lahore");
        shelter1.setLatitude(31.4805);
        shelter1.setLongitude(74.3239);
        shelter1.setCapacity(500);
        shelter1.setCurrentOccupancy(120);
        shelter1.setContactNumber("0300-1234567");
        shelter1.setFacilities("Food, Water, Medical Aid");
        shelter1.setOpen(true);
        shelterRepository.save(shelter1);

        Shelter shelter2 = new Shelter();
        shelter2.setName("Community Center Gulberg");
        shelter2.setAddress("Gulberg III, Lahore");
        shelter2.setLatitude(31.5204);
        shelter2.setLongitude(74.3587);
        shelter2.setCapacity(300);
        shelter2.setCurrentOccupancy(50);
        shelter2.setContactNumber("0321-7654321");
        shelter2.setFacilities("Food, Water");
        shelter2.setOpen(true);
        shelterRepository.save(shelter2);

        // Islamabad Shelters
        Shelter isl1 = new Shelter();
        isl1.setName("F-6 Markaz Community Center");
        isl1.setAddress("F-6 Markaz, Islamabad");
        isl1.setLatitude(33.7215);
        isl1.setLongitude(73.0433);
        isl1.setCapacity(400);
        isl1.setCurrentOccupancy(0);
        isl1.setContactNumber("051-2345678");
        isl1.setFacilities("Food, Water, Medical Aid, Blankets, Toilets");
        isl1.setOpen(true);
        shelterRepository.save(isl1);

        Shelter isl2 = new Shelter();
        isl2.setName("Pakistan Sports Complex");
        isl2.setAddress("G-9/2, Islamabad");
        isl2.setLatitude(33.6844);
        isl2.setLongitude(73.0479);
        isl2.setCapacity(800);
        isl2.setCurrentOccupancy(0);
        isl2.setContactNumber("051-9262626");
        isl2.setFacilities("Food, Water, Medical Aid, Security, Parking");
        isl2.setOpen(true);
        shelterRepository.save(isl2);

        Shelter isl3 = new Shelter();
        isl3.setName("I-10 Markaz Rescue Center");
        isl3.setAddress("I-10 Markaz, Islamabad");
        isl3.setLatitude(33.6651);
        isl3.setLongitude(73.0169);
        isl3.setCapacity(350);
        isl3.setCurrentOccupancy(0);
        isl3.setContactNumber("051-5730000");
        isl3.setFacilities("Food, Water, Medical Aid, Emergency Services");
        isl3.setOpen(true);
        shelterRepository.save(isl3);

        Shelter isl4 = new Shelter();
        isl4.setName("G-11 Government School");
        isl4.setAddress("G-11/3, Islamabad");
        isl4.setLatitude(33.6751);
        isl4.setLongitude(73.0301);
        isl4.setCapacity(450);
        isl4.setCurrentOccupancy(0);
        isl4.setContactNumber("051-2290123");
        isl4.setFacilities("Food, Water, Toilets, Classrooms");
        isl4.setOpen(true);
        shelterRepository.save(isl4);

        Shelter isl5 = new Shelter();
        isl5.setName("Sector H-9 Community Hall");
        isl5.setAddress("H-9/1, Islamabad");
        isl5.setLatitude(33.6594);
        isl5.setLongitude(73.0294);
        isl5.setCapacity(300);
        isl5.setCurrentOccupancy(0);
        isl5.setContactNumber("051-4443322");
        isl5.setFacilities("Food, Water, Blankets");
        isl5.setOpen(true);
        shelterRepository.save(isl5);

        Shelter isl6 = new Shelter();
        isl6.setName("Blue Area Emergency Center");
        isl6.setAddress("Blue Area, Islamabad");
        isl6.setLatitude(33.7077);
        isl6.setLongitude(73.0578);
        isl6.setCapacity(250);
        isl6.setCurrentOccupancy(0);
        isl6.setContactNumber("051-9203456");
        isl6.setFacilities("Food, Water, Medical Aid, Security");
        isl6.setOpen(true);
        shelterRepository.save(isl6);

        // Rawalpindi Shelters
        Shelter rwp1 = new Shelter();
        rwp1.setName("Saddar Relief Camp");
        rwp1.setAddress("Saddar Bazaar, Rawalpindi");
        rwp1.setLatitude(33.5975);
        rwp1.setLongitude(73.0479);
        rwp1.setCapacity(600);
        rwp1.setCurrentOccupancy(0);
        rwp1.setContactNumber("051-5551234");
        rwp1.setFacilities("Food, Water, Medical Aid, Blankets, Security");
        rwp1.setOpen(true);
        shelterRepository.save(rwp1);

        Shelter rwp2 = new Shelter();
        rwp2.setName("Liaquat Bagh Emergency Shelter");
        rwp2.setAddress("Liaquat Road, Rawalpindi");
        rwp2.setLatitude(33.6007);
        rwp2.setLongitude(73.0679);
        rwp2.setCapacity(500);
        rwp2.setCurrentOccupancy(0);
        rwp2.setContactNumber("051-9270123");
        rwp2.setFacilities("Food, Water, Medical Aid, Toilets, Parking");
        rwp2.setOpen(true);
        shelterRepository.save(rwp2);

        Shelter rwp3 = new Shelter();
        rwp3.setName("Bahria Town Community Center");
        rwp3.setAddress("Bahria Town Phase 4, Rawalpindi");
        rwp3.setLatitude(33.5226);
        rwp3.setLongitude(73.1089);
        rwp3.setCapacity(700);
        rwp3.setCurrentOccupancy(0);
        rwp3.setContactNumber("051-5730456");
        rwp3.setFacilities("Food, Water, Medical Aid, Security, Generators");
        rwp3.setOpen(true);
        shelterRepository.save(rwp3);

        Shelter rwp4 = new Shelter();
        rwp4.setName("Chaklala Cantonment Relief Center");
        rwp4.setAddress("Chaklala Cantonment, Rawalpindi");
        rwp4.setLatitude(33.6161);
        rwp4.setLongitude(73.0997);
        rwp4.setCapacity(550);
        rwp4.setCurrentOccupancy(0);
        rwp4.setContactNumber("051-5092345");
        rwp4.setFacilities("Food, Water, Medical Aid, Security, Military Support");
        rwp4.setOpen(true);
        shelterRepository.save(rwp4);

        Shelter rwp5 = new Shelter();
        rwp5.setName("Satellite Town Community Hall");
        rwp5.setAddress("Satellite Town, Rawalpindi");
        rwp5.setLatitude(33.6294);
        rwp5.setLongitude(73.0686);
        rwp5.setCapacity(400);
        rwp5.setCurrentOccupancy(0);
        rwp5.setContactNumber("051-4581234");
        rwp5.setFacilities("Food, Water, Blankets, Toilets");
        rwp5.setOpen(true);
        shelterRepository.save(rwp5);

        Shelter rwp6 = new Shelter();
        rwp6.setName("Westridge Emergency Shelter");
        rwp6.setAddress("Westridge, Rawalpindi");
        rwp6.setLatitude(33.6528);
        rwp6.setLongitude(73.0686);
        rwp6.setCapacity(350);
        rwp6.setCurrentOccupancy(0);
        rwp6.setContactNumber("051-5123456");
        rwp6.setFacilities("Food, Water, Medical Aid");
        rwp6.setOpen(true);
        shelterRepository.save(rwp6);

        Shelter rwp7 = new Shelter();
        rwp7.setName("DHA Phase 2 Relief Camp");
        rwp7.setAddress("DHA Phase 2, Islamabad");
        rwp7.setLatitude(33.5369);
        rwp7.setLongitude(73.1342);
        rwp7.setCapacity(450);
        rwp7.setCurrentOccupancy(0);
        rwp7.setContactNumber("051-5164567");
        rwp7.setFacilities("Food, Water, Medical Aid, Security, Generators, Parking");
        rwp7.setOpen(true);
        shelterRepository.save(rwp7);

        Shelter rwp8 = new Shelter();
        rwp8.setName("Adiala Road Community Center");
        rwp8.setAddress("Adiala Road, Rawalpindi");
        rwp8.setLatitude(33.5642);
        rwp8.setLongitude(73.1156);
        rwp8.setCapacity(300);
        rwp8.setCurrentOccupancy(0);
        rwp8.setContactNumber("051-4890123");
        rwp8.setFacilities("Food, Water, Blankets");
        rwp8.setOpen(true);
        shelterRepository.save(rwp8);
    }

    private void seedHelplines() {
        Helpline h1 = new Helpline();
        h1.setName("Rescue 1122");
        h1.setPhoneNumber("1122");
        h1.setCategory("Rescue");
        h1.setRegion("Nationwide");
        h1.setDescription("Emergency Ambulance and Rescue Service");
        helplineRepository.save(h1);

        Helpline h2 = new Helpline();
        h2.setName("PDMA Helpline");
        h2.setPhoneNumber("1129");
        h2.setCategory("Government");
        h2.setRegion("Provincial");
        h2.setDescription("Provincial Disaster Management Authority");
        helplineRepository.save(h2);
    }

    private void seedForecasts() {
        Forecast f1 = new Forecast();
        f1.setRegion("River Indus");
        f1.setRiskLevel("High");
        f1.setRainfall(50.5);
        f1.setRiverLevel("High Flood");
        f1.setForecastDate(LocalDateTime.now().plusHours(24));
        f1.setDescription("Water levels rising due to heavy upstream rainfall.");
        forecastRepository.save(f1);

        Forecast f2 = new Forecast();
        f2.setRegion("River Ravi");
        f2.setRiskLevel("Low");
        f2.setRainfall(5.0);
        f2.setRiverLevel("Normal");
        f2.setForecastDate(LocalDateTime.now().plusHours(24));
        f2.setDescription("Normal flow conditions expected.");
        forecastRepository.save(f2);
    }

    private void seedAdmins() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // In production, use BCrypt
        admin.setEmail("admin@floodresponse.org");
        admin.setRole("SUPER_ADMIN");
        adminRepository.save(admin);
    }
}
