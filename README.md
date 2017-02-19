# Cryptology
AES &amp; RSA algorithm implementation

### AES Algorithm implementation

AES 算法在整体结构上采用的是 Square 结构而不是 Feistel 结构，该结构由4个不同的阶段组成，包括1个混乱和3个代换。 

1:字节代换(SubBytes)，用一个 S 盒完成分组中的按字节的代换。 

2:行移位代换(ShiftRows)，一个简单的置换。 

3:列混淆(MixColumns)，一个利用在域 GF(2^8)上的算术特征的代换。 

4:轮密钥加(AddRoundKey)，利用当前分组和扩展密钥的一部分进行按位异 或(XOR)。

在密钥方面，包括两个部分:密钥扩展和轮密钥选取.

密钥扩展:密钥 bit 的总数=分组长度×(轮数 Round+1)例如当分组长度为128bits和轮数Round为10时，轮密钥长度为128×(10+1)=1408bits。将密码密钥扩展成一个扩展密钥。 

轮密钥选取:第一个轮密钥由扩展密钥的第一个Nb个4字节字，第二个圈密钥由接下来的Nb个4字节字组成，以此类推。上述的实验步骤每个步骤的划分都明确，分模块实现这些功能即可，函数原型如下:

void SubBytes(unsigned char matrix[][4]);

void ShiftRows(unsigned char matrix[][4]);

void MixColumns(unsigned char matrix[][4]);

void AddRoundKey(unsigned char matrix[][4],int round); void KeyExpansion(unsigned char key[16]);

void InvSubBytes(unsigned char matrix[][4]); void InvShiftRows(unsigned char matrix[][4]); void InvMixColumns(unsigned char matrix[][4]);


### RSA Algorithm implementation

首先，考虑到产生的大数是 2048 位这种数量级的，任何的数值型都无法对 其进行表示，我们必须自己定义一个基于字符数组的结构体。在查阅了相关资料后发现，Java 的标准库中就有这种已经封装好的类型可以直接使用—BigInteger。 此外，还提供了 BigInteger 类型数据的加减乘除、大小比较、输出运算符、赋值、 循环移位等。
做好这些准备工作之后，读取用户输入的位数信息，根据位数信息产生两个 大素数符合 N = p*q。产生的过程具体如下:

####1. 素数搜索

随机产生一个奇数，对以该数为起点的奇数依次递增 2 进行测试，直至找到一个素数。代码部分流程如下:

####2. 素数筛选

*准备工作:*

费马小定理:对于素数 p 和任意整数 a，有 ap ≡ a(mod p)(同余)。反过 来，满足 ap ≡ a(mod p)，p也几乎一定是素数。

伪素数:如果n是一个正整数，如果存在和n互素的正整数a满足 an-1 ≡ 1(mod n)，我们说 n 是基于 a 的伪素数。如果一个数是伪素数，那么它几乎肯定是素数。
Miller-Rabin 测试:不断选取不超过 n-1 的基 b(s 次)，计算是否每次都 有 bn-1 ≡ 1(mod n)，若每次都成立则 n 是素数，否则为合数。在isPrime函数中，我们采用Miller Rabin法进行筛选。根据Miller Rabin法，通过r次测试之后，错误概率不超过 1/4 的 r 次幂。

伪代码如下: 
```
Function Miller-Rabin (n : longint) :boolean;
begin
  for i := 1 to s do
  begin
    a := random(n - 2) + 2;
    if mod_exp(a, n-1, n) <> 1 then return false;
  end;
  return true; 
end;
```
