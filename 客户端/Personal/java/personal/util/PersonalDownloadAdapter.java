package com.example.user.jiancan.personal.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.Constant;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.activityAndFragement.DownloadFileShow;
import com.example.user.jiancan.home.activityAndFragement.FoodItemListDetail;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.HomeListItemBean;
import com.example.user.jiancan.personal.entity.Image;
import com.example.user.jiancan.personal.entity.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class PersonalDownloadAdapter extends BaseAdapter {
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";
    private List<Food> foods ;
    private int item_id;
    private User user;
    private Context context;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态。
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    ViewHolder viewHolder;

    public PersonalDownloadAdapter(List<Food> foods, User user, int item_id, Context context, SparseBooleanArray stateCheckedMap) {
        this.foods = foods;
        this.user = user;
        this.item_id = item_id;
        this.context = context;
        this.stateCheckedMap = stateCheckedMap;
    }
    @Override
    public int getCount() {
        if (foods.size() != 0){
            return foods.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (foods.size()!=0){
            return foods.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvFoodName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llTrends = convertView.findViewById(R.id.ll_trends);
            viewHolder.imageView = convertView.findViewById(R.id.iv_trends_pic);
            viewHolder.checkBox = convertView.findViewById(R.id.cb_select_point);
            viewHolder.tvContent = convertView.findViewById(R.id.tv_item_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,DownloadFileShow.class);
                intent.putExtra("foodId",foods.get(position).getId());
                intent.putExtra("foodType",foods.get(position).getType());
                context.startActivity(intent);

            }
        });
        viewHolder.tvFoodName.setText(foods.get(position).getFoodName());
        viewHolder.tvContent.setText(foods.get(position).getContent().substring(0, 10)+"...");
        showAndHideCheckBox();//控制CheckBox的那个的框显示与隐藏
        viewHolder.checkBox.setChecked(stateCheckedMap.get(position));//设置CheckBox是否选中
        Set<Image> image = foods.get(position).getImages();
        List <Image> list = new ArrayList<Image>(image);
//        Log.e("image",list.get(0).getImageName().toString());
        RequestOptions options = new RequestOptions().error(R.drawable.danta);
        if (list.size() == 0){
            Bitmap bitmap = createVideoThumbnail("http://39 .97.3.111: 8080/JianCanServerSystem/ upload/videos/" + foods.get(position).getVideo());
            saveBitmapToLocal(foods.get(position).getId()+"",bitmap);
            viewHolder.imageView.setImageBitmap(getBitmapFromLocal(foods.get(position).getId()+""));
        }else{
            Glide.with(context)
                    .load(Constant.BASE_URL + "/upload/images/" + list.get(0).getImageName().toString())
                    .apply(options)
                    .into(viewHolder.imageView);
        }


        return convertView;
    }

    /**
     * 向本地SD卡写网络图片
     *
     * @param bitmap
     */
    public static void saveBitmapToLocal(String bitName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(FILE_PATH, "/sdcard/vedioimgs"+bitName+".jpeg");
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(String bitName) {
        try {
            File file = new File(FILE_PATH, "/sdcard/vedioimgs"+bitName+".jpeg");
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取视频某一帧图片作为预览图
     */
    public static Bitmap createVideoThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://"))
            {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            }
            else
            {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(1*1000*500, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        }
        catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
        {
            return null;
        }
        return bitmap;
    }

    public class ViewHolder{
        public TextView tvFoodName;
        public LinearLayout llTrends;
        public ImageView imageView;
        public TextView tvContent;
        public AppCompatCheckBox checkBox;
    }
    private void showAndHideCheckBox() {
        if (isShowCheckBox) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
    }


    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }

}
