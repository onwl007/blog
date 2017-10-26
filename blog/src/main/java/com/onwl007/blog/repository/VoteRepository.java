package com.onwl007.blog.repository;

import com.onwl007.blog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vote 仓库
 *
 * @author onwl007@126.com
 * @date 2017/10/26 20:58
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
