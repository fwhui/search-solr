package search.solr.searchsolr.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import search.solr.searchsolr.damain.Description;

@Mapper
@Component
public interface DescriptionMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(Description record);

    int insertSelective(Description record);

    Description selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(Description record);

    int updateByPrimaryKeyWithBLOBs(Description record);

    int updateByPrimaryKey(Description record);
}