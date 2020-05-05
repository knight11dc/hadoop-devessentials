import java.io.IOException;
import java.util.*;	
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class LengthDivider {

public static class LengthDividerCountMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	public final static IntWritable countOne = new IntWritable(1);
	private final Text fiveCharsOrMore = new Text("greaterOrEqualsToFiveChars");
	private final Text lessThanFiveChars = new Text("lessThanFiveChars");
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		StringTokenizer tokenizer = new StringTokenizer(value.toString());
		while (tokenizer.hasMoreTokens()) {
			if (tokenizer.nextToken().length() >= 5){
				output.collect(fiveCharsOrMore, countOne);
			} else {
				output.collect(lessThanFiveChars, countOne);
			}
		}
	}
}


public static  class LengthDividerCountReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		int sum = 0;
		while (values.hasNext()) {
			sum += values.next().get();
		}
		output.collect(key, new IntWritable(sum));
	}
}
public static void main(String[] args) throws Exception {
JobConf conf = new JobConf(LengthDivider.class);
conf.setJobName("length-divider");
conf.setOutputKeyClass(Text.class);
conf.setOutputValueClass(IntWritable.class);
conf.setMapperClass(LengthDividerCountMapper.class);
conf.setCombinerClass(LengthDividerCountReducer.class);
conf.setReducerClass(LengthDividerCountReducer.class);
	conf.setInputFormat(TextInputFormat.class);
conf.setOutputFormat(TextOutputFormat.class);
FileInputFormat.setInputPaths(conf,new Path("maprfs:///user/mapr/input"));
FileOutputFormat.setOutputPath(conf, new Path("maprfs:///user/mapr/out_line"));
	JobClient.runJob(conf);
	
	}
}
