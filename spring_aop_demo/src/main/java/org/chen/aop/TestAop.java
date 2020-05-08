package org.chen.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Description:
 * @Author chenjianwen
 * @Date 2020/5/8
 **/
@Aspect
@Slf4j
public class TestAop {

    /**
     * cut1切点
     */
    @Pointcut("execution(* org.chen.aop.PointCutTest.cut1(..))")
    private void cut1(){}

    /**
     * cut2切点
     */
    @Pointcut("execution(* org.chen.aop.PointCutTest.cut2(..))")
    private void cut2(){}

    /**
     * cut1或cut2切点
     */
    @Pointcut("cut1() || cut2()")
    private void cut1AndCut2(){}


    @Around("cut1()")
    public void testCut1(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); //获取切点的入参
        Object obj = joinPoint.proceed(); //获取切点的返回值
        System.out.println(args);
        System.out.println(obj);
    }

    @Around("cut2()")
    public void testCut2(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); //获取切点的入参
        Object obj = joinPoint.proceed(); //获取切点的返回值
        System.out.println(args);
        System.out.println(obj);
    }

    @Around("cut1AndCut2()")
    public void testcut1AndCut2(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); //获取切点的入参
        Object obj = joinPoint.proceed(); //获取切点的返回值
        System.out.println(args);
        System.out.println(obj);
    }
}
