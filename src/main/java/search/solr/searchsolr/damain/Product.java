package search.solr.searchsolr.damain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;
import java.util.List;

@Data
public class Product {
    @Field("id")
    private String id;

    private String title;

    @Field("prod_title")
    private List<String> prod_title;

    @Field("prod_sell_point")
    private String sellPoint;

    @Field("prod_price")
    private Integer price;

    @Field("prod_category")
    private String category;

    @Field("prod_desc")
    private String desc;

    private Integer num;

    private String barcode;

    private Long cid;

    private Byte status;

    @Field("prod_created")
    private Date created;

    private String created_str;

    @Field("prod_updated")
    private Date updated;

    private String updated_str;

}