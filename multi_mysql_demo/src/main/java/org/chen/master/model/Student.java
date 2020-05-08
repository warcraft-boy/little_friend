package org.chen.master.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student")
public class Student {

    /**
     * 学生主键
     */
    @TableId(value = "id", type = IdType.AUTO) //如果数据库设置成主键自增，那么这里需要配置@TableId注解
    private Integer id;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 班级主键
     */
    private Integer classId;
    /**
     * 班级名称
     */
    private String className;

}