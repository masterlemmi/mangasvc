package com.lemoncode.spring;


import com.lemoncode.manga.MangaRepository;
import com.lemoncode.util.UrlParser;
import com.lemoncode.util.ObjectGenerator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


@Component
public class AppInitializer implements ApplicationRunner {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AppInitializer.class);


    @Autowired
    private MangaRepository repo;

    @Autowired
    private UrlParser parser;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("The application is starting...");
       // loadInitialMangaList(); //to populate table
    }


    private void loadInitialMangaList() {
        System.out.println("Loading Initial Manga List");


        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream in = classLoader.getResourceAsStream("mangalist.csv");
            Reader reader = new InputStreamReader(in);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String chapterUrl = record.get(2);
                repo.save(ObjectGenerator.generate(chapterUrl));
            }

            System.out.println("Done Loading Manga List");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
