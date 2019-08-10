package search.solr.searchsolr;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import search.solr.searchsolr.damain.Conditions;
import search.solr.searchsolr.damain.Product;
import search.solr.searchsolr.dao.CategoryMapper;
import search.solr.searchsolr.dao.ProductMapper;
import search.solr.searchsolr.service.SolrService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchSolrApplicationTests {

	@Autowired
	SolrService solrService;

	private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	@Test
	public void addTest() throws Exception{
		Product product=new Product();

		product.setId("1");
		product.setTitle("hello world");
		product.setSellPoint("Good,Nice");
		product.setPrice(15000);
		product.setCategory("手机");
		product.setDesc("It is xiaomi phone,very good!");
		product.setCreated(new Date());
		product.setUpdated(new Date());
		solrService.addDocument(product);
	}

	@Test
	public void searchTest() throws Exception {
		//String conditions="{'keyword':'老年手机','start':0,'size':10,'category':'手机'}";
		//String conditions="{'start':0,'size':10,'category':'平板电视','price_range':'201000-209000','update_sort':'desc'}";
		String conditions="{}";
		Map<String,Object> data=solrService.search(gson.fromJson(conditions, Conditions.class),0,12);
		System.out.println(JSON.toJSONString(data));
	}

	@Test
	public void splitWordTest() throws Exception{
		List<String> data=solrService.splitWord("全党同志一定要努力奋斗");
		for (String s : data) {
			System.out.println(s);
		}
	}

	@Test
	public void delTest() throws Exception{
		solrService.deleteDocument("1");
	}

	@Autowired
	ProductMapper productMapper;

	@Test
	public void getCid(){
		List<Integer> cid=productMapper.selectCid();
		System.out.println(JSON.toJSONString(cid));
	}

	@Autowired
	CategoryMapper categoryMapper;

	@Test
	public void getName(){
		List<Integer> cid=productMapper.selectCid();
		List<String> names =categoryMapper.selectNames(cid);
		System.out.println(JSON.toJSONString(names));
	}


}
