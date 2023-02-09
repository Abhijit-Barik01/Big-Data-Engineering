package in.nokia;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text word,Iterable<IntWritable> counts,Context context) throws IOException, InterruptedException {
		// process values
		int total=0;
		for(IntWritable iw:counts)
		{
			total=total + iw.get();
		}
		
		context.write(word,new IntWritable(total));
	}

}
