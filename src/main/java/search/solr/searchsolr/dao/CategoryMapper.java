package search.solr.searchsolr.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import search.solr.searchsolr.damain.Category;

import java.util.List;

@Mapper
@Component
public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Long id);

    List<String> selectNames(@Param("cids") List<Integer> cids);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}