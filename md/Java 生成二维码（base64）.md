# Java 生成二维码（及base64转换）

本文主要是讲解java生成二维码且以base64的形式返回， 生成二维码的功能主要是依赖Google的zxing包 。

## 导入zxing依赖

以maven为例，在pom文件中导入依赖

```java
<dependency>   <groupId>com.google.zxing</groupId>   <artifactId>core</artifactId>   <version>3.3.0</version></dependency><dependency>   <groupId>com.google.zxing</groupId>   <artifactId>javase</artifactId>   <version>3.3.0</version></dependency>
```

## 实现代码

```java
public static void main(String[] args) throws WriterException, IOException {
   //需要编码的内容
    String urlCode="my QR Code"; 
    QRCodeWriter qrCodeWriter1 = new QRCodeWriter();  
    //设置二维码图片宽高  
    BitMatrix bitMatrix1 = qrCodeWriter1.encode(urlCode, BarcodeFormat.QR_CODE,600, 600); 
    //输出到指定路径   
    Path path = FileSystems.getDefault().getPath("C:/Users/DELL/Desktop/MyQRCode.png");   	MatrixToImageWriter.writeToPath(bitMatrix1,"PNG",path);   
    // 写到输出流   
    ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();                    MatrixToImageWriter.writeToStream(bitMatrix1, "PNG", outputStream1);
    //转换为base64   
    Base64.Encoder encoder1 = Base64.getEncoder();   
    String advUrl = "data:image/jpeg;base64,"
        +encoder1.encodeToString(outputStream1.toByteArray());   	 		          
    //打印base64结果
    System.out.println(advUrl);
}
```

## 结果展示

### 二维码图片生成

在对应路径上（"C:/Users/DELL/Desktop/MyQRCode.png"）上会生成一张二维码图片

![1595493847047](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\1595493847047.png)

### 扫码结果

用微信扫码打开得到我们输入的编码内容（“my QR Code”）

![1595493998953](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\1595493998953.png)

### base64转换输出

控制台输出转换后的base64编码

![1595494023655](C:\Users\DELL\AppData\Roaming\Typora\typora-user-images\1595494023655.png)

​	