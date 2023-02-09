package in.nokia;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		
		String line =value.toString();
		System.out.println(key);
		System.out.println(line);
		
		
		String words[]=line.split(" ");
		for(String word:words)
		{
			context.write(new Text(word),new IntWritable(1));
		}
				
		
	}

}
