package co.empathy.academy.search.util;

import co.empathy.academy.search.models.Aka;
import co.empathy.academy.search.models.Director;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.Principal;
import co.empathy.academy.search.models.principals.Name;
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
    private final BufferedReader principalsReader;
    private final int documentsSize = 20000;

    private boolean hasDocuments = true;


    public IMDbReader(MultipartFile basicsFile, MultipartFile ratingsFile, MultipartFile akasFile, MultipartFile crewFile,
                      MultipartFile principalsFile) {
        try {
            this.basicsReader = new BufferedReader(new InputStreamReader(basicsFile.getInputStream()));
            this.ratingsReader = new BufferedReader(new InputStreamReader(ratingsFile.getInputStream()));
            this.akasReader = new BufferedReader(new InputStreamReader(akasFile.getInputStream()));
            this.crewReader = new BufferedReader(new InputStreamReader(crewFile.getInputStream()));
            this.principalsReader = new BufferedReader(new InputStreamReader(principalsFile.getInputStream()));
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
            principalsReader.readLine();
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
                String nextBasicsId = null;
                String nextLine = basicsReader.readLine();
                if (nextLine != null) {
                    nextBasicsId = nextLine.split("\t")[0];
                }
                basicsReader.reset();
                List<Aka> akas = readAkas(basics[0], nextBasicsId);

                List<Director> directors = readDirectors();

                List<Principal> principals = readPrincipals(basics[0], nextBasicsId);

                Movie movie = new Movie(basics[0], basics[1], basics[2], basics[3], basics[4].contentEquals("1"),
                        StringIntegerConversion.toInt(basics[5]), StringIntegerConversion.toInt(basics[6]),
                        StringIntegerConversion.toInt(basics[7]), StringArrayConversion.toArray(basics[8]),
                        averageRating, numVotes, akas, directors, principals);

                result.add(movie);

                currentLinesRead++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private List<Principal> readPrincipals(String basicsId, String nextBasicsId) {
        List<Principal> principals = new ArrayList<>();
        boolean currentTconst = true;
        try {
            principalsReader.mark(100000);
            while (currentTconst) {
                String principalsLine = principalsReader.readLine();
                if (principalsLine == null) {
                    currentTconst = false;
                } else {
                    String[] fields = principalsLine.split("\t");
                    if (!fields[0].contentEquals(basicsId)) {
                        if (checkBasicIdHigher(basicsId, fields[0])) {
                            readUntilNextIdNotExists(basicsId, principalsReader);
                        } else if (!checkEqualIds(basicsId, fields[0])) {
                            currentTconst = false;
                        }
                    } else {
                        principals.add(new Principal(new Name(fields[2]), fields[5]));
                    }
                }
            }
            principalsReader.reset();
            if (nextBasicsId != null && !checkEqualIds(basicsId, nextBasicsId)) {
                readUntilNextId(basicsId, principalsReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return principals;
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
                            readUntilNextIdNotExists(basicsId, akasReader);
                        } else if (!checkEqualIds(basicsId, fields[0])) {
                            currentTconst = false;
                        }
                    } else {
                        akas.add(new Aka(fields[2], fields[3], fields[4], fields[7].contentEquals("1")));
                    }
                }
            }
            akasReader.reset();
            if (nextBasicsId != null && !checkEqualIds(basicsId, nextBasicsId)) {
                readUntilNextId(basicsId, akasReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return akas;
    }

    private void readUntilNextIdNotExists(String basicsId, BufferedReader reader) {
        boolean differentTConst = true;
        try {
            while (differentTConst) {
                reader.mark(100000);
                String line = reader.readLine();
                String[] fields = line.split("\t");
                if (!checkBasicIdHigher(basicsId, fields[0])) {
                    differentTConst = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUntilNextId(String basicsId, BufferedReader reader) {
        boolean differentTConst = true;
        try {
            while (differentTConst) {
                reader.mark(1000);
                String line = reader.readLine();
                String[] fields = line.split("\t");
                if (!checkBasicIdHigher(basicsId, fields[0])) {
                    differentTConst = false;
                }
            }
            reader.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkBasicIdHigher(String tconst1, String tconst2) {
        int id1 = StringIntegerConversion.toInt(tconst1.substring(2, 9));
        int id2 = StringIntegerConversion.toInt(tconst2.substring(2, 9));

        return id1 > id2;
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
