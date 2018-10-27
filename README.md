# MediaPicker 
[![](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/hacknife/MediaPicker) [![](https://img.shields.io/badge/version-1.3.0--alpha1-brightgreen.svg)](https://github.com/hacknife/MediaPicker)<br/>
图片选择器满足你的一切需求，单选、多选、裁剪、拍照、九图、图片预览、单击放大、一应俱全，适配到Android8.0
<br/><br/>
![](https://github.com/hacknife/ImagePicker/blob/master/screenshot.gif)
## 代码示例
在使用MediaPicker前，必须在Activity中重写onActivityResult方法
```Java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaPicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }

```

其次实现MediaLoader，并设置到MediaPicker.getInstance().imageLoader(new GlideMediaLoader())
···
public class GlideMediaLoader implements MediaLoader {
    @Override
    public void displayFileImage(ImageView imageView, String path) {
         GlideApp.with(imageView.getContext())                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .into(imageView);
    }

    @Override
    public void displayUserImage(ImageView imageView, String path) {
         GlideApp.with(imageView.getContext()).load(path).into(imageView);
    }


    @Override
    public void displayFileVideo(String path) {

    }

    @Override
    public Class<?> displayFullImageClass() {
        return null;
    }
}
···

### 功能

#### 拍照
```Java
        MediaPicker.getInstance()
                .crop(false)//是否裁剪
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startPhotoPicker(this);
```


#### 拍照并裁剪
```Java
        MediaPicker.getInstance()
                .crop(true)//是否裁剪
                .outPutY(800)//裁剪大小
                .outPutX(800)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startPhotoPicker(this);
```


#### 选择单张图片并裁剪
```Java
        MediaPicker.getInstance()
                .multiMode(false)//是否多选
                .showCamera(true)//是否显示拍照Item
                .crop(false)//是否裁剪
                .outPutY(800)
                .outPutX(800)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startImagePicker(this);
```


#### 选择多图
```Java
        MediaPicker.getInstance()
                .multiMode(true)//是否多选
                .selectLimit(8)//图片限制
                .showCamera(true)
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startImagePicker(this, null);//第二个参数为已经选择的图片路径集合
```


#### 九图显示
```Java
@BindLayout(R.layout.activity_sample_nine)
public class SampleNineActivity extends AppCompatActivity {
    RecyclerView rc_view;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_nine);
        rc_view = findViewbyId(R.id.rc_view);
        imageAdapter = new ImageAdapter(this);
        imageAdapter.setRecyclerView(rc_view);
        imageAdapter.setImageSize(20, 0, 0);//九图间隔；距离左边；距离右边
        List<String> list = new ArrayList<>();
        list.add("http://dpic.tiankong.com/8n/a0/QJ6177688033.jpg");
        list.add("http://dpic.tiankong.com/6j/s5/QJ6146491299.jpg");
        list.add("http://dpic.tiankong.com/zt/pz/QJ6519443181.jpg");
        list.add("http://dpic.tiankong.com/br/ja/QJ6223394486.jpg");
        list.add("http://dpic.tiankong.com/i6/ke/QJ6322734186.jpg");
        list.add("http://dpic.tiankong.com/cl/at/QJ6769314375.jpg");
        list.add("http://dpic.tiankong.com/xp/3t/QJ6230352510.jpg");
        list.add("http://dpic.tiankong.com/37/ha/QJ6534800776.jpg");
        list.add("http://dpic.tiankong.com/5p/33/QJ6215180920.jpg");
        imageAdapter.bindData(list);
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        imageAdapter.onActivityReenter(resultCode, data);
    }
}

```


#### 图片预览(放大缩小)
```Java
        List<String> list = new ArrayList<>();
        list.add("http://dpic.tiankong.com/8n/a0/QJ6177688033.jpg");
        list.add("http://dpic.tiankong.com/6j/s5/QJ6146491299.jpg");
        list.add("http://dpic.tiankong.com/zt/pz/QJ6519443181.jpg");
        list.add("http://dpic.tiankong.com/br/ja/QJ6223394486.jpg");
        list.add("http://dpic.tiankong.com/i6/ke/QJ6322734186.jpg");
        list.add("http://dpic.tiankong.com/cl/at/QJ6769314375.jpg");
        list.add("http://dpic.tiankong.com/xp/3t/QJ6230352510.jpg");
        list.add("http://dpic.tiankong.com/37/ha/QJ6534800776.jpg");
        list.add("http://dpic.tiankong.com/5p/33/QJ6215180920.jpg");
        MediaPicker.getInstance().startImageViewer(this, list);
```


#### 视频选择
```Java
        MediaPicker.getInstance()
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startVideoPicker(this);
```
## 如何配置
将本仓库引入你的项目:
### Step 1. 添加仓库到Build文件
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://hacknife.com' }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
            ...
            implementation 'com.hacknife:mediapicker:1.3.0-alpha2'
	}
```
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/hacknife/CarouselBanner/blob/master/qq_group.png)
