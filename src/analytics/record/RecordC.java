package analytics.record;

import java.time.LocalDate;

/**
 * RecordC is a class for creating objects from timeline lines
 */
public class RecordC extends AbstractRecord {

    public RecordC(String line) {
        super(line);
    }

    public LocalDate getDate() {
        return LocalDate.parse(filters[4], formatter);
    }

    public String getTime() {
        return filters[5];
    }
}
