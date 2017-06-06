package com.out;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.math.RandomAccessSparseVector;
 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
public class ClusterOutput {
    public static void main(String[] args) {
        try {
            
        	//Mahout������ļ�,��Ҫ������
        	String clusterOutputPath = args[0];
        	//������ľ������ļ�������������ش���
        	String resultPath = args[1];
        	
        	BufferedWriter bw;
            Configuration conf = new Configuration();
            conf.set("fs.default.name", "hdfs://192.168.190.200:9000");
            FileSystem fs = FileSystem.get(conf);
 
            SequenceFile.Reader reader = null;
            
            reader = new SequenceFile.Reader(fs, new Path(clusterOutputPath + "/clusteredPoints/part-m-00000"), conf);
 
            bw = new BufferedWriter(new FileWriter(new File(resultPath)));
            
            //keyΪ�۴�����id
            IntWritable key = new IntWritable();
            WeightedVectorWritable value = new WeightedVectorWritable();
            
            while (reader.next(key, value)) {
                //�õ�����
            	RandomAccessSparseVector vector = (RandomAccessSparseVector) value.getVector();
                
                String vectorvalue = "";
                
                //����������ά��ƴ�ӳ�һ�У���\t�ָ�
                for(int i = 0;i < vector.size();i++){
                	
                	if(i == vector.size() - 1){
                		vectorvalue += vector.get(i);
                	}else{
                		vectorvalue += vector.get(i) + "\t";
                	}
                	
                }
                
                //������ǰ���ϸ��������ڵľ۴�����id
                bw.write(key.toString() + "\t" + vectorvalue + "\n");
            }
            
            bw.flush();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 