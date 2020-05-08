package org.chen.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020/5/8
 **/
@Data
@Document(collection = "user")
public class User {
    @Id
    private ObjectId id;
    private String name;
    private Integer age;
}
