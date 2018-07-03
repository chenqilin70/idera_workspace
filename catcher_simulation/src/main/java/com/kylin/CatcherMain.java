package com.kylin;

import com.kylin.util.CatcherRecursiveTask;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

public class CatcherMain {
    public static void main(String[] args){
        ForkJoinPool pool = new ForkJoinPool();
        CatcherRecursiveTask task = new CatcherRecursiveTask(1,999);
        ForkJoinTask<List<File>> result = pool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
