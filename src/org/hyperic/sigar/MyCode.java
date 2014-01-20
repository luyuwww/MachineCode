package org.hyperic.sigar;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.cmd.CpuClass;
import org.hyperic.sigar.cmd.DfClass;
import org.hyperic.sigar.cmd.FreeClass;

public class MyCode {
	public static void main(String[] args) throws Exception {
		String computerInfo;
		File f = null;
		try {
			Sigar sigar = new Sigar();
			computerInfo = CpuClass.getCpuInfo(sigar);
			computerInfo += FreeClass.getFreeTotalSize(sigar);
			computerInfo += DfClass.getFirstDiskPartitionLength(sigar);
			computerInfo = getMD5String(computerInfo).toUpperCase();
			System.out.println(computerInfo);
			f = new File("./target/test-classes/_Machinecode_"+computerInfo+".txt");
			if(!f.exists()){
				f.createNewFile();
			}
			FileUtils.write(f, computerInfo);
		} catch (Exception e) {
			FileUtils.write(f , e.toString()+"获取机器特征码出错,请联系软件提供商!");
		}
	}
	
protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	protected static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MyCode.class.getName()+"初始化失败，MessageDigest不支持MD5!");
			nsaex.printStackTrace();
		}
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messageDigest.update(bytes);
		return bufferToHex(messageDigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	//转换成16进制
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
}
