import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {
	private final static BigInteger one = new BigInteger("1"); // 定义大整数1
	private final static SecureRandom random = new SecureRandom(); // 定义安全随机数

	private static BigInteger privateKey; // 私钥
	private static BigInteger publicKey; // 公钥
	private static BigInteger modulus; // 模值

	// 构造函数根据p、q生成模值，根据公钥生成私钥
	RSA(BigInteger p, BigInteger q, BigInteger publicKey) {
		BigInteger phi = (p.subtract(one)).multiply(q.subtract(one)); // 求φ(N)
		modulus = p.multiply(q); // 求模值
		System.out.println("N:" + modulus);
		privateKey = publicKey.modInverse(phi); // 求私钥
		System.out.println("私钥d:" + privateKey);
	}

	// 加密
	public static BigInteger encrypt(BigInteger message) {
		return message.modPow(publicKey, modulus); // 加密算法
	}

	// 解密
	public static BigInteger decrypt(BigInteger encrypted) {
		return encrypted.modPow(privateKey, modulus); // 解密算法
	}

	// 写文件
	public static void writerTxt(String filename, String content, Boolean append) {
		BufferedWriter fw = null;
		try {
			File file = new File(filename);
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), "UTF-8")); // 指定写入文件和编码格式
			fw.append(content); // 附加文本
			fw.newLine();
			fw.flush(); // 缓存中的内容写入文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 读文件
	public static String readTxt(String filename) {
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), "UTF-8")); // 指定读取文件和编码格式
			while ((str = reader.readLine()) != null) { // 逐行读取
				return str; // 返回字符串
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("输入密钥规模（位数）：");
		int N = in.nextInt(); // 读入密钥规模
		BigInteger p = BigInteger.probablePrime(N / 2, random); // 生成大素数p
		BigInteger q = BigInteger.probablePrime(N / 2, random); // 生成大素数q
		System.out.println("p:" + p);
		System.out.println("q:" + q);
		System.out.println("输入公钥e：");
		publicKey = in.nextBigInteger(); // 读入公钥
		RSA key = new RSA(p, q, publicKey); // 构造模值和私钥
		BigInteger m, c;
		String out;
		while (true) {
			System.out.println("1.加密文件；2。解密文件；3.退出");
			switch (in.nextInt()) {
			case 1:
				System.out.println("输入明文文件：");
				m = new BigInteger(readTxt(in.next())); // 读出明文
				System.out.println("明文：" + m.toString());
				out = encrypt(m).toString(); // 加密明文
				System.out.println("加密后：" + out);
				writerTxt("./result.txt", "明文：" + m.toString() + ' ' + "加密后："
						+ out, true); // 写入文件
				writerTxt("verify.txt", out, false);
				break;
			case 2:
				System.out.println("输入密文文件：");
				c = new BigInteger(readTxt(in.next())); // 读出密文
				System.out.println("密文：" + c.toString());
				out = decrypt(c).toString(); // 解密密文
				System.out.println("解密后：" + out);
				writerTxt("./result.txt", "密文：" + c.toString() + ' ' + "解密后："
						+ out, true); // 写入文件
				writerTxt("verify.txt", out, false);
				break;
			case 3:
				in.close();
				System.exit(0);
			default:
				System.out.println("错误命令！");
				break;
			}
		}
	}
}