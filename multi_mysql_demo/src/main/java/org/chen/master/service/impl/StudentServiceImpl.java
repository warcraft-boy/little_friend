package org.chen.master.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.chen.master.dao.StudentMapper;
import org.chen.master.model.Student;
import org.chen.master.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @Description: <br>
 * @Date: Created in 2020/3/10 <br>
 * @Author: chenjianwen
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
