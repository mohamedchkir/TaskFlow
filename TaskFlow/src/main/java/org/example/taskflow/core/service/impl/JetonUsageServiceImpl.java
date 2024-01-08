package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.model.entity.JetonUsage;
import org.example.taskflow.core.repository.JetonUsageRepository;
import org.example.taskflow.core.service.JetonUsageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JetonUsageServiceImpl implements JetonUsageService {

    private final JetonUsageRepository jetonUsageRepository;


    @Override
    public void doubleNumberOFJetonWhenActionDateIsBeforeNowPlusHalfDay() {

        List<JetonUsage> jetonUsage = jetonUsageRepository.findAll();



    }
}
