package com.example.libassistant.ui.myinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.libassistant.AdditionalMyInfo;
import com.example.libassistant.AdditionalMyInfoAdapter;
import com.example.libassistant.AssistActivity;
import com.example.libassistant.BookInfo;
import com.example.libassistant.ContentActivity;
import com.example.libassistant.NewBookActivity;
import com.example.libassistant.OpacActivity;
import com.example.libassistant.OpenWeb;
import com.example.libassistant.R;
import com.example.libassistant.ReaderInfo;
import com.hb.dialog.myDialog.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class MyInfoFragment extends Fragment {

    private List<BookInfo> mBorrowedBooks=new ArrayList<>();
    private ReaderInfo readerInfo;
    private View view;

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private MyInfoViewModel homeViewModel;
    private Switch aSwitch;
    private List<AdditionalMyInfo> myAdditionalService=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(MyInfoViewModel.class);
        ContentActivity activity=(ContentActivity)getActivity();

        homeViewModel.setData(activity.getReaderInfo());
        final View root = inflater.inflate(R.layout.fragment_myinfo, container, false);

        homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ReaderInfo>() {
            @Override
            public void onChanged(@Nullable ReaderInfo readerInfos) {
                UpdateUI(readerInfos,root);
            }
        });


        return root;
    }
    public void UpdateUI(ReaderInfo readerInfos,View view){
        //读取设置指纹识别
        final SharedPreferences preferences=getActivity().getSharedPreferences("settings",0);
        aSwitch=(Switch)view.findViewById(R.id.useFinger);
        aSwitch.setChecked(preferences.getBoolean("useFinger",false));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("useFinger",b);
                editor.commit();
            }
        });
        //获取数据
        ContentActivity activity=(ContentActivity)getActivity();
        readerInfos=activity.getReaderInfo();
        TextView t_name=view.findViewById(R.id.txt_name);
        t_name.setText("姓名："+readerInfos.getName());
        TextView t_sex=view.findViewById(R.id.txt_sex);
        t_sex.setText("性别："+readerInfos.getSex());
        TextView t_depart=view.findViewById(R.id.txt_department);
        t_depart.setText("单位："+readerInfos.getDepartment());
        TextView t_job=view.findViewById(R.id.txt_job);
        t_job.setText("级别："+readerInfos.getJob_title());
        ImageView img_photo=view.findViewById(R.id.imageView_myphoto);
        ImageView imgviewAlert=view.findViewById(R.id.my_msg);
        if(Integer.parseInt(readerInfos.getOverdue())>0){
            imgviewAlert.setImageResource(R.drawable.newmsg);
            final ReaderInfo finalReaderInfos = readerInfos;
            imgviewAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAlertDialog dialog=new MyAlertDialog(v.getContext()).builder();
                    dialog.setTitle("超期提示");
                    dialog.setMsg("你有"+ finalReaderInfos.getOverdue()+"书超期未还");
                    dialog.show();
                }
            });
        }
        else
        {
            imgviewAlert.setImageResource(R.drawable.nomsg);
        }
        Glide.with(view)
                .load(readerInfos.getPhoto())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(img_photo);
        //初始化功能菜单RecyclerView
        if(myAdditionalService.size()==0){
            InitAdditionalService();
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getActivity());
        RecyclerView recyclerView=view.findViewById(R.id.listview_services);
        AdditionalMyInfoAdapter adapter =new AdditionalMyInfoAdapter(myAdditionalService);
        adapter.setOnItemClickListener(new AdditionalMyInfoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Assist();
                        break;
                    case 1:
                        NewBookShow();
                        break;
                    case 2:
                        Opac_search();
                        break;
                    case 3:
                        EZiyuan();
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    private void InitAdditionalService(){
        myAdditionalService.add(new AdditionalMyInfo("智能客服",R.drawable.zhinengkefu));
        myAdditionalService.add(new AdditionalMyInfo("新书推荐",R.drawable.tushutuijian));
        myAdditionalService.add(new AdditionalMyInfo("文献检索",R.drawable.searchbook));
        myAdditionalService.add(new AdditionalMyInfo("数字资源",R.drawable.shuziziyuan));
    }
    public MyInfoViewModel getViewModel(){
        return homeViewModel;
    }
    private void Opac_search(){
        Intent intent=new Intent(getActivity(), OpacActivity.class);
        startActivity(intent);
    }
    private void Assist(){
        Intent intent=new Intent(getActivity(), AssistActivity.class);
        ContentActivity activity=(ContentActivity)getActivity();
        readerInfo=activity.getReaderInfo();
        intent.putExtra("userpic",readerInfo.getPhoto());
        startActivity(intent);
    }
    public void setData(){
        ContentActivity activity=(ContentActivity)getActivity();
        homeViewModel.setData(activity.getReaderInfo());
        UpdateUI(((ContentActivity) getActivity()).getReaderInfo(),getView());
    }
    public void NewBookShow(){
        Intent intent=new Intent(getActivity(), NewBookActivity.class);
        startActivity(intent);
    }
    private void EZiyuan(){
        Intent intent=new Intent(getActivity(), OpenWeb.class);
        intent.putExtra("url","http://svod5306.utools.club:8000/static/html/eziyuan.html");
        startActivity(intent);
    }
}
