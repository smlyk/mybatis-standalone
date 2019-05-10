package com.smlyk.mapper;


import com.smlyk.model.Blog;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer bid);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Integer bid);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    List<Blog> selectBlogList(RowBounds rowBounds);
}