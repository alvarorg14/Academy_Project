package co.empathy.academy.search.util;

import co.empathy.academy.search.models.Aka;
import co.empathy.academy.search.models.Director;
import co.empathy.academy.search.models.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IMDbReader {

    private final BufferedReader basicsReader;
    private final BufferedReader ratingsReader;
    private final BufferedReader akasReader;
    private final BufferedReader crewReader;
    private final int documentsSize = 20000;

    private boolean hasDocuments = true;


    public IMDbReader(MultipartFile basicsFile, MultipartFile ratingsFile, MultipartFile akasFile, MultipartFile crewFile) {
        try {
            this.basicsReader = new BufferedReader(new InputStreamReader(basicsFile.getInputStream()));
            this.ratingsReader = new BufferedReader(new InputStreamReader(ratingsFile.getInputStream()));
            this.akasReader = new BufferedReader(new InputStreamReader(akasFile.getInputStream()));
            this.crewReader = new BufferedReader(new InputStreamReader(crewFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readHeaders();
    }

    private void readHeaders() {
        try {
            basicsReader.readLine();
            ratingsReader.readLine();
            akasReader.readLine();
            crewReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> readDocuments() {
        List<Movie> result = new ArrayList<>();
        int currentLinesRead = 0;


        String ratingsLine = null;
        try {
            ratingsLine = ratingsReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (currentLinesRead < documentsSize) {
            try {
                String basicsLine = basicsReader.readLine();

                if (basicsLine == null) {
                    this.hasDocuments = false;
                    return result;
                }

                String[] basics = basicsLine.split("\t");

                String[] ratings = null;
                if (ratingsLine != null) {
                    ratings = ratingsLine.split("\t");
                }
                double averageRating = 0.0;
                int numVotes = 0;


                if (!ratings[0].contentEquals(basics[0])) {
                    //Do nothing
                } else {
                    averageRating = StringDoubleConversion.toDouble(ratings[1]);
                    numVotes = StringIntegerConversion.toInt(ratings[2]);
                    ratingsLine = ratingsReader.readLine();
                }

                basicsReader.mark(1000);
                String nextBasicsId = basicsReader.readLine().split("\t")[0];
                basicsReader.reset();
                List<Aka> akas = readAkas(basics[0], nextBasicsId);

                List<Director> directors = readDirectors();

                Movie movie = new Movie(basics[0], basics[1], basics[2], basics[3], basics[4].contentEquals("1"),
                        StringIntegerConversion.toInt(basics[5]), StringIntegerConversion.toInt(basics[6]),
                        StringIntegerConversion.toInt(basics[7]), StringArrayConversion.toArray(basics[8]),
                        averageRating, numVotes, akas, directors);

                result.add(movie);

                currentLinesRead++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private List<Director> readDirectors() {
        List<Director> directors = new ArrayList<>();
        String crewLine = null;
        try {
            crewLine = crewReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] crew = crewLine.split("\t");
        String[] directorsArray = crew[1].split(",");
        for (String nconst : directorsArray) {
            directors.add(new Director(nconst));
        }
        return directors;
    }

    private List<Aka> readAkas(String basicsId, String nextBasicsId) {
        List<Aka> akas = new ArrayList<>();
        boolean currentTconst = true;
        try {
            akasReader.mark(100000);
            while (currentTconst) {
                String akasLine = akasReader.readLine();
                if (akasLine == null) {
                    currentTconst = false;
                } else {
                    String[] fields = akasLine.split("\t");
                    if (!fields[0].contentEquals(basicsId)) {
                        if (checkBasicIdHigher(basicsId, fields[0])) {
                            readUntilNextIdNotExists(basicsId);
                        } else if (!checkEqualIds(basicsId, fields[0])) {
                            currentTconst = false;
                        }
                    } else {
                        akas.add(new Aka(fields[2], fields[3], fields[4], fields[7].contentEquals("1")));
                    }
                }
            }
            akasReader.reset();
            if (!checkEqualIds(basicsId, nextBasicsId)) {
                readUntilNextId(basicsId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return akas;
    }

    private void readUntilNextIdNotExists(String basicsId) {
        boolean differentTConst = true;
        try {
            while (differentTConst) {
                akasReader.mark(100000);
                String akasLine = akasReader.readLine();
                String[] fields = akasLine.split("\t");
                if (!checkBasicIdHigher(basicsId, fields[0])) {
                    differentTConst = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUntilNextId(String basicsId) {
        boolean differentTConst = true;
        try {
            while (differentTConst) {
                akasReader.mark(1000);
                String akasLine = akasReader.readLine();
                String[] fields = akasLine.split("\t");
                if (!checkBasicIdHigher(basicsId, fields[0])) {
                    differentTConst = false;
                }
            }
            akasReader.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkBasicIdHigher(String basicsTconst, String akasTconst) {
        int tconstId = StringIntegerConversion.toInt(basicsTconst.substring(2, 9));
        int akasId = StringIntegerConversion.toInt(akasTconst.substring(2, 9));

        return tconstId > akasId;
    }

    private boolean checkEqualIds(String tconst1, String tconst2) {
        int id1 = StringIntegerConversion.toInt(tconst1.substring(2, 9));
        int id2 = StringIntegerConversion.toInt(tconst2.substring(2, 9));

        return id1 == id2;
    }

    public boolean hasDocuments() {
        return hasDocuments;
    }
}
