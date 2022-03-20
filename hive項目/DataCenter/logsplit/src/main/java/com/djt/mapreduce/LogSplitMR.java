package com.djt.mapreduce;
import com.djt.pojo.TV;
import com.djt.pojo.UserLog;
import com.djt.utils.TVUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 解析、分割原始数据MR
 */
public class LogSplitMR extends Configured implements Tool {

	public static class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		private Text keyText = new Text();
		private String line = "";
		@Override
		protected void map(LongWritable key, Text value, Context context)	throws IOException, InterruptedException {
			try {
				//System.out.println("key=====================" + key.toString());
				//System.out.println("value=====================" + value.toString());
				line = value.toString();
				TV tv = TVUtil.parseTVData(line);

				ArrayList<UserLog> list = tv.getList();
				String cardNum = tv.getCardNum();

				String stbNum = tv.getStbNum();

				String date = tv.getDate();

				String pageWidgetVersion = tv.getPageWidgetVersion();
				for(UserLog userLog:list){
					String sn = userLog.getSn();

					String p = userLog.getP();

					String n = userLog.getN();

					String t = userLog.getT();

					String pi = userLog.getPi();

					String s = userLog.getS();

					String e = userLog.getE();

					StringBuilder sb = new StringBuilder();
					sb.append(cardNum+"@");
					sb.append(stbNum+"@");
					sb.append(date+"@");
					sb.append(pageWidgetVersion+"@");
					sb.append(e+"@");
					sb.append(s+"@");
					sb.append(n+"@");
					sb.append(t+"@");
					sb.append(pi+"@");
					sb.append(p+"@");
					sb.append(sn);
					this.keyText.set(sb.toString());
					//System.out.println(sb.toString());
					context.write(keyText,NullWritable.get());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			ToolRunner.run(new Configuration(), new LogSplitMR(), args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job job = Job.getInstance(conf, "LogSplitMR");

		//String inputPathStr ="D:\\study\\data\\2020\\ars10767@20120917016005.txt";
		String inputPathStr =job.getConfiguration().get("inputPath");
		//String outputPathStr = "D:\\study\\data\\2020\\out";
		String outputPathStr = job.getConfiguration().get("outputPath");

		FileInputFormat.setInputPaths(job, inputPathStr);
		FileOutputFormat.setOutputPath(job, new Path(outputPathStr));

		//本地测试
		job.setInputFormatClass(TextInputFormat.class);
		//集群运行
		//job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setJarByClass(LogSplitMR.class);
		return job.waitForCompletion(true) ? 0 : 1;
	}
}
