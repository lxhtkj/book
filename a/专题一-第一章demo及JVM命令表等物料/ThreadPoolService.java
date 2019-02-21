import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolService {


    //提交15个执行时间需要0.001  5秒的任务，看线程池的状态
    public static void service(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        for (int i = 0; i < 15; i++) {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("begin.....");
                        //Thread.sleep(1L);
                        Thread.sleep(10000L);
                        System.out.println("end.....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("提交任务成功！！！");
        System.out.println("当前线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前等待线程数量： " + threadPoolExecutor.getQueue().size());
        //System.out.println("等待0.1秒");
        //Thread.sleep(100L);
        //System.out.println("0.1秒后线程池线程数量：" + threadPoolExecutor.getPoolSize());
        //System.out.println("0.1秒后等待线程数量： " + threadPoolExecutor.getQueue().size());
        System.out.println("等待8秒");
        Thread.sleep(8000L);
        System.out.println("8秒后线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("8秒后等待线程数量： " + threadPoolExecutor.getQueue().size());
    }


    /**
     * 线程池信息：核心线程数量5，最大数量10，存活时间0，队列LinkedBlockingQueue
     */
    public void serviceTest1() throws Exception {
        System.out.println("系统CPU核数：" + Runtime.getRuntime().availableProcessors());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        service(threadPoolExecutor);
        threadPoolExecutor.shutdown();
    }




    //提交15个执行时间需要5秒的任务，看线程池的状态
    public static void service2(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        for (int i = 0; i < 15; i++) {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("begin.....");
                        Thread.sleep(5000L);
                        System.out.println("end.....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("提交任务成功！！！");
        System.out.println("当前线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前等待线程数量： " + threadPoolExecutor.getQueue().size());
        System.out.println("等待5.5秒");
        Thread.sleep(5500L);
        System.out.println("5.5秒后线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("5.5秒后等待线程数量： " + threadPoolExecutor.getQueue().size());
    }




    /**
     * 线程池信息：核心线程数量5，最大数量10，存活时间5秒，队列LinkedBlockingQueue
     */
    public void serviceTest2() throws Exception {
        System.out.println("系统CPU核数：" + Runtime.getRuntime().availableProcessors());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        service2(threadPoolExecutor);
        threadPoolExecutor.shutdown();
    }



    //提交15个执行时间需要5秒的任务，看线程池的状态
    public static void service3(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        for (int i = 0; i < 15; i++) {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("begin.....");
                        Thread.sleep(1L);
                        //Thread.sleep(5000L);
                        System.out.println("end.....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("提交任务成功！！！");
        System.out.println("当前线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前等待线程数量： " + threadPoolExecutor.getQueue().size());
        System.out.println("等待5.5秒");
        Thread.sleep(5500L);
        System.out.println("5.5秒后线程池线程数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("5.5秒后等待线程数量： " + threadPoolExecutor.getQueue().size());
    }




    /**
     * 线程池信息：核心线程数量5，最大数量10，存活时间5秒，队列SynchronousQueue
     */
    public void serviceTest3() throws Exception {
        System.out.println("系统CPU核数：" + Runtime.getRuntime().availableProcessors());
        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 10, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        //service3(threadPoolExecutor);
        //threadPoolExecutor.shutdown();

        ThreadPoolExecutor threadPoolExecutor1 = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        service3(threadPoolExecutor1);
        threadPoolExecutor1.shutdown();
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolService service = new ThreadPoolService();
        //service.serviceTest1(); //CPU为2时无论任务时间长短，线程池都会创建5个线程
        //service.serviceTest2(); //当任务时间大于存货时间时，线程池会复用存活的线程
        service.serviceTest3();//任务数小于核心线程数时，会创建任务需要的线程数
    }
}