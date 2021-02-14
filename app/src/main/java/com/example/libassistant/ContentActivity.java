package com.example.libassistant;

import android.os.Bundle;
import android.webkit.WebSettings;

import com.example.libassistant.ui.myborrowedbooks.MyBorrowedBooksFragment;
import com.example.libassistant.ui.myborrowedbooks.MyBorrowedBooksViewModel;
import com.example.libassistant.ui.myhistorybooks.MyHistoryBooksFragment;
import com.example.libassistant.ui.myhistorybooks.MyHistoryBooksViewModel;
import com.example.libassistant.ui.myinfo.MyInfoFragment;
import com.example.libassistant.ui.myinfo.MyInfoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContentActivity extends AppCompatActivity {

    private String mReaderData;
    private ReaderInfo m_readerInfo;
    private List<BookInfo> m_borrowedBooks=new ArrayList<>();
    private List<BookInfo> m_historyBooks=new ArrayList<>();

    private MyInfoFragment m_fragmentMyInfo;
    private MyBorrowedBooksFragment m_fragmentBorrowedBooks;
    private MyHistoryBooksFragment m_fragmentHistoryBooks;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_myinfo, R.id.menu_my_books, R.id.menu_history_books)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        refreshLayout=(SwipeRefreshLayout) findViewById(R.id.refresh_data);
        refreshLayout.setSlingshotDistance(500);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
                refreshLayout.setRefreshing(false);
            }
        });

        mReaderData=getIntent().getStringExtra("data");
        Gson gson=new Gson();
        ServiceReturnData returnData=gson.fromJson(mReaderData,new TypeToken<ServiceReturnData<ReaderInfo>>(){}.getType());
        m_readerInfo=(ReaderInfo) returnData.getData();
        m_borrowedBooks=m_readerInfo.getBorrowedBooksList();
        m_historyBooks=m_readerInfo.getHistoryBookList();
    }
    public List<BookInfo> getBorrowedBooks(){
        return m_borrowedBooks;
    }
    public List<BookInfo> getHistoryBooks(){
        return m_historyBooks;
    }
    public ReaderInfo getReaderInfo(){
        return m_readerInfo;
    }
    public void RefreshData(){

        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("service","verifyuser");
        mp.put("uid",MainActivity.tagID);
        mp.put("password",MainActivity.userpassword);

        String server=getString(R.string.libserver);
        GoodHttp goodHttp=new GoodHttp(mp,server);

        try {
            mReaderData=goodHttp.CallService();
            Gson gson=new Gson();
            ServiceReturnData returnData=gson.fromJson(mReaderData,new TypeToken<ServiceReturnData<ReaderInfo>>(){}.getType());
            m_readerInfo=(ReaderInfo) returnData.getData();
            m_borrowedBooks=m_readerInfo.getBorrowedBooksList();
            m_historyBooks=m_readerInfo.getHistoryBookList();
            Fragment mMainNavFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

            Fragment fragment = mMainNavFragment.getChildFragmentManager().getPrimaryNavigationFragment();
            if (fragment instanceof MyHistoryBooksFragment) {
                ((MyHistoryBooksFragment) fragment).setData();
            }
            if(fragment instanceof MyInfoFragment){
                ((MyInfoFragment) fragment).setData();
            }
            if(fragment instanceof MyBorrowedBooksFragment){
                ((MyBorrowedBooksFragment) fragment).setData();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public SwipeRefreshLayout getRefreshLayout(){
        return refreshLayout;
    }
}
