package com.LightseaBlue.Dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Description: hdfsDao Date: 2020/2/7 Time 12:58 Author: LightseaBlue Version:
 * 1.0
 */
public class hdfsDao {
	private static Configuration conf;
	private static FileSystem fs;

	static {
		System.setProperty("Hadoop", "root");
		conf = new Configuration();
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件系统
	 * 
	 * @return
	 * @throws IOException
	 */
	public static FileSystem getFileSystem() throws IOException {
		return fs;
	}

	/**
	 * 改名
	 * 
	 * @param FilePath
	 * @param newFilePath
	 * @return
	 * @throws IOException
	 */
	public static boolean reName(String FilePath, String newFilePath) throws IOException {
		Path oldPath = new Path(FilePath);
		Path newPath = new Path(newFilePath);
		return fs.rename(oldPath, newPath);
	}

	/**
	 * 复制文件
	 * 
	 * @param FilePath
	 * @param newFilePath
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String FilePath, String newFilePath) throws IOException {
		Path oldPath = new Path(FilePath);
		String x = newFilePath + oldPath.getName();
		Path newPath = new Path(x);
		boolean copy = FileContext.getFileContext().util().copy(oldPath, newPath, false, true);// 四个属性为原路径，新路径，是否删除源文件，是否覆盖文件
		return copy;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(String filePath) throws IOException {
		Path p = new Path(filePath);
		return fs.delete(p, true);
	}

	/**
	 * 创建文件
	 * 
	 * @param newFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean creatFile(String newFileName) throws IOException {
		Path p = new Path(newFileName);
		return fs.mkdirs(p);
	}

	/**
	 * 移动
	 * 
	 * @param file
	 * @param path
	 * @throws IOException
	 */
	public static void fileUpload(String file, String path) throws IOException {
		Path src = new Path(file);
		Path p = new Path(path);
		fs.copyFromLocalFile(src, p);
	}

}
