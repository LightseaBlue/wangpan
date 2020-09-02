package com.LightseaBlue.Servlet;

import com.LightseaBlue.Bean.FileBean;
import com.LightseaBlue.Bean.UpListBean;
import com.LightseaBlue.Dao.hdfsDao;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import org.apache.hadoop.fs.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: mainServlet Date: 2020/2/7 
 * Time 12:37 
 * Author: LightseaBlue
 * Version: 1.0 
 */
@WebServlet("/main.action")
public class mainServlet extends BaseServlet {
	private static final long serialVersionUID = -1970760742274848883L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = req.getParameter("op");
		try {
			if ("showAll".equals(op)) {
				showAll(req, resp);
			} else if ("goUp".equals(op)) {
				goUp(req, resp);
			} else if ("reName".equals(op)) {
				reName(req, resp);
			} else if ("deleteFile".equals(op)) {
				deleteFile(req, resp);
			} else if ("creatFile".equals(op)) {
				creatFile(req, resp);
			} else if ("fileUpload".equals(op)) {
				fileUpload(req, resp);
			} else if ("moveFile".equals(op)) {
				moveFile(req, resp);
			} else if ("copyFile".equals(op)) {
				copyFile(req, resp);
			} else if ("selectFile".equals(op)) {
				selectFile(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件类型
	 *
	 * @param req
	 * @param resp
	 */
	private void selectFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String i = req.getParameter("i");
		Path p = new Path("/");
		FileSystem fs = hdfsDao.getFileSystem();
		RemoteIterator<LocatedFileStatus> files = fs.listFiles(p, true);
		List<FileBean> list = new ArrayList<>();
		while (files.hasNext()) {
			LocatedFileStatus f = files.next();
			if (f.isFile()) {
				FileBean bean = new FileBean();
				Path path = f.getPath();
				if ("1".equals(i)) {
					if (path.getName().lastIndexOf(".png") > 0 || path.getName().lastIndexOf(".PNG") > 0
							|| path.getName().lastIndexOf(".JPG") > 0 || path.getName().lastIndexOf(".jpg") > 0) {
						bean.setFileName(path.getName());
						bean.setFilePath(p.toString());
						long len = f.getLen();
						String lenString = fileLen(len);
						bean.setFileLen(lenString);
						long fileTime = f.getModificationTime();
						Date d = new Date(fileTime);
						DateFormat format = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
						String fileDate = format.format(d);
						bean.setFileTime(fileDate);

						list.add(bean);
					}
				} else if ("2".equals(i)) {
					if (path.getName().lastIndexOf(".DOC") > 0 || path.getName().lastIndexOf(".doc") > 0
							|| path.getName().lastIndexOf(".TXT") > 0 || path.getName().lastIndexOf(".txt") > 0) {
						bean.setFileName(path.getName());
						bean.setFilePath(p.toString());
						long len = f.getLen();
						String lenString = fileLen(len);
						bean.setFileLen(lenString);
						long fileTime = f.getModificationTime();
						Date d = new Date(fileTime);
						DateFormat format = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
						String fileDate = format.format(d);
						bean.setFileTime(fileDate);

						list.add(bean);
					}
				}
			}
		}
		tojson(resp, list);
	}

	/**
	 * 复制文件到
	 *
	 * @param req
	 * @param resp
	 */
	private void copyFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String filePath = req.getParameter("filePath");
		String newFilePath = req.getParameter("newFilePath");
//        System.out.println(filePath);

		if (!newFilePath.equals("/")) {
			newFilePath += "/";
		}
		boolean x = hdfsDao.copyFile(filePath, newFilePath);
//        System.out.println(x);
		tojson(resp, x);
	}

	/**
	 * 文件移动
	 *
	 * @param req
	 * @param resp
	 */
	private void moveFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String filePath = req.getParameter("filePath");
		String newFilePath = req.getParameter("newFilePath");
		if (!newFilePath.equals("/")) {
			newFilePath += "/";
		}
		boolean x = hdfsDao.reName(filePath, newFilePath);
		tojson(resp, x);
	}

	/**
	 * 文件上传
	 *
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 * @throws SmartUploadException
	 */
	private void fileUpload(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, SmartUploadException {
		Path p = null;
		try {
			SmartUpload su = new SmartUpload();
			su.initialize(getServletConfig(), req, resp);
			su.setCharset("utf-8");
			su.upload();
			File files = su.getFiles().getFile(0);
			files.saveAs("/" + files.getFileName());

			String path = su.getRequest().getParameter("hid");
			String x = getServletContext().getRealPath("/" + files.getFileName());

			if (x != null) {
				hdfsDao.fileUpload(x, path);
				String pathName = path + files.getFileName();
				p = new Path(pathName);
				java.io.File f = new java.io.File(path);
				if (f.exists()) {
					f.delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if (p != null) {
			resp.sendRedirect("index.jsp");
		} else {
			System.out.println("失败...");
		}

	}

	/**
	 * 新建目录
	 *
	 * @param req
	 * @param resp
	 */
	private void creatFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String newFileName = req.getParameter("newFileName");
		boolean x = hdfsDao.creatFile(newFileName);
		tojson(resp, x);
	}

	/**
	 * 删除文件
	 *
	 * @param req
	 * @param resp
	 */
	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String filePath = req.getParameter("filePath");
		boolean x = hdfsDao.deleteFile(filePath);
		tojson(resp, x);
	}

	/**
	 * 重命名
	 *
	 * @param req
	 * @param resp
	 */
	private void reName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String newFilePath = req.getParameter("newFilePath");
		String filePath = req.getParameter("filePath");
		boolean x = hdfsDao.reName(filePath, newFilePath);
		tojson(resp, x);
	}

	/**
	 * 返回父路径
	 *
	 * @param req
	 * @param resp
	 */
	private void goUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String thisPath = req.getParameter("path");
		Path p = new Path(thisPath);
		String stringPath;
		if (!p.toString().equals("/")) {
			stringPath = p.getParent().toString();
		} else {
			stringPath = "/";
		}
		List<FileBean> list = getFile(stringPath);
		UpListBean bean = new UpListBean();
		bean.setList(list);
		bean.setParentPath(stringPath);
		tojson(resp, bean);
	}

	/**
	 * 首页展示
	 *
	 * @param req
	 * @param resp
	 */
	private void showAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String stringPath = req.getParameter("path");
		List<FileBean> list = getFile(stringPath);
		tojson(resp, list);
	}

	/**
	 * 通过路径获取文件目录
	 *
	 * @return
	 * @throws IOException
	 */
	private List<FileBean> getFile(String stringPath) throws IOException {
		Path p = new Path(stringPath);
		FileSystem hdfs = hdfsDao.getFileSystem();
		FileStatus[] file = hdfs.listStatus(p);
		List<FileBean> list = new ArrayList<>();
		for (FileStatus f : file) {
			FileBean bean = new FileBean();
			// 文件真实路径
			String FilePath = f.getPath().toString();
			bean.setFilePath(FilePath);
			// 文件长度
			if (f.isFile()) {
				long len = f.getLen();
				String fileLen = fileLen(len);
				bean.setFileLen(fileLen);
			}
			// 文件名
			Path path = f.getPath();
			bean.setFileName(path.getName());
			// 文件创建时间
			long fileTime = f.getModificationTime();
			Date d = new Date(fileTime);
			DateFormat format = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
			String fileDate = format.format(d);
			bean.setFileTime(fileDate);
			list.add(bean);
		}
		return list;
	}

	private String fileLen(long len) {
		String fileLen = null;
		if (len / 1024 != 0) {
			fileLen = len / 1024 + "kb";
		} else if (len / 1024 / 1024 != 0) {
			fileLen = len / 1024 / 1024 + "mb";
		} else if (len / 1024 / 1024 / 1024 != 0) {
			fileLen = len / 1024 / 1024 / 1024 + "gb";
		} else if (len / 1024 / 1024 / 1024 / 1024 != 0) {
			fileLen = len / 1024 / 1024 / 1024 / 1024 + "tb";
		} else {
			fileLen = len + "b";
		}
		return fileLen;
	}
}
