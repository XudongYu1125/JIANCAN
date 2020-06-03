package com.example.user.jiancan.home.activityAndFragment;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.jiancan.R;
import com.example.user.jiancan.home.entity.CommentDetailBean;
import com.example.user.jiancan.home.entity.ReplyDetailBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.jiancan.home.activityAndFragment.FoodListItemDetail.setHintInVisible;
import static com.example.user.jiancan.home.activityAndFragment.FoodListItemDetail.setHintVisible;

/**
 * @author june
 */
public class CommentExpandAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "CommentExpandAdapter";
    private List <CommentDetailBean> commentBeanList;
    private Context context;
    private String otherUserImgUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1141259048,554497535&fm=26&gp=0.jpg";
    private boolean isLike = false;
    private View.OnClickListener iv_expandClickListener;

    public CommentExpandAdapter(Context context , List <CommentDetailBean> commentBeanList , View.OnClickListener iv_expandClickListener) {
        this.context = context;
        this.commentBeanList = commentBeanList;
        this.iv_expandClickListener = iv_expandClickListener;
    }

    @Override
    public int getGroupCount() {
        if (null == commentBeanList || commentBeanList.size () == 0){
            return 0;
        }else {
            return commentBeanList.size ( );
        }
    }

    @Override
    public int getChildrenCount( int groupPosition ) {
        if (commentBeanList.get (groupPosition).getReplyList ( ) == null) {
            return 0;
        } else {
            return Math.max (commentBeanList.get (groupPosition).getReplyList ( ).size ( ) , 0);
        }
    }

    @Override
    public Object getGroup( int groupPosition ) {
        return commentBeanList.get (groupPosition);
    }

    @Override
    public Object getChild( int groupPosition , int childPosition ) {
        return commentBeanList.get (groupPosition).getReplyList ( ).get (childPosition);
    }

    @Override
    public long getGroupId( int groupPosition ) {
        return groupPosition;
    }

    @Override
    public long getChildId( int groupPosition , int childPosition ) {
        return getCombinedChildId (groupPosition , childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView( final int groupPosition , final boolean isExpanded , View convertView , ViewGroup parent ) {
        final GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from (context).inflate (R.layout.comment_item_layout , parent , false);
            groupHolder = new GroupHolder (convertView);
            convertView.setTag (groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag ( );
        }

        if (getGroupCount () == 0){
            groupHolder.linearLayout.setVisibility (View.GONE);
            setHintVisible();
        }else {
            setHintInVisible ();
        }
        //点赞的方法
        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e ("进入点击方法","click");
                if(isLike){
                    Log.e ("取消赞",String.valueOf (groupPosition));
                    isLike = false;
//                    unLikeItem (2,commentBeanList.get (groupPosition).getId ());
                    groupHolder.iv_like.setColorFilter (Color.parseColor ("#AAAAAA"));
                }else {
                    Log.e ("赞了",String.valueOf (groupPosition));
                    isLike = true;
//                    likeItem (2,commentBeanList.get (groupPosition).getId ());
                    groupHolder.iv_like.setColorFilter (Color.parseColor ("#FF5C5C"));
                }
            }
        });
        //用户头像显示
        Glide.with (context).load (otherUserImgUrl)
                .apply (new RequestOptions( )
                        .circleCrop ( )
                        .error (R.mipmap.ic_launcher)
                        .placeholder (R.drawable.userlogo))
                .into (groupHolder.logo);
        //用户名显示
        groupHolder.tv_name.setText (commentBeanList.get (groupPosition).getNickName ( ));
        //评论时间
        groupHolder.tv_time.setText (commentBeanList.get (groupPosition).getDate ( ));
        //评论内容
        groupHolder.tv_content.setText (commentBeanList.get (groupPosition).getContent ( ));
        //收缩展开方法
        if (isExpanded){
            groupHolder.iv_expand.setImageResource (R.drawable.collapse);
        }else {
            groupHolder.iv_expand.setImageResource (R.drawable.expand);
        }
        Map <String, Object> tagMap = new HashMap <> ();
        tagMap.put("groupPosition", groupPosition);
        tagMap.put("isExpanded", isExpanded);
        groupHolder.iv_expand.setTag(tagMap);
        groupHolder.iv_expand.setOnClickListener(iv_expandClickListener);
        //当评论没有回复时将展开按钮隐藏
        if (commentBeanList.get (groupPosition).getReplyList ().isEmpty ()){
            groupHolder.iv_expand.setVisibility (View.GONE);
        }else {
            groupHolder.iv_expand.setVisibility (View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView( int groupPosition , int childPosition , boolean isLastChild , View convertView , ViewGroup parent ) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,parent, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        String replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
        String replyTo = commentBeanList.get (groupPosition).getReplyList ().get (childPosition).getReplyToOtherUser ();
        if(!TextUtils.isEmpty(replyUser)){
            if (!TextUtils.isEmpty (replyTo)){
                childHolder.tv_name.setText (replyUser+" 回复 "+replyTo+":");
            }else {
                childHolder.tv_name.setText(replyUser + ":");
            }
        }else {
            childHolder.tv_name.setText("无名"+":");
        }

        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable( int groupPosition , int childPosition ) {
        return true;
    }


    private class GroupHolder {
        private ImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        private ImageView iv_expand;
        private LinearLayout linearLayout;

        public GroupHolder( View view ) {
            logo = (ImageView) view.findViewById (R.id.comment_item_logo);
            tv_content = (TextView) view.findViewById (R.id.comment_item_content);
            tv_name = (TextView) view.findViewById (R.id.comment_item_userName);
            tv_time = (TextView) view.findViewById (R.id.comment_item_time);
            iv_like = (ImageView) view.findViewById (R.id.comment_item_like);
            iv_expand = (ImageView) view.findViewById (R.id.comment_item_expand);
            linearLayout = (LinearLayout) view.findViewById (R.id.expand_view);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }

    /**
     * by june on 2020/5/1
     * func:评论成功后插入一条数据
     * @param commentDetailBean 新的评论数据
     * */
    public void addTheCommentData(CommentDetailBean commentDetailBean){
        if(commentDetailBean!=null){
            commentDetailBean.setReplyList (new ArrayList <ReplyDetailBean> ());
            commentBeanList.add(commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * by june on 2020/5/1
     * func:回复成功后插入一条数据
     * @param replyDetailBean 新的回复数据
     * */
    public void addTheReplyData(ReplyDetailBean replyDetailBean, int groupPosition){
        if(replyDetailBean!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetailBean.toString() );
            if(commentBeanList.get(groupPosition).getReplyList() != null ){
                commentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
            }else {
                List<ReplyDetailBean> replyList = new ArrayList <> ();
                replyList.add(replyDetailBean);
                commentBeanList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * by june on 2020/5/1
     * func:添加和展示所有回复
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
        if(commentBeanList.get(groupPosition).getReplyList() != null ){
            commentBeanList.get(groupPosition).getReplyList().clear();
            commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
        }else {

            commentBeanList.get(groupPosition).setReplyList(replyBeanList);
        }

        notifyDataSetChanged();
    }


}
