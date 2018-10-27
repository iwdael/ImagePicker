# MediaPicker 
[![](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/hacknife/MediaPicker) [![](https://img.shields.io/badge/version-1.3.0--alpha1-brightgreen.svg)](https://github.com/hacknife/MediaPicker)<br/>
图片选择器满足你的一切需求，单选、多选、裁剪、拍照、九图、图片预览、单击放大、一应俱全，适配到Android8.0
<br/><br/>
![](https://github.com/hacknife/ImagePicker/blob/master/screenshot.gif)
## 代码示例
在使用MediaPicker前，必须在Activity中重写onActivityResult方法
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaPicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }

```
<br/>
拍照
```
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
<br/>
拍照并裁剪
```
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
<br/>
选择单张图片并裁剪
```
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
<br/>
选择多图
```
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
<br/>
九图显示
```
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
<br/>
图片预览(放大缩小)
```
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
<br/>
视频选择
```
        MediaPicker.getInstance()
                .selectedListener(new MediaPicker.OnSelectedListener() {
                    @Override
                    public void onImageSelected(List<ImageItem> items) {
                        toast(items.toString());
                    }
                })
                .startVideoPicker(this);
```