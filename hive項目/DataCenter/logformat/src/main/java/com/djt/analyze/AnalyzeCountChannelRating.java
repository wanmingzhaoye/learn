package com.djt.analyze;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.djt.dao.AbstractTVDAO;
import com.djt.dao.ChannelDao;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * 针对上一步的结果统计频道每天每分钟的收视指标
 * 
 */
public class AnalyzeCountChannelRating extends Configured implements Tool {
	public static class AnalyzeCountChannelRatingMapper extends
			Mapper<Object, Text, Text, Text> {
		// 存储当前在播数集合
		private Map<String, String> curNumMap = new HashMap<String, String>();
		private AbstractTVDAO ChannelDao = null;
		@SuppressWarnings("deprecation")
		protected void setup(Context context) throws IOException,
				InterruptedException {
			// 返回缓存文件路径
			Path[] cacheFilesPaths = context.getLocalCacheFiles();
			this.ChannelDao =  new ChannelDao();
			curNumMap = ChannelDao.parseTVObj(cacheFilesPaths);
		}

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			System.out.println("value="+value);
			// channel0 + "@" + date1 + "@" + min2+avgnum3 +reachnum4
			String[] kv = StringUtils.split(value.toString(), "@");
			if (kv.length != 5) {
				return;
			}
			// 平均收视人数
			int avgnum = Integer.parseInt(kv[3].trim());
			// 到达人数
			int reachnum = Integer.parseInt(kv[4].trim());
			// 当前在播数
			int currentStbnum = Integer.parseInt(curNumMap.get(kv[1].trim()
					+ "@" + kv[2].trim()));
			// 收视率
			float tvrating = (float) avgnum / 25000 * 100;
			// 市场份额
			float marketshare = (float) avgnum / currentStbnum * 100;
			// 到达率
			float reachrating = (float) reachnum / 25000 * 100;
			// 将计算的所有指标输出
			context.write(value, new Text(tvrating + "@" + reachrating + "@"
					+ marketshare));

		}
	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "AnalyzeCountChannelRating");

		// 设置输出key value分隔符
		job.getConfiguration().set("mapreduce.output.textoutputformat.separator", "@");
		// 添加缓存文件
		FileSystem fs = FileSystem.get(conf);
		String cachePathStr = job.getConfiguration().get("cachePath");
		FileStatus[] dirstatus = fs.listStatus(new Path(cachePathStr));
		for (FileStatus file : dirstatus) {
			job.addCacheFile(file.getPath().toUri());
		}
		job.setJarByClass(AnalyzeCountChannelRating.class);
		job.setMapperClass(AnalyzeCountChannelRatingMapper.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 设置输入路径
		String inputPathStr = job.getConfiguration().get("inputPath");
		FileInputFormat.addInputPath(job, new Path(inputPathStr));

		// 设置输出路径
		String outPathStr = job.getConfiguration().get("outputPath");
		FileOutputFormat.setOutputPath(job, new Path(outPathStr));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int ec = ToolRunner.run(new Configuration(),
				new AnalyzeCountChannelRating(), args);
		System.exit(ec);
	}
}
