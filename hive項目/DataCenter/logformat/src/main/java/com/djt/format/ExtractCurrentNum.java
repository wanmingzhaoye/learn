package com.djt.format;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
 * 针对LogSplitMR的结果统计每分钟的当前在播数
 * 
 */
public class ExtractCurrentNum  extends Configured implements Tool {
	public static class ExtractCurrentNumMapper extends
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

			// 将时间段解析为每分钟记录，比如23:51:45~23:56:45之间的每分钟
			List<String> list = TimeUtil.getTimeSplit(kv[5], kv[4]);
			int size = list.size();
			// 循环统计所有指标每分钟的数据
			for (int i = 0; i < size; i++) {
				// 根据start end 切割的每分钟
				String min = list.get(i);

				// 输出每分钟当前在播人数（1）
				context.write(new Text(date + "@" + min), new Text(stbnum));
			}
		}
	}

	public static class ExtractCurrentNumReduce extends
			Reducer<Text, Text, Text, Text> {
		private Text result = new Text();
		// 定义当前在播数集合
		private Set<String> set_curnum = new HashSet<String>();

		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			set_curnum.clear();
			for (Text value : values) {
				set_curnum.add(value.toString());
			}
			// 计算出当前在播人数
			result.set(set_curnum.size()+"");
			context.write(key, result);
		}

	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "ExtractCurrentNum");

		// 设置输出key value分隔符
		job.getConfiguration().set(
				"mapreduce.output.textoutputformat.separator", "@");

		job.setJarByClass(ExtractCurrentNum.class);
		job.setMapperClass(ExtractCurrentNumMapper.class);
		job.setReducerClass(ExtractCurrentNumReduce.class);

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
				new ExtractCurrentNum(), args);
		System.exit(ec);
	}
}
