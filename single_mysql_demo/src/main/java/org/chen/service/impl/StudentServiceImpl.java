package org.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chen.dao.StudentMapper;
import org.chen.model.Student;
import org.chen.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020/5/8
 **/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
