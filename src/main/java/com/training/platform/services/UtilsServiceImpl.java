package com.training.platform.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class UtilsServiceImpl implements UtilsService {
    private Executor executor = null;
    @Override
    public String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    @Override
    public Executor getExecutor(){
        //Singeton ThreadPoolExecutor
        if(executor == null) {
            //default thread pool setting
            Integer initialPoolSize = 200;
            Integer maxPoolSize = 4000;
            Integer keepAliveTime = 1;
            TimeUnit unitTime = TimeUnit.MINUTES;

            //override thread pool setting by inject parameter
            initialPoolSize = (System.getProperty("THREAD_INIT_POOL_SIZE") != null  && !System.getProperty("THREAD_INIT_POOL_SIZE").equals("")) ? Integer.parseInt(System.getProperty("THREAD_INIT_POOL_SIZE")) : initialPoolSize;
            maxPoolSize = (System.getProperty("THREAD_MAX_POOL_SIZE") != null  && !System.getProperty("THREAD_MAX_POOL_SIZE").equals("")) ? Integer.parseInt(System.getProperty("THREAD_MAX_POOL_SIZE")) : maxPoolSize;
            keepAliveTime = (System.getProperty("THREAD_KEEP_ALIVE_TIME") != null  && !System.getProperty("THREAD_KEEP_ALIVE_TIME").equals("")) ? Integer.parseInt(System.getProperty("THREAD_KEEP_ALIVE_TIME")) : keepAliveTime;


            //Docs => ThreadPoolExecutor(initialPoolSize,maxPoolSize,keepAliveTime,unitTime,QueueMode,ThreadPolicy)
            executor = new java.util.concurrent.ThreadPoolExecutor(initialPoolSize, maxPoolSize, keepAliveTime, unitTime, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return executor;
    }

}