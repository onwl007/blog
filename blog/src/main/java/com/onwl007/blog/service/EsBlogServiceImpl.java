package com.onwl007.blog.service;

import com.onwl007.blog.domain.User;
import com.onwl007.blog.domain.es.EsBlog;
import com.onwl007.blog.repository.EsBlogRepository;
import com.onwl007.blog.vo.TagVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * @author onwl007@126.com
 * @date 2017/10/25 20:46
 */
@Service
public class EsBlogServiceImpl implements EsBlogService{

    @Autowired
    private EsBlogRepository esBlogRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private UserService userService;

    private static final Pageable TOP_5PAGEABLE=new PageRequest(0,5);
    private static final String EMPTY_KEYWORD="";


    /**
     * @param id
     */
    @Override
    public void removeEsBlog(String id) {
        esBlogRepository.delete(id);
    }

    /**
     * @param esBlog
     * @return
     */
    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogRepository.save(esBlog);
    }

    /**
     * @param blogId
     * @return
     */
    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRepository.findByBlogId(blogId);
    }

    /**
     * @param keyword
     * @param pageable
     * @return
     */
    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> pages=null;
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        if (pageable.getSort()==null){
            pageable=new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        pages=esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword,pageable);
        return pages;
    }

    /**
     * @param keyword
     * @param pageable
     * @return
     * @throws SearchParseException
     */
    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) throws SearchParseException{
        Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if (pageable.getSort()==null){
            pageable=new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword,pageable);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    /**
     * 查询最新前5的文章
     * @return
     */
    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page=this.listHotestEsBlogs(EMPTY_KEYWORD,TOP_5PAGEABLE);
        return page.getContent();
    }

    /**
     * 最热前5
     * @return
     */
    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> page=this.listHotestEsBlogs(EMPTY_KEYWORD,TOP_5PAGEABLE);
        return page.getContent();
    }

    /**
     * 根据前30的标签查询
     * @return
     */
    @Override
    public List<TagVO> listTop30Tags() {

        List<TagVO> list=new ArrayList<>();
        //given
        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog").withTypes("blog")
                .addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30))
                .build();
        //when
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms modelTerms=(StringTerms)aggregations.asMap().get("tags");

        Iterator<Bucket> modelBucketIt=modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()){
            Bucket actiontypeBucket=modelBucketIt.next();
            list.add(new TagVO(actiontypeBucket.getKey().toString(),actiontypeBucket.getDocCount()));
        }
        return list;
    }

    @Override
    public List<User> listTop12Users() {
        List<String> usernamelist=new ArrayList<>();
        //given
        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog").withTypes("blog")
                .addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12))
                .build();
        //when
        Aggregations aggregations=elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms modelTerms=(StringTerms)aggregations.asMap().get("users");

        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        List<User> list = userService.listUsersByUsernames(usernamelist);

        return list;
    }
}
