package com.zhangchen.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * 客户端代码常用套路
 * 1、获取一个客户端对象
 * 2、执行相关的操作命令
 * 3、关闭资源
 * HDFS zookeeper
 */
public class HdfsClient {

    private FileSystem fs;
    @Before
    public void inti() throws IOException, InterruptedException, URISyntaxException {
        //连接集群nn的地址
        URI uri=new URI("hdfs://hadoop102:8020");
        //创建一个配置文件
        Configuration configuration=new Configuration();
        //用户
        String user="zhangchen";
        // 1 获取到了客户端对象
        fs = FileSystem.get(uri,configuration,user);
    }

    @After
    public void close() throws IOException {
        // 3 关闭资源
        fs.close();
    }
    // 创建目录
    @Test
    public void testmkdir() throws URISyntaxException, IOException, InterruptedException {

        // 2 创建一个文件夹
        fs.mkdirs(new Path("/xiyou/huaguoshan"));

    }

    // 上传
    @Test
    public  void tetsPut() throws IOException {
        // 参数解读：参数一：表示删除源数据；参数二：是否允许覆盖；参数三：原数据路径；参数四：目的地路径
        fs.copyFromLocalFile(false,true,new Path("C:\\atlog.txt"),new Path("hdfs://hadoop102:/xiyou/huaguoshan"));
    }

    // 文件下载
    @Test
    public void testGet() throws IOException {
        // 参数的解读：参数一：原文件是否铲除 参数二：原文件的路径HDFS  参数三：目标地址路径WIN 参数四：
        fs.copyToLocalFile(false,new Path("hdfs://hadoop102:/xiyou/huaguoshan/atlog.txt"),new Path("C:\\"),false);
    }

    // 删除
    @Test
    public void testRm() throws IOException {
        // 参数解读：参数一：要删除的路径   参数二：是否递归删除
        // 删除文件
        fs.delete(new Path("hdfs://hadoop102:/xiyou"),true);

        // 删除空目录
        fs.delete(new Path("hdfs://hadoop102:/wcoutput"),false);

        //删除非空目录
        fs.delete(new Path("hdfs://hadoop102:/wcinput"),true);
    }

    // 文件的更名和移动
    @Test
    public void testMv() throws IOException {
        // 参数解读： 参数1：原文件路径  参数2：目标文件路径
        // 对文件名称的修改
        fs.rename(new Path("hdfs://hadoop102:/xiyou/huaguoshan/atlog.txt"), new Path("hdfs://hadoop102:/xiyou/huaguoshan/a.txt"));

        // 文件的移动和更名
        fs.rename(new Path("hdfs://hadoop102:/xiyou/huaguoshan/a.txt"),new Path("hdfs://hadoop102:/b.txt"));

        //目录更名
        fs.rename(new Path("hdfs://hadoop102:/xiyou/huaguoshan"),new Path("hdfs://hadoop102:/bbbb"));

    }

    // 获取文件详细信息
    @Test
    public void fileDetail() throws IOException {
        RemoteIterator<LocatedFileStatus> listFiles=fs.listFiles(new Path("/"),true);

        while (listFiles.hasNext()){
            LocatedFileStatus fileStatus= listFiles.next();
            System.out.println("================"+fileStatus.getPath()+"=========================");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // 获取块信息
            BlockLocation[] blockLocation=fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocation));
        }
    }

    // 判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {
        FileStatus[] listStatus= fs.listStatus(new Path("/"));

        for (FileStatus status : listStatus){
            if (status.isFile()){
                System.out.println("文件"+ status.getPath().getName());
            }else {
                System.out.println("目录"+ status.getPath().getName());
            }
        }

    }
}
