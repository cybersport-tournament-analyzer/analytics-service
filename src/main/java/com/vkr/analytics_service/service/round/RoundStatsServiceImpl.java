package com.vkr.analytics_service.service.round;

import com.vkr.analytics_service.entity.round.RoundStats;
import com.vkr.analytics_service.repository.round.RoundStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoundStatsServiceImpl implements RoundStatsService {

    private final RoundStatsRepository roundStatsRepository;

    @Override
    public Page<RoundStats> getAll(Pageable pageable) {
        return roundStatsRepository.findAll(pageable);
    }

    @Override
    public void deleteAll() {
        roundStatsRepository.deleteAll();
    }
}
