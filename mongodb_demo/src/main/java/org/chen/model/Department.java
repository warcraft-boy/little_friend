package org.chen.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020/5/8
 **/
@Data
@Document(collection = "department")
public class Department {

    @Id
    private ObjectId id;

    @Field("dept_no")
    private String deptNo;

    @Field("dept_name")
    private String deptName;
}
