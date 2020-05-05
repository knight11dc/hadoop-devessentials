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
import org.apache.hadoop.util.Progressable;

public class CopyToHDFS {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
Configuration conf = new Configuration();
conf.addResource(new Path("/opt/mapr/hadoop/hadoop-2.4.1/etc/hadoop/core-site.xml"));// adding the  necessary configuration values 

conf.addResource(new Path("/opt/mapr/hadoop/hadoop-2.4.1/etc/hadoop/hdfs-site.xml"));// adding the  necessary configuration values 
	
FileSystem hdfs = FileSystem.get(conf);

		FileSystem local = FileSystem.get(new URI("file:///"), new Configuration());
	Path localGlob = new Path("/home/mapr/java_inp/inp.txt");// source directory 
		Path hdfsRoot = new Path("/user/mapr/java_test");//Target HDFS directoy 
		hdfs.mkdirs(hdfsRoot);
		
		FileStatus [] files = local.globStatus(localGlob);
		for (FileStatus file : files ){
			Path from = file.getPath();
			Path to = new Path(hdfsRoot, file.getPath().getName());
			copy(local, from, hdfs, to);//copying from source to target 
		}
	}
// The “copy” helper function 
	private static void copy(FileSystem fromFs, Path fromPath, FileSystem toFs, Path toPath) throws IOException {
		System.out.println("Copying [" + fromPath + "] to [" + toPath + "]");
		OutputStream out = null;
		InputStream in = null;
		try {
			in = fromFs.open(fromPath);
			out = toFs.create(toPath, new Progressable() {
			    @Override
			    public void progress() {
			        System.out.print("..");
			    }
			});
			IOUtils.copyBytes(in, out, 10, false);
		} finally {
			IOUtils.closeStream(out);
			IOUtils.closeStream(in);
		}
		System.out.print("\n");
	}
}
