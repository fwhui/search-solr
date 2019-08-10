package search.solr.searchsolr.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import search.solr.searchsolr.damain.Conditions;
import search.solr.searchsolr.service.SolrService;

import java.util.Map;

@Controller
public class IndexController {

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Autowired
    SolrService solrService;


    @RequestMapping(value = {"/index","/"})
    public String index(Model model){
        model.addAttribute("categorys",solrService.getCname());
        return "index";
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String,Object> search(@RequestParam(value = "conditions",defaultValue = "{}")String conditions_json, @RequestParam(value = "offset",defaultValue = "1")Integer offset, @RequestParam(value = "size",defaultValue = "12")Integer size) throws Exception{
        Conditions conditions=gson.fromJson(conditions_json, Conditions.class);
        Map<String,Object> data=solrService.search(conditions,offset,size);
        return data;
    }
}
