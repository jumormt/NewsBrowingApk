package com.example.a76947.cmnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.a76947.cmnet.adapter.CommonAdapter;
import com.example.a76947.cmnet.adapter.ViewHolder;
import com.example.a76947.cmnet.model.CellInfo;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.MediaType;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.a76947.cmnet.base.DataResult;


public class ListActivity extends AppCompatActivity {

    private static final String TAG = "图片网址： ";
    private Button btnToDetail;

    // 首页面列表
    private PullToRefreshListView MainList;

    private ListView MainListson;

    // 分页
    private int page = 1;
    // 单次传输数据数量
    private int limit = 5;

    private CommonAdapter<CellInfo> adapter;

    private ProgressDialog dialog;

    // 存放待刷新模型数据
    private ArrayList<CellInfo> list = new ArrayList<CellInfo>();

    private String falg; // 判断是否最后一页

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

//    private Bitmap bmp;

    private List<Holderplus> helpergloble;

    private String urlgloble;

    class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private Holderplus myImageView;
        private String mUrl;

        public NewsAsyncTask(Holderplus imageView,String url){
            myImageView = imageView;
            mUrl = url;
        }
        //String...params是可变参数接受execute中传过来的参数
        @Override
        protected Bitmap doInBackground(String... params) {

            String url=params[0];
            //这里同样调用我们的getBitmaFromeUrl
            Bitmap bitMap = getURLimage(params[0]);
            int width = bitMap.getWidth();
            int height = bitMap.getHeight();
            // 设置想要的大小
            int newWidth = 150;
            int newHeight = 150;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
                    true);
            return bitMap;
        }
        //这里的bitmap是从doInBackgroud中方法中返回过来的
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(myImageView.tag.equals(mUrl)) {
                myImageView.holder.setImageBitmap(R.id.imageView1, bitmap);
            }

        }
    }

    class Holderplus{
        ViewHolder holder;
        String tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("正在加载中...");

        init();
        setListener();
        getCell();

//        btnToDetail = (Button)findViewById(R.id.btnToDetail);
//        btnToDetail.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                            Toast.makeText(ListActivity.this, "You click dashboard!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        adapter = null;
//        getCell();
    }

    // 初始化列表
    public void init() {
        MainList = (PullToRefreshListView)findViewById(R.id.refresh_list_view);
        MainListson = MainList.getRefreshableView();

        ILoadingLayout startLabels = MainList.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = MainList.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

    }

    public void setListener() {
        MainList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (list != null) {
                    list.clear();
                }
                page = 1;
                dialog.show();
                adapter = null;
                getCell();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                dialog.show();
                getCell();
            }

        });
        MainListson.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(ListActivity.this, DetailActivity.class);
                intent.putExtra("flag", "order");
                Bundle bundle = new Bundle();
                bundle.putSerializable("cellDetail", list.get(position - 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    // 更新列表数据源
    public void getCell(){
        sendRequestWithOkHttp();
//        CellInfo cell1 = new CellInfo();
//        cell1.setCelltitle("111111");
//        cell1.setCellurl("http://m.blog.csdn.net/dev_csdn/article/details/78792014");
//        cell1.setCellpic("http://pub.xitaoinfo.com/pt_0f7006eda83549598130ab4d790cc582.jpg?imageView2/1/w/80/h/80/q/75");
//        CellInfo cell2 = new CellInfo();
//        cell2.setCelltitle("222222");
//        cell2.setCellurl("https://www.baidu.com/");
//        cell2.setCellpic("http://pub.xitaoinfo.com/pt_df8d717f819740f38d47efcbfedce796.jpg?imageView2/1/w/80/h/80/q/75");
//        CellInfo cell3 = new CellInfo();
//        cell3.setCelltitle("333333");
//        cell3.setCellurl("http://www.byr.edu.cn/index.php");
//        cell3.setCellpic("http://pub.xitaoinfo.com/pt_44ae2b09aeb44b5bb4fdb05ff8185bc9.jpg?imageView2/1/w/80/h/80/q/75");
//
//        list.add(cell1);
//        list.add(cell2);
//        list.add(cell3);


//        for (int i = 1; i <= 3; ++i){
//
//            CellInfo cell = new CellInfo();
//            cell.setCelltitle(Integer.toString(i));
//            cell.setCellurl("https://www.baidu.com/");
//            list.add(cell);
//        }

//        MainList.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                MainList.onRefreshComplete();
//            }
//        }, 1000);
        setCell(list);
    }

    // 发送网络请求接收数据
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
//                    String json = "{'id':'1','name':'Jack','age':'11'}";
                    RequestBody requestBody =  new FormBody.Builder()
                            .add("page", Integer.toString(page))
                            .add("limit", Integer.toString(limit))
                            .build();
//                    String json = "[{'id':'1','name':'Jack','age':'11'},{'id':'2','name':'Mike','age':'12'}]";
//                    JSONObject jsonObj2 = new JSONObject(json);
//                    jsonObj2 = null;JSON


//                    RequestBody requestBody1 = RequestBody.create(JSON,json);
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
//                            .url("http://192.168.56.1:8880/get_data.json")
                            .url("http://192.168.56.1:8880/register.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    MainList.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MainList.onRefreshComplete();
                        }
                    }, 1000);
                    dialog.dismiss();
                    String responseData = response.body().string();
                    Log.d("responsedata",responseData);
                    Log.d("responsedatalength",Integer.toString(responseData.length()));
                    JSONArray jsonArray = new JSONArray(responseData);
//                    jsonArray.length() == 0
                    if (responseData.equals("\n[]\n")){
//                        MainList.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                MainList.onRefreshComplete();
//                            }
//                        }, 1000);
                        Log.d("ListActivity","数据空");
                        Looper.prepare();
                        Toast.makeText(ListActivity.this, "亲，没有数据了！", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }else{
                        Log.d("ListActivity","收到数据");
//                        System.out.println(responseData);
//                        Toast.makeText(ListActivity.this, "亲，没有数据了！", Toast.LENGTH_SHORT).show();
                        parseJSONWithJSONObject(responseData);
                    }
//                    parseJSONWithJSONObject(responseData);
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
                } catch (Exception e) {
                    MainList.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MainList.onRefreshComplete();
                        }
                    }, 1000);
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRequestWithOkHttp2(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("page", Integer.toString(page))
                .add("limit", Integer.toString(limit))
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.56.1:8880/response.php")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "opps,请求失败了！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                Toast.makeText(getApplicationContext(), "请求成功！", Toast.LENGTH_SHORT).show();
                String responseData = response.body().string();
                if (responseData != null){
                    parseJSONWithJSONObject(responseData);
                }else{
                    MainList.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MainList.onRefreshComplete();
                        }
                    }, 1000);
                    Toast.makeText(ListActivity.this, "亲，没有数据了！", Toast.LENGTH_SHORT).show();
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }
        });
    }


    // 解析返回json
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                addListWithJsonobj(jsonObject, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新list
    private void addListWithJsonobj(JSONObject jsonobject, ArrayList<CellInfo> lis){
        try{
            CellInfo cell = new CellInfo();
            String cellid = jsonobject.getString("id");
            String celltitle = jsonobject.getString("title");
            String cellurl = jsonobject.getString("url");
            String cellpic = jsonobject.getString("image");
            cell.setCellid(cellid);
            cell.setCelltitle(celltitle);
            cell.setCellurl(cellurl);
            cell.setCellpic(cellpic);
            lis.add(cell);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    //更新ui
    public void setCell(ArrayList<CellInfo> celldata){

//        if (adapter ==null) {
////        if(true){
//            list = celldata;
//            adapter = new CommonAdapter<CellInfo>(this, list, R.layout.item_cell) {
//                @Override
//                public void convert(ViewHolder helper, CellInfo item) {
//                    helper.setText(R.id.celltitle, item.getCelltitle());
////                    MainList.onRefreshComplete();
//                }
//            };
//            MainListson.setAdapter(adapter);
////            MainList.onRefreshComplete();
//
//        }else{
//            // adapter.onDateChange(list);
//            if (list == null || list.size() == 0) {
//                list = celldata;
//            } else {
//                list.addAll(celldata);
//            }
//            adapter.onDateChange(list);
////            MainList.onRefreshComplete();
//            if (celldata.size() == 1) {
//                MainListson.setSelection(list.size() - 1);
//            }
//        }

        list = celldata;
        adapter = new CommonAdapter<CellInfo>(this, list, R.layout.item_cell) {
            @Override
            public void convert(final ViewHolder helper, CellInfo item) {
                Holderplus holderplus = new Holderplus();
                holderplus.holder = helper;
                holderplus.tag = item.getCellpic();
//                helpergloble =  insertElement(helpergloble, holderplus, 1);
//                helpergloble.add(holderplus);
                helper.setText(R.id.celltitle, item.getCelltitle());
                showImageByAsyncTask(holderplus,item.getCellpic());
//                helper.setImageResource(R.id.imageView1, R.drawable.apple_pic);
//                showImageByThead(helper, "http://imgstore04.cdn.sogou.com/app/a/100520024/877e990117d6a7ebc68f46c5e76fc47a");

//                helper.setImageBitmap(R.id.imageView1, getURLimage("http://img.zcool.cn/community/046d60586dd9a2a8010e321330dd3f.jpg@80w_80h_1c_1e_1o_100sh.jpg"));
//                    MainList.onRefreshComplete();
            }
        };
        MainListson.setAdapter(adapter);

    }

    public void showImageByAsyncTask(Holderplus holder,String url){
        new NewsAsyncTask(holder,url).execute(url);
    }




//    public void showImageByThead(final ViewHolder holder,final String url){
//        helpergloble = holder;
//        urlgloble = url;
//        new Thread(new Runnable() {
//            public void run() {
//                Bitmap bitmap = getURLimage(urlgloble);
////从网上取图片
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = bitmap;
//                handle.sendMessage(msg);
//            }
//        }).start();
//    }
//    //在消息队列中实现对控件的更改
//    private Handler handle = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    System.out.println("111");
//                    Bitmap bmp=(Bitmap)msg.obj;
//                    helpergloble.setImageBitmap(R.id.imageView1, bmp);
//                    break;
//            }
//        };
//    };

    /**
     * 从服务器取图片
     * @param url
     * @return
     */
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}


