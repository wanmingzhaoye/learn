package com.djt.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.net.InetAddress;

/**
 * @author dajiangtai
 * @create 2020-10-10-10:42
 */
public class StartDSJCount {
    private IDownLoadService downLoadSerivce ;
    private IProcessService processService;
    private IRepositoryService repositoryService;

    //构造函数  建立连接
    public StartDSJCount() {
        //重试策略:重试3次，每次间隔时间指数增长(有具体增长公式)
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        //zk地址
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZKUtil.ZOOKEEPER_HOSTS, retryPolicy);
        //建立连接
        client.start();
        try {
            //获取本地ip地址
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            //每启动一个爬虫应用，创建一个临时节点，子节点名称为当前ip
            //creatingParentsIfNeeded :创建一个临时节点如果父节点不存在也创建父节点
            //withACL
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(ZKUtil.PATH+"/"+ip);
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {

        StartDSJCount dsj = new StartDSJCount();
        dsj.setDownLoadSerivce(new HttpClientDownLoadService());
        dsj.setRepositoryService(new QueueRepositoryService());
        dsj.setProcessService(new ProcessServiceImpl());
        //大学排行
        String url ="http://www.lrts.me/book/category/3058";
        dsj.repositoryService.addHighLevel(url);
        //开启爬虫
        dsj.startSpider();
    }


    /**
     * 开启一个爬虫入口
     */
    public void startSpider(){
        while(true){
            //数据仓库提取解析url
            final String url = repositoryService.poll();

            //判断url是否为空
            if(StringUtils.isNotBlank(url)){

            //下载
            Page page = StartDSJCount.this.downloadPage(url);

                StartDSJCount.this.processPage(page);
            //解析

                       ThreadUtil.sleep((long) (Math.random() * 5000));
                         }else{
                System.out.println("队列中的url解析完毕，请等待！");
                ThreadUtil.sleep((long) (Math.random() * 5000));
            }
        }
    }

    /**
     * 下载页面
     * @param url
     * @return
     */
    public Page downloadPage(String url){
        return this.downLoadSerivce.download(url);
    }

    /**
     * 页面解析
     * @param page
     */
    public void processPage(Page page){
        this.processService.process(page);
    }

    public IDownLoadService getDownLoadSerivce() {
        return downLoadSerivce;

    }

    public void setDownLoadSerivce(IDownLoadService downLoadSerivce) {
        this.downLoadSerivce = downLoadSerivce;
    }

    public IProcessService getProcessService() {
        return processService;
    }

    public void setProcessService(IProcessService processService) {
        this.processService = processService;
    }


    public IRepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

}
