package com.example.user.jiancan.personal.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.AppCompatCheckBox;
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
import com.example.user.jiancan.home.activityAndFragment.FoodListItemDetail;
import com.example.user.jiancan.personal.entity.Food;
import com.example.user.jiancan.personal.entity.HomeListItemBean;
import com.example.user.jiancan.personal.entity.Image;
import com.example.user.jiancan.personal.entity.TUser;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class OtherTrendsAdapter extends BaseAdapter {
    private List<Food> foods ;
    private int item_id;
    private TUser user;
    private Context context;
    ViewHolder viewHolder;

    public OtherTrendsAdapter(List<Food> foods, TUser user, int item_id, Context context) {
        this.foods = foods;
        this.user = user;
        this.item_id = item_id;
        this.context = context;
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
            viewHolder = new OtherTrendsAdapter.ViewHolder();
            viewHolder.tvContent = convertView.findViewById(R.id.tv_item_content);
            viewHolder.tvFoodName = convertView.findViewById(R.id.tv_item_name);
            viewHolder.llTrends = convertView.findViewById(R.id.ll_trends);
            viewHolder.imageView = convertView.findViewById(R.id.iv_trends_pic);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (OtherTrendsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.llTrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeListItemBean homeListItemBean = new HomeListItemBean();
                homeListItemBean.setUserId(user.getId());
                homeListItemBean.setUser(user.getNickname());
                homeListItemBean.setContent(foods.get(position).getContent());
                homeListItemBean.setImages(foods.get(position).getImages());
                homeListItemBean.setFoodId(foods.get(position).getId());
                homeListItemBean.setLikeNum(foods.get(position).getFabulous()+"");
                homeListItemBean.setName(foods.get(position).getFoodName());
                homeListItemBean.setShowImg("");
                homeListItemBean.setTitle(foods.get(position).getTitle());
                homeListItemBean.setType(foods.get(position).getType());
                homeListItemBean.setVideoUrl(foods.get(position).getVideo());
                Intent intent = new Intent();
                intent.setClass(context, FoodListItemDetail.class);
                intent.putExtra("食物数据",new Gson().toJson(homeListItemBean));
                context.startActivity(intent);

            }
        });
        viewHolder.tvFoodName.setText(foods.get(position).getFoodName());
        viewHolder.tvContent.setText(foods.get(position).getContent().substring(0, 10)+"...");
        Set<Image> image = foods.get(position).getImages();
        List <Image> list = new ArrayList<Image>(image);
//        Log.e("image",list.get(0).getImageName().toString());
        RequestOptions options = new RequestOptions().error(R.drawable.danta);
        if (list.size() == 0){
            Bitmap bitmap = createVideoThumbnail("http://39.97.3.111:8080/JianCanServerSystem/upload/videos/" + foods.get(position).getVideo());
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
            File f = new File("/sdcard/Showpictures/" + bitName+".jpeg");
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = f.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(String bitName) {
        try {
            File file = new File("/sdcard/Showpictures/" + bitName+".jpeg");
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

    }
}
