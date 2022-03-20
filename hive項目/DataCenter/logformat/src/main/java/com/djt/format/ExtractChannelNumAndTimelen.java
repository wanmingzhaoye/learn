package com.djt.format;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.djt.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * 针对LogSplitMR的结果统计每个频道每天的收视人数和人均收视时长
 * 
 */
public class ExtractChannelNumAndTimelen   extends Configured implements Tool {
	public static class ExtractChannelNumAndTimelenMapper extends
			Mapper<LongWritable, Text, Text, Text> {
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//cardNum0@stbNum1@date2@pageWidgetVersion3@e4@s5@n6@t7@pi8@p9@sn10
			String[] kv = StringUtils.split(value.toString(), "@");
			// 过滤掉不合格数据
			if (kv.length != 11) {
				return;
			}
			// 机顶盒号
			String stbnum = kv[1].trim();
			// 日期
			String date = kv[2].trim();
			// 节目
			String channel = kv[10].trim();

			// 起始时间
			int start = TimeUtil.TimeToSecond(kv[5].trim());
			// 结束时间
			int end = TimeUtil.TimeToSecond(kv[4].trim());

			if (end < start) {
				end = end + 24 * 3600;
			}

			//每条记录的收看时长
			int duration = end - start;
			// 输出每条记录用户的机顶盒号和时长
			context.write(new Text(channel + "@" + date), new Text(stbnum + "@"
					+ duration));
		}
	}

	public static class ExtractChannelNumAndTimelenReduce extends
			Reducer<Text, Text, Text, Text> {
		private Text result = new Text();
		// 定义收视人数集合
		private Set<String> set_num = new HashSet<String>();

		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			set_num.clear();
			int timelen = 0;
			for (Text value : values) {
				String[] arr = StringUtils.split(value.toString(), "@");
				set_num.add(arr[0]);
				// 满足到达条件
				if (arr.length > 1) {
					timelen += Integer.parseInt(arr[1]);
				}
			}
			int num = set_num.size();
			// 计算出每天的收视人数和人均收视时长
			result.set(num + "@" + timelen / num);
			context.write(key, result);
		}

	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "ExtractChannelNumAndTimelen");

		// 设置输出key value分隔符
		job.getConfiguration().set(
				"mapreduce.output.textoutputformat.separator", "@");

		job.setJarByClass(ExtractChannelNumAndTimelen.class);
		job.setMapperClass(ExtractChannelNumAndTimelenMapper.class);
		job.setReducerClass(ExtractChannelNumAndTimelenReduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 设置输入路径
		String inputPathStr =job.getConfiguration().get("inputPath");
		FileInputFormat.addInputPath(job, new Path(inputPathStr));

		// 设置输出路径
		String outputPathStr = job.getConfiguration().get("outputPath");
		FileOutputFormat.setOutputPath(job, new Path(outputPathStr));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int ec = ToolRunner.run(new Configuration(),
				new ExtractChannelNumAndTimelen(), args);
		System.exit(ec);
	}
}
