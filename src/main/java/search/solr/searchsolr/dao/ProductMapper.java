package search.solr.searchsolr.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import search.solr.searchsolr.damain.Product;

import java.util.List;

@Mapper
@Component
public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    List<Integer> selectCid();

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}