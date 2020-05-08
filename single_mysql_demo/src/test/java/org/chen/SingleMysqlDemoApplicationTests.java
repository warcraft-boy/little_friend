package org.chen;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.chen.dao.StudentMapper;
import org.chen.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = SingleMysqlDemoApplication.class)
@RunWith(SpringRunner.class)
public class SingleMysqlDemoApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    //=========================mybatisplus测试==============================
    /**
     * 查询所有学生信息
     */
    @Test
    public void test01(){
        List<Student> studentList = studentMapper.selectList(null);
        studentList.forEach(System.out::println);
    }
    /**
     * 查询符合某条件的学生信息
     */
    @Test
    public void test02(){
        //List<Student> studentList = studentMapper.selectList(new QueryWrapper<Student>().eq("id",2));
        List<Student> studentList2 = studentMapper.selectList(Wrappers.<Student>lambdaQuery().eq(Student::getId,1));
        //studentList.forEach(System.out::println);
        studentList2.forEach(System.out::println);
    }
    /**
     * 查询符合某条件的学生数
     */
    @Test
    public void test03(){
        Integer count = studentMapper.selectCount(Wrappers.<Student>lambdaQuery().eq(Student::getClassName,"一班"));
        System.out.println(count);
    }
    /**
     * 根据条件查询某一具体对象
     */
    @Test
    public void test04(){
        Student student = studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getId,1));
        System.out.println(student);
    }
    /**
     * 修改
     */
    @Test
    public void test05(){
        int value = studentMapper.update(new Student(),Wrappers.<Student>lambdaUpdate().set(Student::getName,"冥冥").eq(Student::getId,1));
        System.out.println(value);
    }

}
