package com.vkr.analytics_service.repository.player.comparison;

import com.vkr.analytics_service.entity.player.comparisons.Duels;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuelsRepository extends ElasticsearchRepository<Duels, String> {
    List<Duels> findAllByScopeAndScopeId(String scope, String scopeId);
}
