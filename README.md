# MyAppFor7.0
7.0的摄像头的权限配置和6.0的动态权限设置
===================
我们都知道！只从6.0和7.0系统出来后！我们的android开发工程师有了更大的使命</br>
就是权限配置，6.0需要进行一个动态权限配置，而7.0需要私有文件的特殊路径访问</br>

废话不多说，下面adnroid 工程师的使命的具体操作
----------

####先说7.0系统的权限特殊的，就一照相机为例子来讲</br>
#####使用FileProvider
*第一步：在manifest清单文件中注册provider
```java
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="com.jph.takephoto.fileprovider"
    android:grantUriPermissions="true"
    android:exported="false">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```
  *心得：exported:要求必须为false，为true则会报安全异常。grantUriPermissions:true，表示授予 URI 临时访问权限。
*第二步：指定共享的目录
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <paths>
        <external-path path="" name="camera_photos" />
    </paths>
</resources>
  *<files-path/>代表的根目录： Context.getFilesDir()
  *<external-path/>代表的根目录: Environment.getExternalStorageDirectory()
  *<cache-path/>代表的根目录: getCacheDir()
  ```
*第三步：使用FileProvider
```java
File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
if (!file.getParentFile().exists())file.getParentFile().mkdirs();
Uri imageUri = FileProvider.getUriForFile(context, "com.jph.takephoto.fileprovider", file);//通过FileProvider创建一个content类型的Uri
Intent intent = new Intent();
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
startActivityForResult(intent,1006);
```
  *上面的就是使用fileProvider来进行操作的拍照，可以对比下7.0以前的，下面是7.0系统之前的拍照的源码
  ```java
File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
if (!file.getParentFile().exists())file.getParentFile().mkdirs();
Uri imageUri = Uri.fromFile(file);
Intent intent = new Intent();
intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
startActivityForResult(intent,1006);
```
######上述代码中主要有两处改变：
    *将之前Uri的scheme类型为file的Uri改成了有FileProvider创建一个content类型的Uri。
    *添加了intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);来对目标应用临时授权该Uri所代表的文件。
    
心得：上述代码通过FileProvider的Uri getUriForFile (Context context, String authority, File file)
静态方法来获取Uri，该方法中authority参数就是清单文件中注册provider的android:authorities="com.jph.takephoto.fileprovider"。
对Web服务器如tomcat，IIS比较熟悉的小伙伴，都只知道为了网站内容的安全和高效，Web服务器都支持为网站内容设置一个虚拟目录，其实FileProvider也有异曲同工之处



