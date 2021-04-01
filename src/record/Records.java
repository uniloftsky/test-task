package record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Holds analytical logic
 */
public class Records {

    //Lines from a file
    private final List<String> lines;

    //RegEx checks lines for valid both for queries and timelines
    private final Pattern cPattern = Pattern.compile("^([C]) (((10\\.[1-3]\\s)|([1-9]\\.[1-3]\\s)|[1-9]\\s|10\\s))((((10|[1-9])\\.(20|1[0-9]|[1-9])\\.[1-5])\\s|((10|[1-9])\\.(20|1[0-9]|[1-9])\\s)|((10|[1-9])\\s)))([PN]) ((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d) (\\d+)");
    private final Pattern dPattern = Pattern.compile("^([D]) ((\\*\\s)|((10\\.[1-3]\\s)|([1-9]\\.[1-3]\\s)|[1-9]\\s|10\\s))((\\*\\s)|(((10|[1-9])\\.(20|1[0-9]|[1-9])\\.[1-5])\\s|((10|[1-9])\\.(20|1[0-9]|[1-9])\\s)|((10|[1-9])\\s)))([PN]) (((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d)-((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d)|(((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d)))");

    public Records(List<String> lines, String fileName) {
        this.lines = lines;
        readData(fileName);
    }

    //Reading lines form a file
    private void readData(String fileName) {
        try (FileReader reader = new FileReader(new File(fileName));
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String inputLine = bufferedReader.readLine();
            while (inputLine != null) {
                lines.add(inputLine);
                inputLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Checks count of lines for a valid value
        if (Integer.parseInt(lines.get(0)) > 100000) {
            throw new RuntimeException("Count of lines must be below 100.000");
        }
    }

    /**
     * Going through lines and finding queries or timelines
     * If a query was found, starting to analyze lines above it
     */
    public void filterData() {
        List<RecordC> recordsC = new ArrayList<>();
        Filter filter;
        Matcher matcherC, matcherD;
        for (int i = 1; i < lines.size(); i++) {
            matcherC = cPattern.matcher(lines.get(i));
            matcherD = dPattern.matcher(lines.get(i));

            //if line matches regex for a timeline
            if (matcherC.find()) {
                recordsC.add(new RecordC(lines.get(i)));
            }
            //if line matches regex for a query
            if (matcherD.find()) {
                RecordD recordD = new RecordD(lines.get(i));
                filter = new Filter(recordD);
                //if query doesnt match any data line, outputs "-"
                if (filter.filter(recordsC).isEmpty()) {
                    System.out.println("-");
                }
            }
        }
    }
}
