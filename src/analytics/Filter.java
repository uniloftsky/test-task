package analytics;

import analytics.record.RecordC;
import analytics.record.RecordD;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Verify query for matching data lines
 */
public class Filter {

    private final String[] serviceFilter;
    private final String[] questionFilter;
    private final String responseType;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public Filter(RecordD recordD) {
        this.serviceFilter = recordD.getServiceFilter();
        this.questionFilter = recordD.getQuestionFilter();
        this.responseType = recordD.getResponseType();
        this.dateFrom = recordD.getDateFrom();
        this.dateTo = recordD.getDateTo();
    }

    /**
     * Combining all criteria
     * @param recordC timeline record what will be checked for a matching
     * @return true if matches, false if dont
     */
    private boolean filterByAll(RecordC recordC) {
        return filterByService(recordC) && filterByQuestion(recordC) && filterByResponseType(recordC) && filterByDate(recordC);
    }

    /**
     * Summary processing. Returning a list of timelines that matches for future checking.
     * If the query matches one or more timelines, outputs average wait time
     * @param recordsC list of lines that matches
     * @return list of lines that matches for future checking
     */
    public List<RecordC> filter(List<RecordC> recordsC) {
        List<RecordC> foundRecords = new ArrayList<>();
        for (RecordC recordC : recordsC) {
            if (filterByAll(recordC)) {
                foundRecords.add(recordC);
            }
        }
        //calculation and displaying average time of waiting
        OptionalDouble avg = foundRecords.stream().mapToInt(v -> Integer.parseInt(v.getTime())).average();
        if (avg.isPresent()) {
            System.out.println(avg.getAsDouble());
        }
        return foundRecords;
    }

    /**
     * Checks for a service and variation match
     * @param recordC timeline record what will be checked for a matching
     * @return true if matches, false if dont
     */
    private boolean filterByService(RecordC recordC) {
        //if the query contains "*" it means the query matches all services types
        if (serviceFilter[0].equals("*")) {
            return true;
        } else if (serviceFilter.length == 2) {
            return Arrays.equals(serviceFilter, recordC.getServiceFilter());
        } else return serviceFilter[0].equals(recordC.getServiceFilter()[0]);
    }

    /**
     * Checks for a question type, category, and subcategory match
     * @param recordC timeline record what will be checked for a matching
     * @return true if matches, false if dont
     */
    private boolean filterByQuestion(RecordC recordC) {
        //if the query contains "*" it means the query matches all question types
        if (questionFilter[0].equals("*")) {
            return true;
        } else if (questionFilter.length == 3) {
            return Arrays.equals(questionFilter, recordC.getQuestionFilter());
        } else if (questionFilter.length == 2) {
            return Arrays.equals(questionFilter, Arrays.stream(recordC.getQuestionFilter()).limit(2).toArray());
        } else return questionFilter[0].equals(recordC.getQuestionFilter()[0]);
    }

    /**
     * Checks for a response type match
     * @param recordC timeline record what will be checked for a matching
     * @return true if matches, false if dont
     */
    private boolean filterByResponseType(RecordC recordC) {
        if (responseType.equals("*")) {
            return true;
        } else {
            return responseType.equals(recordC.getResponseType());
        }
    }

    /**
     * Checks for a specified date or date range match
     * @param recordC timeline record what will be checked for a matching
     * @return true if matches, false if dont
     */
    private boolean filterByDate(RecordC recordC) {
        if (dateTo == null) {
            return recordC.getDate().equals(dateFrom);
        } else {
            return (recordC.getDate().isAfter(dateFrom) || recordC.getDate().equals(dateFrom)) && (recordC.getDate().isBefore(dateTo) || recordC.getDate().equals(dateTo));
        }
    }
}
