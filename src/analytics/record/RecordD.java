package analytics.record;

import java.time.LocalDate;

/**
 * RecordD is a class for creating objects from query lines
 */
public class RecordD extends AbstractRecord {

    public RecordD(String line) {
        super(line);
    }

    /**
     * Getting date field from a query
     * @return the date or dateFrom at the specified query
     */
    public LocalDate getDateFrom() {
        return LocalDate.parse(filters[4].split("-")[0], formatter);
    }

    /**
     * If query contains date range, method takes dateTo from it
     * @return the dateTo of date range
     */
    public LocalDate getDateTo() {
        if (filters[4].split("-").length == 2) {
            return LocalDate.parse(filters[4].split("-")[1], formatter);
        } else {
            return null;
        }
    }
}
