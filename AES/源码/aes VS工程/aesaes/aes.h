//
//  AES.h
//  AES_encryption
//
//  Created by Grover Chen on 5/11/14.
//  Copyright (c) 2014 Grover Chen. All rights reserved.
//

#ifndef __AES_encryption__AES__
#define __AES_encryption__AES__

#include <iostream>
#include <string>
#include <fstream>
#include <math.h>
#include <stdio.h>
using namespace std;

class AES{
public:
	AES(unsigned char key[16]){
		KeyExpansion(key);
	}
	unsigned char* cipher(unsigned char* input);
	unsigned char* inv_cipher(unsigned char* input);
protected:
private:
	unsigned char static sBox[256];
	unsigned char static invBox[256];
	unsigned char RoundKey[240];
	void SubBytes(unsigned char matrix[][4]);
	void ShiftRows(unsigned char matrix[][4]);
	void MixColumns(unsigned char matrix[][4]);
	void AddRoundKey(unsigned char matrix[][4], int round);
	void KeyExpansion(unsigned char key[16]);
	void InvSubBytes(unsigned char matrix[][4]);
	void InvShiftRows(unsigned char matrix[][4]);
	void InvMixColumns(unsigned char matrix[][4]);
	unsigned char FFmul(unsigned char a, unsigned char b);
};

#endif /* defined(__AES_encryption__AES__) */
