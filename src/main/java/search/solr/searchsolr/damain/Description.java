package search.solr.searchsolr.damain;

import lombok.Data;

import java.util.Date;

@Data
public class Description {
    private Long pid;

    private Date created;

    private Date updated;

    private String desc;

}