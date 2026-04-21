package ru.kuznetsov.shop.generator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.shop.generator.model.MockUserRecord;

@Repository
public interface MockUserRecordRepository extends CrudRepository<MockUserRecord, Integer> {

    MockUserRecord getByUserName(String userName);
}
