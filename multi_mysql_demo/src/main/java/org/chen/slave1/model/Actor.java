package org.chen.slave1.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020-03-24
 **/
@TableName("actor")
@Data
public class Actor {

    @TableId(value = "id", type = IdType.AUTO)
    private int actorId;

    private String firstName;

    private String lastName;

    private Date lastUpdate;
}
