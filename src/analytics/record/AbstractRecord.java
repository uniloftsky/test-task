package analytics.record;

import java.time.format.DateTimeFormatter;

public abstract class AbstractRecord {

    protected final String[] filters;

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public AbstractRecord(String line) {
        this.filters = line.split(" ");
    }

    public String[] getServiceFilter() {
        return filters[1].split("\\.");
    }

    public String[] getQuestionFilter() {
        return filters[2].split("\\.");
    }

    public String getResponseType() {
        return filters[3];
    }

}
