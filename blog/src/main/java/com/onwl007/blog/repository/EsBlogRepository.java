package com.onwl007.blog.repository;

import com.onwl007.blog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Blog 存储库
 * @author onwl007@126.com
 * @date 2017/10/25 20:49
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,String>{

    /**
     * 模糊查询(去重)
     * @param title
     * @param Summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,String Summary,String content,String tags,Pageable pageable);

    EsBlog findByBlogId(Long blogId);
}