package search.solr.searchsolr.damain;

import lombok.Data;

@Data
public class Conditions {

    private String keyword;
    private String category;
    private String price_range;
    private String price_sort;
    private String create_sort;
    private String update_sort;

}
