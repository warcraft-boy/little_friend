package org.chen;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.chen.model.Department;
import org.chen.model.User;
import org.chen.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest(classes = MongodbDemoApplication.class)
@RunWith(SpringRunner.class)
public class MongodbDemoApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 以下MongoRepository和MongoTemplate都可以操作mongodb，建议两者配合使用
     */
    //=====================================MongoRepository测试==============================
    //插入数据
    @Test
    public void test08(){
        User user = new User();
        user.setName("重楼");
        user.setAge(21);
        userRepository.save(user);
    }
    //查询所有数据
    @Test
    public void test09(){
        List<User> users = userRepository.findAll();
        System.out.println(users);
    }
    //查询某一条数据
    @Test
    public void test10(){
        User user = new User();
        user.setName("重楼");
        Example<User> example = Example.of(user);
        User u = userRepository.findOne(example).get();
        System.out.println(u);
    }
    //批量插入
    @Test
    public void test11(){
        List<User> users = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            User u = new User();
            u.setName("chen" + i);
            u.setAge(18);
            users.add(u);
        }
        userRepository.saveAll(users);
    }
    //查询数据总条数
    @Test
    public void test12(){
        long count = userRepository.count();
        System.out.println(count);
    }
    //按条件查看数据是否存在
    @Test
    public void test13(){
        User user = new User();
        user.setAge(18);
        Example<User> example = Example.of(user);
        boolean exist = userRepository.exists(example);
        System.out.println(exist);
    }
    //按照主键查看数据是否存在
    @Test
    public void test14(){
        ObjectId id = new ObjectId("5df89aa9cc5f11430d46e786");
        boolean exist = userRepository.existsById(id);
        System.out.println(exist);
    }
    //排序查询，按照年龄排序
    @Test
    public void test15(){
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        List<User> users = userRepository.findAll(sort);
        System.out.println(users);
    }
    //分页查询
    @Test
    public void test16(){
        int pageNo = 1;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
        long count = userPage.getTotalElements(); //数据总条数
        List<User> users = userPage.getContent(); //当前页的数据
        System.out.println(count);
        System.out.println(users);
    }

    //====================================MongoTemplate测试==============================
    //插入数据
    @Test
    public void test22(){
        Department d = new Department();
        d.setDeptNo("007");
        d.setDeptName("技术部");
        mongoTemplate.save(d);
        //或者这样：
        //mongoTemplate.insert(d);
    }
    //查询满足某条件的数据（精确查询）
    @Test
    public void test23(){
        Query query = new Query();
        Criteria criteria = Criteria.where("deptNo").is("007");
        query.addCriteria(criteria);
        List<Department> departmentList = mongoTemplate.find(query, Department.class);
        //或者这样：
        //List<Department> departmentList = mongoTemplate.find(new Query(Criteria.where("deptNo").is("007")), Department.class);
        System.out.println(departmentList);
    }
    //查询所有
    @Test
    public void test24(){
        List<Department> departmentList = mongoTemplate.findAll(Department.class);
        System.out.println(departmentList);
    }
    //查询满足多个条件的数据
    @Test
    public void test25(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("deptNo").is("008");
        criteria.and("deptName").is("市场部");
        query.addCriteria(criteria);
        List<Department> departmentList = mongoTemplate.find(query, Department.class);
        System.out.println(departmentList);
    }
    //单条查询
    @Test
    public void test26(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("deptNo").is("008");
        query.addCriteria(criteria);
        Department department = mongoTemplate.findOne(query, Department.class);
        System.out.println(department);
    }
    //模糊查询
    @Test
    public void test27(){
        Pattern pattern = Pattern.compile("^.*" + "00" +".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("deptNo").regex(pattern);
        query.addCriteria(criteria);
        List<Department> departmentList = mongoTemplate.find(query, Department.class);
        System.out.println(departmentList);
    }
    //分页查询 query.limit(size).skip(size*(page-1))
    @Test
    public void test28(){
        Query query = new Query();
        query.limit(1).skip(1);
        List<Department> departmentList = mongoTemplate.find(query, Department.class);
        System.out.println(departmentList);
    }
    //修改
    @Test
    public void test29(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is("5e0d64fb3a571a782f3e1b8c");
        query.addCriteria(criteria);
        Update update = Update.update("deptNo", "123").set("deptName", "王炸");
        UpdateResult ur = mongoTemplate.upsert(query, update, Department.class);
        System.out.println(ur.getModifiedCount());
    }
    //删除
    @Test
    public void test30(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is("5e0d64fb3a571a782f3e1b8c");
        query.addCriteria(criteria);
        DeleteResult dr = mongoTemplate.remove(query, Department.class);
        System.out.println(dr.getDeletedCount());
    }

}
