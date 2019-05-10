package com.smlyk;

import com.smlyk.mapper.BlogMapper;
import com.smlyk.model.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author yekai
 */
public class MybatisTest {

    @Test
    public void testSelectByRowBounds() throws Exception{

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);

        int offset = 0;
        int limit = 5;
        RowBounds rowBounds = new RowBounds(offset, limit);
        List<Blog> blogList = blogMapper.selectBlogList(rowBounds);
        blogList.stream()
                .forEach(blog -> System.out.println(blog));

    }


}
