package com.example.libassistant.ui.myborrowedbooks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapWell;
import com.example.libassistant.AESUtils;
import com.example.libassistant.BarCode;
import com.example.libassistant.BookInfo;
import com.example.libassistant.BookInfoAdapter;
import com.example.libassistant.ContentActivity;
import com.example.libassistant.ExchangeBookActivity;
import com.example.libassistant.GoodHttp;
import com.example.libassistant.MainActivity;
import com.example.libassistant.OpenWeb;
import com.example.libassistant.R;
import com.example.libassistant.ReaderInfo;
import com.example.libassistant.ServiceReturnData;
import com.example.libassistant.WebService;
import com.example.libassistant.zxing.android.CaptureActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hb.dialog.myDialog.MyAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyBorrowedBooksFragment extends Fragment implements WebService {
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private String url;
    private PieChart pieChart;
    private MyBorrowedBooksViewModel dashboardViewModel;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String barData = data.getStringExtra(DECODED_CONTENT_KEY);
                barData=AESUtils.decrypt(getString(R.string.aeskey),barData);
                Gson gson=new Gson();
                GoodHttp goodHttp;
                BarCode barCode=gson.fromJson(barData,BarCode.class);
                switch (barCode.getType()){
                    case BarCode.TYPE_EXCNANGE:
                        barCode=gson.fromJson(barData,new TypeToken<BarCode<GoodHttp>>(){}.getType());
                        goodHttp=(GoodHttp) barCode.getData();
                        goodHttp.add("uid",MainActivity.tagID);
                        CallService(goodHttp);
                        break;
                    case BarCode.TYPE_WEBVIEW:
                        barCode=gson.fromJson(barData,new TypeToken<BarCode<String>>(){}.getType());
                        String url=(String) barCode.getData();
                        Intent intent=new Intent(getContext(), OpenWeb.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                }
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MyBorrowedBooksViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_my_books, container, false);
        final ContentActivity activity=(ContentActivity)getActivity();

        dashboardViewModel.setData(activity.getBorrowedBooks());
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<BookInfo>>() {
            @Override
            public void onChanged(@Nullable List<BookInfo> s) {
                UpdateUI(activity.getReaderInfo(),s,root);
            }
        });

        return root;
    }

    public void setData(){
        ContentActivity activity=(ContentActivity)getActivity();
        dashboardViewModel.setData(activity.getBorrowedBooks());
        UpdateUI(((ContentActivity) getActivity()).getReaderInfo(),((ContentActivity) getActivity()).getBorrowedBooks(),getView());
    }

    public void UpdateUI(ReaderInfo readerInfo,List<BookInfo> mBorrowedBooks,View view){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        RecyclerView recyclerView=view.findViewById(R.id.listview_now_borrowed_books);
        BookInfoAdapter bookInfoAdapter=new BookInfoAdapter(mBorrowedBooks);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        bookInfoAdapter.setOnItemClickListener(new BookInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, BookInfo data) {
                switch (view.getId()){
                    case R.id.btn_zhuanjie:
                        HashMap<String,String> mp=new HashMap<String, String>();
                        mp.put("service","exchange");
                        mp.put("gch",data.getGch());
                        GoodHttp goodHttp=new GoodHttp(mp,getString(R.string.libserver));

                        BarCode<GoodHttp> barCode=new BarCode<GoodHttp>(BarCode.TYPE_EXCNANGE,goodHttp);
                        Gson gson=new Gson();
                        String barData=gson.toJson(barCode);
                        barData= AESUtils.encrypt(getString(R.string.aeskey),barData);
                        /*以下代码测试打开网页
                        BarCode<String> barCode=new BarCode<String>(BarCode.TYPE_WEBVIEW,"http://www.qq.com/");
                        Gson gson=new Gson();
                        String barData=gson.toJson(barCode);
                        barData= AESUtils.encrypt(getString(R.string.aeskey),barData);
                        */
                        Intent intent=new Intent(getActivity(), ExchangeBookActivity.class);
                        intent.putExtra("barData",barData);
                        startActivity(intent);
                        break;
                    case R.id.btn_xujie:
                        XuJie(data);
                        break;
                }

            }
        });
        //
        DrawPie(readerInfo,view);

        ImageView scanBar=view.findViewById(R.id.scanBarCode);
        scanBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                }
                else{
                    goScan();
                }
            }
        });
    }
    private void XuJie(BookInfo data){
        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("gch",data.getGch());
        mp.put("service","renew");
        GoodHttp goodHttp=new GoodHttp(mp,getString(R.string.libserver));
        String return_json=goodHttp.CallService();
        Gson gson=new Gson();
        ServiceReturnData<String> result=gson.fromJson(return_json,new TypeToken<ServiceReturnData<String>>(){}.getType());
        if(result.getStatus().equals("true")){
            MyAlertDialog dlg=new MyAlertDialog(getContext()).builder();
            dlg.setTitle("恭喜");
            dlg.setMsg("续借成功，下拉刷新即可显示续借结果");
            dlg.show();
        }else{
            MyAlertDialog dlg=new MyAlertDialog(getContext()).builder();
            dlg.setTitle("提示");
            dlg.setMsg("续借失败，可能网络问题");
            dlg.show();
        }
    }
    public void goScan(){
        Intent intent=new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent,REQUEST_CODE_SCAN);
    }
    @Override
    public String CallService(GoodHttp request){
        try {
            String return_data=request.CallService();
            Gson gson=new Gson();
            ServiceReturnData result=gson.fromJson(return_data,ServiceReturnData.class);
            if(result.getStatus().equals("true")){
                MyAlertDialog dlg=new MyAlertDialog(getContext()).builder();
                dlg.setTitle("恭喜");
                dlg.setMsg("转借成功，下拉刷新即可显示");
                dlg.show();
            }
            else{
                MyAlertDialog dlg=new MyAlertDialog(getContext()).builder();
                dlg.setTitle("提示");
                dlg.setMsg("转借失败，可能网络问题");
                dlg.show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private void DrawPie(ReaderInfo readerInfo,View view){
        int total=Integer.parseInt(readerInfo.getCanBorrow());
        int chaoqi=Integer.parseInt(readerInfo.getOverdue());
        int buchaoqi=Integer.parseInt(readerInfo.getNowBorrow())-Integer.parseInt(readerInfo.getOverdue());
        pieChart=view.findViewById(R.id.pieChart1);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(45f);


        ArrayList<PieEntry> pieEntries=new ArrayList<>();
        pieEntries.add(new PieEntry(total,"总共可借阅册数"));
        pieEntries.add(new PieEntry(chaoqi,"超期册数"));
        pieEntries.add(new PieEntry(buchaoqi,"未超期册数"));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);

        PieDataSet pieDataSet=new PieDataSet(pieEntries,"");
        pieDataSet.setColors(colors);
        PieData pieData=new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieChart.setData(pieData);

        Description description=new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setDrawEntryLabels(false);

        Legend legend=pieChart.getLegend();
        pieChart.invalidate();
    }
}
