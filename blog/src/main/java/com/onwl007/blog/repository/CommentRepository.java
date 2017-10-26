package com.onwl007.blog.repository;

import com.onwl007.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment 仓库
 * @author onwl007@126.com
 * @date 2017/10/26 20:49
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
