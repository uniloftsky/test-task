import analytics.Records;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

    /*
        Client`s code
        Reading records from file and working with them using filterData method
    */
    public static void main(String[] args) {
        List<String> recordsList = new ArrayList<>();
        Records records = new Records(recordsList, "input.txt");
        records.filterData();
    }

}

