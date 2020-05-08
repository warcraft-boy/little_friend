package org.chen.repository;

import org.bson.types.ObjectId;
import org.chen.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020/5/8
 **/
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
}
