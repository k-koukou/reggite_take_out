package com.itkoukou.reggie.common;

/**
 * @className BaseContext
 * @userName 李院斌
 * @DATA 2023-02-08 23:29
 * @nickName 辻弎
 **/
/*基于threadLocal封装的工具类 ， 用于保存以及获取当前用户ID

    只能作用于一个线程之内
 */

public class BaseContext {
    private static ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<> ();
    //给线程中传入数据
    public static void setCurrentId(Long id){
        THREAD_LOCAL.set (id);
    }
    //将线程中的数据拿出来
    public static Long getCurrentById(){
        return THREAD_LOCAL.get ();
    }
    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return THREAD_LOCAL.get();
    }

}
