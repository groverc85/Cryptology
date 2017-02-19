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
	private final static BigInteger one = new BigInteger("1"); // ���������1
	private final static SecureRandom random = new SecureRandom(); // ���尲ȫ�����

	private static BigInteger privateKey; // ˽Կ
	private static BigInteger publicKey; // ��Կ
	private static BigInteger modulus; // ģֵ

	// ���캯������p��q����ģֵ�����ݹ�Կ����˽Կ
	RSA(BigInteger p, BigInteger q, BigInteger publicKey) {
		BigInteger phi = (p.subtract(one)).multiply(q.subtract(one)); // ���(N)
		modulus = p.multiply(q); // ��ģֵ
		System.out.println("N:" + modulus);
		privateKey = publicKey.modInverse(phi); // ��˽Կ
		System.out.println("˽Կd:" + privateKey);
	}

	// ����
	public static BigInteger encrypt(BigInteger message) {
		return message.modPow(publicKey, modulus); // �����㷨
	}

	// ����
	public static BigInteger decrypt(BigInteger encrypted) {
		return encrypted.modPow(privateKey, modulus); // �����㷨
	}

	// д�ļ�
	public static void writerTxt(String filename, String content, Boolean append) {
		BufferedWriter fw = null;
		try {
			File file = new File(filename);
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), "UTF-8")); // ָ��д���ļ��ͱ����ʽ
			fw.append(content); // �����ı�
			fw.newLine();
			fw.flush(); // �����е�����д���ļ�
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

	// ���ļ�
	public static String readTxt(String filename) {
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), "UTF-8")); // ָ����ȡ�ļ��ͱ����ʽ
			while ((str = reader.readLine()) != null) { // ���ж�ȡ
				return str; // �����ַ���
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
		System.out.println("������Կ��ģ��λ������");
		int N = in.nextInt(); // ������Կ��ģ
		BigInteger p = BigInteger.probablePrime(N / 2, random); // ���ɴ�����p
		BigInteger q = BigInteger.probablePrime(N / 2, random); // ���ɴ�����q
		System.out.println("p:" + p);
		System.out.println("q:" + q);
		System.out.println("���빫Կe��");
		publicKey = in.nextBigInteger(); // ���빫Կ
		RSA key = new RSA(p, q, publicKey); // ����ģֵ��˽Կ
		BigInteger m, c;
		String out;
		while (true) {
			System.out.println("1.�����ļ���2�������ļ���3.�˳�");
			switch (in.nextInt()) {
			case 1:
				System.out.println("���������ļ���");
				m = new BigInteger(readTxt(in.next())); // ��������
				System.out.println("���ģ�" + m.toString());
				out = encrypt(m).toString(); // ��������
				System.out.println("���ܺ�" + out);
				writerTxt("./result.txt", "���ģ�" + m.toString() + ' ' + "���ܺ�"
						+ out, true); // д���ļ�
				writerTxt("verify.txt", out, false);
				break;
			case 2:
				System.out.println("���������ļ���");
				c = new BigInteger(readTxt(in.next())); // ��������
				System.out.println("���ģ�" + c.toString());
				out = decrypt(c).toString(); // ��������
				System.out.println("���ܺ�" + out);
				writerTxt("./result.txt", "���ģ�" + c.toString() + ' ' + "���ܺ�"
						+ out, true); // д���ļ�
				writerTxt("verify.txt", out, false);
				break;
			case 3:
				in.close();
				System.exit(0);
			default:
				System.out.println("�������");
				break;
			}
		}
	}
}