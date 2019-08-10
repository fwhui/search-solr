package search.solr.searchsolr.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import search.solr.searchsolr.damain.Conditions;
import search.solr.searchsolr.damain.Product;
import search.solr.searchsolr.dao.CategoryMapper;
import search.solr.searchsolr.dao.ProductMapper;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SolrService {

    private static final String SOLR_URL = "http://localhost:8983/solr/search";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    // 获取存在商品信息的类目名称
    public List<String> getCname(){
        List<Integer> cids = productMapper.selectCid();
        return categoryMapper.selectNames(cids);
    }

    // 查询
    public Map<String,Object> search(Conditions conditions,Integer offset, Integer size) throws Exception{
        Map<String,Object> data=new HashMap<>();

        if(offset<1){
            data.put("code",-1);
            data.put("message","请求页码不在正常范围:)");
            return data;
        }

        SolrQuery query = new SolrQuery();
        query.setStart((offset-1)*size);
        query.setRows(size);

        // 关键字筛选
        if(StringUtils.isEmpty(conditions.getKeyword())){
            query.set("q","*:*");
        }else{
            String q="prod_title:"+conditions.getKeyword()+ " OR ";
            q+="prod_sell_point:"+conditions.getKeyword()+ " OR ";
            q+="prod_desc:"+conditions.getKeyword();
            query.set("q",q);
        }

        // 类目筛选
        if(!StringUtils.isEmpty(conditions.getCategory())){
            query.addFilterQuery("prod_category:"+conditions.getCategory());
        }

        // 价格范围筛选
        if(!StringUtils.isEmpty(conditions.getPrice_range())){
            String price_range = conditions.getPrice_range();
            String range1="*";
            String range2="*";
            if(price_range.contains("以上")){
                range1=price_range.replace("以上","").trim();
            }else if(price_range.contains("以下")){
                range2=price_range.replace("以下","").trim();
            }else{
               String[] range = price_range.split("-");
               range1=range[0].trim();
               range2=range[1].trim();
            }
            query.addFilterQuery("prod_price:["+range1+" TO "+range2+"]");
        }

        // 价格排序
        if(!StringUtils.isEmpty(conditions.getPrice_sort())){
            if("asc".equals(conditions.getPrice_sort())){
                query.addSort("prod_price", ORDER.asc);
            }else{
                query.addSort("prod_price", ORDER.desc);
            }
        }
        // 上架时间排序
        else if(!StringUtils.isEmpty(conditions.getCreate_sort())){
            if("asc".equals(conditions.getCreate_sort())){
                query.addSort("prod_created", ORDER.asc);
            }else{
                query.addSort("prod_created", ORDER.desc);
            }
        }
        // 更新时间排序
        else if(!StringUtils.isEmpty(conditions.getUpdate_sort())){
            if("asc".equals(conditions.getUpdate_sort())){
                query.addSort("prod_updated", ORDER.asc);
            }else{
                query.addSort("prod_updated", ORDER.desc);
            }
        }

        QueryResponse response=client.query(query);

        List<Product> products=response.getBeans(Product.class);
        for (Product product:products) {
            String title=product.getProd_title().get(0);
            product.setTitle(title);
            product.setProd_title(null);
            product.setDesc(null);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            product.setCreated_str(sdf.format(product.getCreated()));
            product.setUpdated_str(sdf.format(product.getUpdated()));
            product.setCreated(null);
            product.setUpdated(null);
        }

        int pages=(int)Math.ceil(((float)response.getResults().getNumFound())/size);
        int currentPage=offset;
        int prev=1;
        int next=1;
        if(currentPage>pages){
            data.put("code",-1);
            data.put("message","请求页码不在正常范围:)");
            return data;
        }
        if(currentPage>1){
            prev=currentPage-1;
        }
        if(currentPage<pages){
            next=currentPage+1;
        }
        if(currentPage==pages){
            next=currentPage;
        }
        data.put("code",0);
        data.put("pages",pages);
        data.put("data",products);
        data.put("currentPage",currentPage);
        data.put("prev",prev);
        data.put("next",next);
        return data;
    }

    // 添加 & 更新
    public void addDocument(Product product) throws  Exception{
        client.addBean(product);
        client.commit();
    }

    // 根据id删除
    public void deleteDocument(String id) throws  Exception{
        client.deleteById(id);
        client.commit();
    }

    // 根据使用了分词器的字段进行分词
    public List<String> splitWord(String word) throws Exception{
        FieldAnalysisRequest request = new FieldAnalysisRequest("/analysis/field");
        request.addFieldName("prod_title");
        request.setFieldValue("");
        request.setQuery(word);
        FieldAnalysisResponse response = request.process(client);
        List<String> results = new ArrayList<>();
        Iterator<AnalysisPhase> it = response.getFieldNameAnalysis("prod_title").getQueryPhases().iterator();
        while(it.hasNext()) {
            AnalysisPhase pharse = it.next();
            List<TokenInfo> list = pharse.getTokens();
            for (TokenInfo info : list) {
                results.add(info.getText());
            }
        }
        return results;
    }
}
