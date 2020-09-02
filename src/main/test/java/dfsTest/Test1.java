package dfsTest;

import com.LightseaBlue.Bean.FileBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Description: Test1
 * Date: 2020/2/7
 * Time 12:27
 * Author: LightseaBlue
 * Version: 1.0 <br>
 */
public class Test1 {
    private FileSystem fs;
    @Before
    public void getFilesystem() throws IOException {
        Configuration conf=new Configuration();
        fs=FileSystem.get(conf);
    }

    @Test
    public void mkdirs() throws IOException {
        Path p=new Path("/Test");
        boolean x=fs.mkdirs(p);
        System.out.println(x);
    }

    @Test
    public void FileList() throws IOException {
        Path p=new Path("hdfs://LightseaBlue/Test");
        FileStatus[] f=fs.listStatus(p);
        for(FileStatus file:f){
            String x=file.getPath().toString();
            FileBean bean=new FileBean();
            bean.setFilePath(x);
            System.out.println(x);
        }
    }

    @Test
    public void putTest() throws IOException {
        Path src=new Path("C:\\Users\\fox11\\Desktop\\car.sql");
        System.out.println(src.getName());
        Path p=new Path("/");
        fs.copyFromLocalFile(src,p);

    }

    @Test
    public void fileTest() throws IOException {
        Path oldPath=new Path("/23.txt");
        Path newPath=new Path("/af/23.txt");
        boolean x= FileContext.getFileContext().util().copy(oldPath,newPath,false,false);
        System.out.println(x);
    }
















}
