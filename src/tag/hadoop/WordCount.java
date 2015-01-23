/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tag.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class WordCount {
    
    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final Text userAndDate = new Text();
        private String user;

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split("\t");
            String date = line[2].split(" ")[0];
            if(line[1].equals(user)) {
                userAndDate.set(line[1] + ":" + date);
                IntWritable time = new IntWritable(Integer.parseInt(line[5]));
                context.write(userAndDate, time);
            }
        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            user = context.getConfiguration().get("user");
            super.setup(context);
        }
        
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
    
    public static class CollectorMapper
            extends Mapper<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }
        
    }

    public static void main(String[] args) throws Exception {
        
        Configuration conf = new Configuration();
        conf.set("user", args[2]);
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setNumReduceTasks(4);
        
        Configuration conf2 = new Configuration();
        Job job2 = Job.getInstance(conf2, "collector");
        job2.setJarByClass(WordCount.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        FileInputFormat.addInputPath(job2, new Path(args[1]));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]+"_output"));
        
        job.waitForCompletion(true);
        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}
