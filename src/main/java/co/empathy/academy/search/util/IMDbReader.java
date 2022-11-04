package co.empathy.academy.search.util;

import co.empathy.academy.search.models.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IMDbReader {

    private final BufferedReader imdbReader;
    private final int documentsSize = 20000;

    private boolean hasDocuments = true;


    public IMDbReader(MultipartFile imdbFile) {
        try {
            this.imdbReader = new BufferedReader(new InputStreamReader(imdbFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readHeaders();
    }

    private void readHeaders() {
        try {
            imdbReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> readDocuments() {
        List<Movie> result = new ArrayList<>();
        int currentLinesRead = 0;

        while (currentLinesRead < documentsSize) {
            String line = null;
            try {
                line = imdbReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (line == null) {
                this.hasDocuments = false;
                return result;
            }

            String[] fields = line.split("\t");
            Movie movie = new Movie(fields[0], fields[1], fields[2], fields[3], fields[4].contentEquals("1"),
                    StringIntegerConversion.toInt(fields[5]), StringIntegerConversion.toInt(fields[6]),
                    StringIntegerConversion.toInt(fields[7]), fields[8]);

            result.add(movie);

            currentLinesRead++;
        }

        return result;
    }

    public boolean hasDocuments() {
        return hasDocuments;
    }
}
