import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class CopyFromHDFS1 {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		Path glob = new Path("/user/mapr/java_test/inp2.txt");//source directory on HDFS 
		String localRoot = "/home/mapr/java_inp/"; // target local directory 
		
Configuration conf = new Configuration();
conf.addResource(new Path("/opt/mapr/hadoop/hadoop-2.4.1/etc/hadoop/core-site.xml"));// adding the  necessary configuration values 
conf.addResource(new Path("/opt/mapr/hadoop/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));// adding the necessary configuration values
FileSystem hdfs = FileSystem.get(conf);
		FileSystem localFs = FileSystem.get(new URI("file:///"), new Configuration());
		FileStatus [] files = hdfs.globStatus(glob);
		for (FileStatus file : files ){// copy each file 
		       copyToLocal(hdfs, localFs, file.getPath(),new Path(localRoot + file.getPath().getName()));
		}
	}

// Helper CopyToLocal function 
	private static void copyToLocal(FileSystem fromFs, FileSystem toFs, Path fromPath, Path toPath) throws IOException {
		System.out.println("Copying [" + fromPath + "] to [" + toPath + "]");
		OutputStream out = null;
		InputStream in = null;
		try {
			in = fromFs.open(fromPath);
			out = toFs.create(toPath);
			IOUtils.copyBytes(in, out, 10, false);
		} finally {
			IOUtils.closeStream(out);
			IOUtils.closeStream(in);
		}
	}
}
