package ru.kuznetsov.shop.generator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuznetsov.shop.generator.model.MockUserRecord;
import ru.kuznetsov.shop.generator.repository.MockUserRecordRepository;

@Service
@RequiredArgsConstructor
public class MockUserRecordService {

    private final MockUserRecordRepository mockUserRecordRepository;

    public MockUserRecord getById(Integer id) {
        return mockUserRecordRepository.findById(id).orElse(null);
    }

    public Iterable<MockUserRecord> getAll() {
        return mockUserRecordRepository.findAll();
    }

    public MockUserRecord getByUserName(String userName) {
        return mockUserRecordRepository.getByUserName(userName);
    }

    public MockUserRecord save(MockUserRecord mockUserRecord) {
        return mockUserRecordRepository.save(mockUserRecord);
    }

    public void deleteById(Integer id) {
        mockUserRecordRepository.deleteById(id);
    }
}
