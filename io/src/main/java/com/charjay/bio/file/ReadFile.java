package com.charjay.bio.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * BIO读取数据，从一个阻塞的流中，一行一行的读取
 * io.txt的内容：随便输入4行
 */
public class ReadFile {
	public static void main(String[] args) {
		try{
			FileInputStream input = new FileInputStream("e://io.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String nameLine = reader.readLine();
			String ageLine = reader.readLine();
			String emailLine = reader.readLine();
			String phoneLine = reader.readLine();
			String lastLine = reader.readLine();
			System.out.println(nameLine);
			System.out.println(ageLine);
			System.out.println(emailLine);
			System.out.println(phoneLine);
			System.out.println(lastLine);
			
			input.close();
			reader.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
