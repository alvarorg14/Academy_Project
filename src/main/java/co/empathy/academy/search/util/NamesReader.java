package co.empathy.academy.search.util;

import co.empathy.academy.search.models.Name;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NamesReader {

    private final BufferedReader namesReader;

    private final int documentsSize = 20000;

    private boolean hasDocuments = true;

    public NamesReader(MultipartFile namesFile) {
        try {
            this.namesReader = new BufferedReader(new InputStreamReader(namesFile.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readHeaders();
    }

    private void readHeaders() {
        try {
            namesReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasDocuments() {
        return hasDocuments;
    }

    public List<Name> readDocuments() {
        List<Name> result = new ArrayList<>();
        int currentLinesRead = 0;


        while (currentLinesRead < documentsSize) {
            try {
                String namesLine = namesReader.readLine();

                if (namesLine == null) {
                    this.hasDocuments = false;
                    return result;
                }

                String[] names = namesLine.split("\t");

                Name name = new Name(names[0], names[1], StringIntegerConversion.toInt(names[2]),
                        StringIntegerConversion.toInt(names[3]), StringArrayConversion.toArray(names[4]));

                result.add(name);

                currentLinesRead++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
