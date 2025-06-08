package com.finki.explorandija.util;

import com.finki.explorandija.model.Challenge;
import com.finki.explorandija.model.Destination;
import com.finki.explorandija.model.enums.ChallengeType;
import com.finki.explorandija.repository.ChallengeRepository;
import com.finki.explorandija.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final DestinationRepository destinationRepository;
    private final ChallengeRepository challengeRepository;

    @Autowired
    public DataInitializer(DestinationRepository destinationRepository, ChallengeRepository challengeRepository) {
        this.destinationRepository = destinationRepository;
        this.challengeRepository = challengeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking if data initialization is needed...");

        if (destinationRepository.count() == 0) {
            logger.info("No destinations found. Initializing sample data...");

            // Destination 1: Skopje
            Destination skopje = new Destination();
            skopje.setName("Skopje");
            skopje.setDescription("The capital and largest city of North Macedonia, located in the heart of the Balkan Peninsula.");
            skopje.setImageUrl("https://i.imgur.com/gGhAd6T.jpeg");
            skopje.setLatitude(41.9981);
            skopje.setLongitude(21.4254);
            destinationRepository.save(skopje);
            logger.info("Saved destination: Skopje");

            Challenge skopjeChallenge1 = new Challenge("Stone Bridge Photo", "Take a picture of the Stone Bridge.", ChallengeType.PHOTO, skopje);
            Challenge skopjeChallenge2 = new Challenge("Old Bazaar Visit", "Explore the Old Bazaar and find a traditional craft.", ChallengeType.DISCOVERY, skopje);
            Challenge skopjeChallenge3 = new Challenge("Kale Fortress View", "Climb to Kale Fortress and enjoy the city view.", ChallengeType.PHOTO, skopje);
            challengeRepository.saveAll(Arrays.asList(skopjeChallenge1, skopjeChallenge2, skopjeChallenge3));
            logger.info("Saved challenges for Skopje");

            // Destination 2: Ohrid
            Destination ohrid = new Destination();
            ohrid.setName("Ohrid");
            ohrid.setDescription("A city on the shores of Lake Ohrid, known for its ancient churches, medieval castle, and stunning natural beauty.");
            ohrid.setImageUrl("https://i.imgur.com/W0IWbmX.jpeg");
            ohrid.setLatitude(41.1231);
            ohrid.setLongitude(20.8016);
            destinationRepository.save(ohrid);
            logger.info("Saved destination: Ohrid");
            
            Challenge ohridChallenge1 = new Challenge("Lake Ohrid Swim", "Take a dip in the pristine waters of Lake Ohrid.", ChallengeType.ACTION, ohrid);
            Challenge ohridChallenge2 = new Challenge("Samuel's Fortress", "Visit Samuel's Fortress and learn about its history.", ChallengeType.DISCOVERY, ohrid);
            Challenge ohridChallenge3 = new Challenge("St. John at Kaneo", "Photograph the iconic Church of St. John at Kaneo.", ChallengeType.PHOTO, ohrid);
            challengeRepository.saveAll(Arrays.asList(ohridChallenge1, ohridChallenge2, ohridChallenge3));
            logger.info("Saved challenges for Ohrid");

            // Destination 3: Matka Canyon
            Destination matka = new Destination();
            matka.setName("Matka Canyon");
            matka.setDescription("A beautiful canyon west of Skopje, home to several medieval monasteries and endemic species.");
            matka.setImageUrl("https://i.imgur.com/0pSzsiu.jpeg");
            matka.setLatitude(41.9519);
            matka.setLongitude(21.2969);
            destinationRepository.save(matka);
            logger.info("Saved destination: Matka Canyon");

            Challenge matkaChallenge1 = new Challenge("Cave Vrelo Boat Trip", "Take a boat trip to Cave Vrelo, one of the deepest underwater caves.", ChallengeType.ACTION, matka);
            Challenge matkaChallenge2 = new Challenge("Monastery of St. Andrew", "Visit the Monastery of St. Andrew within the canyon.", ChallengeType.DISCOVERY, matka);
            challengeRepository.saveAll(Arrays.asList(matkaChallenge1, matkaChallenge2));
            logger.info("Saved challenges for Matka Canyon");
            
            // Destination 4: Bitola
            Destination bitola = new Destination();
            bitola.setName("Bitola");
            bitola.setDescription("The second-largest city, known for its Ottoman-era architecture and vibrant Shirok Sokak street.");
            bitola.setImageUrl("https://i.imgur.com/stZ19ee.jpeg");
            bitola.setLatitude(41.0319);
            bitola.setLongitude(21.3347);
            destinationRepository.save(bitola);
            logger.info("Saved destination: Bitola");

            Challenge bitolaChallenge1 = new Challenge("Shirok Sokak Stroll", "Take a walk down Shirok Sokak and enjoy the atmosphere.", ChallengeType.DISCOVERY, bitola);
            Challenge bitolaChallenge2 = new Challenge("Heraclea Lyncestis", "Explore the ancient ruins of Heraclea Lyncestis.", ChallengeType.HISTORICAL, bitola);
            challengeRepository.saveAll(Arrays.asList(bitolaChallenge1, bitolaChallenge2));
            logger.info("Saved challenges for Bitola");

            logger.info("Finished initializing sample data.");
        } else {
            logger.info("Data already exists. Skipping initialization.");
        }
    }
} 