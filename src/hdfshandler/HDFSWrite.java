package hdfshandler;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.net.URI;
import java.util.logging.Logger;

public class HDFSWrite {

    private static final Logger logger = Logger.getLogger("HDFSHandler");

    public static void main(String[] args) throws Exception {
        //        String hdfsuri = args[0];
        String hdfsUri = "hdfs://node-master:8020";

        String path = args[0];
        String fileName = args[1];
        String fileContent = args[2];

        // ====== Init HDFS File System Object
        Configuration conf = new Configuration();
        // Set FileSystem URI
        conf.set("fs.defaultFS", hdfsUri);
        // Because of Maven
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        // Set user
        System.setProperty("HADOOP_USER_NAME", "ptwrdhn2");
        System.setProperty("hadoop.home.dir", "/user/ptwrdhn2");
        //Get the filesystem - HDFS
        FileSystem fs = FileSystem.get(URI.create(hdfsUri), conf);

        //==== Create folder if not exists
        Path newFolderPath= new Path(path);
        if(!fs.exists(newFolderPath)) {
            // Create new Directory
            fs.mkdirs(newFolderPath);
            logger.info("Path "+path+" created.");
        }

        logger.info("Begin Write file into hdfs");
        //Create a path
        Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
        //Init output stream
        FSDataOutputStream outputStream=fs.create(hdfswritepath);
        //Classical output stream usage
        outputStream.writeBytes(fileContent);
        outputStream.close();
        logger.info("End Write file into hdfs");
    }

}
