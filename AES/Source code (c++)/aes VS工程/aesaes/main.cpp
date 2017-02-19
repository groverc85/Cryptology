//
//  main.cpp
//  AES_encryption
//
//  Created by Grover Chen on 4/11/14.
//  Copyright (c) 2014 Grover Chen. All rights reserved.
//

#include "aes.h"

//������ʮ�������ַ��ϲ�Ϊһ��
void change(unsigned char *temp, unsigned char *p){
	if (temp[0] <= '9'&&temp[0] >= '0') {
		*p += (temp[0] - '0') * 16;
	}
	else if (temp[0] <= 'f'&&temp[0] >= 'a'){
		*p += (temp[0] - 'a' + 10) * 16;
	}
	else{
		*p += (temp[0] - 'A' + 10) * 16;
	}

	if (temp[1] <= '9'&&temp[1] >= '0') {
		*p += (temp[1] - '0');
	}
	else if (temp[1] <= 'f'&&temp[1] >= 'a'){
		*p += (temp[1] - 'a' + 10);
	}
	else{
		*p += (temp[1] - 'A' + 10);
	}
}


int main(void)
{
	unsigned char key[128];
	FILE *fp;
	string keyname, readname, writename;

	cout << "��������Կ�ļ�λ�ü��ļ���" << endl;
	cin >> keyname;

	if ((fp = fopen(keyname.c_str(), "r")) == NULL)
		//    if((fp=fopen("key.txt","r"))==NULL)
	{
		cout << "�޷���" << endl;
		return 0;
	}
	fread(key, 128, 1, fp);
	fclose(fp);

	AES aes(key);

	int mode;
	cout << "���ܲ�������1  ���ܲ�������2" << endl;
	cin >> mode;
	if (mode == 1)
	{
		cout << "����������λ�ü��ļ���" << endl;
		cin >> readname;
		FILE *fp1;
		if ((fp1 = fopen(readname.c_str(), "r")) == NULL)
			//        if((fp1=fopen("input.txt","r"))==NULL)
		{
			cout << "�޷���" << endl;
			return 0;
		}
		cout << "�����������ĵ�λ�ü��ļ���" << endl;
		cin >> writename;
		FILE *fp2;
		if ((fp2 = fopen(writename.c_str(), "w+")) == NULL)
			//        if ((fp2 = fopen("output.txt", "w+")) == NULL)
		{
			cout << "�޷���" << endl;
			return 0;
		}

		//�����ļ��ֽ���
		long size = 0;
		char ch = fgetc(fp1);
		while (ch != EOF) {
			ch = fgetc(fp1);
			size++;
		}
		fseek(fp1, 0, 0);

		//��16�ֽ�Ϊ��λ���ļ�
		unsigned char data[16];
		for (int j = 0; j<ceil((size / 16.0)); j++)
		{
			fread(data, 16, 1, fp);
			if ((j == ceil((size / 16.0)) - 1) && size % 16 != 0)  //�����16λʱҪ��0
			{
				int temp = size % 16;
				for (int k = temp; k < 16; k++)
					data[k] = '\0';
			}
			aes.cipher(data);
			for (int i = 0; i < 16; i++) {
				if (data[i] >= 0 && data[i] <= 15) {
					fprintf(fp2, "0");
					fprintf(fp2, "%x", data[i]);
				}
				else
					fprintf(fp2, "%x", data[i]);
			}
		}
		fclose(fp1);
		fclose(fp2);
	}
	else if (mode == 2)
	{
		cout << "����������λ�ü��ļ���" << endl;
		cin >> readname;
		FILE *fp1;
		if ((fp1 = fopen(readname.c_str(), "r")) == NULL) {
			//        if ((fp1 = fopen("input.txt", "r")) == NULL) {
			cout << "���ܴ��ļ�";
			return 0;
		}
		string writename;
		cout << "�����������ĵ�λ�ü��ļ���" << endl;
		cin >> writename;
		FILE *fp2;
		if ((fp2 = fopen(writename.c_str(), "w+")) == NULL) {
			//        if ((fp2 = fopen("output.txt", "w+")) == NULL) {
			cout << "�޷���";
			return 0;
		}
		long size = 0;
		char ch = fgetc(fp1);
		while (ch != EOF) {
			ch = fgetc(fp1);
			size++;
		}
		fseek(fp1, 0, 0);
		//��16�ֽ�Ϊ��λ���ļ�
		unsigned char data[16];
		unsigned char temp[2];
		for (int j = 0; j < ceil((size / 32.0)); j++) {
			for (int i = 0; i < 16; i++) {
				fseek(fp1, 2 * i, 0);
				fread(temp, 2, 1, fp1);
				change(temp, data + i);
			}

			aes.inv_cipher(data);
			fwrite(data, 16, 1, fp2);
		}
		fclose(fp1);
		fclose(fp2);
	}
	fclose(fp);
	return 0;
}