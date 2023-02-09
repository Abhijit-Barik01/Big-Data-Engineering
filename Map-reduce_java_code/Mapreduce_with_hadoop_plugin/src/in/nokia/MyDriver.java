package in.nokia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(in.nokia.MyDriver.class);
		job.setMapperClass(in.nokia.MyMapper.class);

		job.setReducerClass(in.nokia.MyReducer.class);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path("hdfs://localhost:9000/user/hduser/wordcount_input"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/user/hduser/wordcount_output"));

		if (!job.waitForCompletion(true))
			return;
	}

}
