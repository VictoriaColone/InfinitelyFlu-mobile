package com.ximao.infinitelyflu_mobile.utils.apm;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.didichuxing.doraemonkit.kit.network.NetworkManager;
import com.didichuxing.doraemonkit.kit.performance.PerformanceDataManager;
import com.didichuxing.doraemonkit.kit.performance.PerformanceDokitViewManager;
import com.didichuxing.doraemonkit.kit.performance.datasource.DataSourceFactory;
import com.ximao.infinitelyflu_mobile.R;
import java.lang.ref.WeakReference;


/**
 * @author ximao
 * @date 2021/8/9
 * 悬浮控件服务
 */
public class FloatViewService extends Service {

    private static final String TAG = "FloatViewService";

    /**
     * 定义浮动窗口布局
     */
    LinearLayout mFloatLayout;

    WindowManager.LayoutParams wmParams;

    /**
     * 创建浮动窗口设置布局参数的对象
     */
    WindowManager mWindowManager;

    private Context mContext;

    /**
     * 是否打开心电图
     */
    private boolean isTurnOn = false;

    TextView mNetTextView;

    TextView mCpuTextView;

    TextView mMemoryTextView;

    private Handler mHandler = new MainHandler(this);

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = FloatViewService.this;
        createFloatView();
        new NetWorkSpeedUtils(this, mHandler).startShowNetSpeed();
        new CpuMonitorUtils(mHandler).startShowCpuRate();
        new MemoryUtils(mHandler).startShowMemoryUsage();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        // 获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 设置window type
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 1800;
        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        // 获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_view, null);
        mNetTextView = mFloatLayout.findViewById(R.id.network_text);
        mCpuTextView = mFloatLayout.findViewById(R.id.cpu_text);
        mMemoryTextView = mFloatLayout.findViewById(R.id.memory_text);
        mWindowManager.addView(mFloatLayout, wmParams);
        // 浮动窗口按钮
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mFloatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTurnOn) {
                    startMonitor();
                } else {
                    stopMonitor();
                }
                isTurnOn = !isTurnOn;
            }
        });
    }

    /**
     * 开启监控视图
     */
    private void startMonitor() {
        // 健康体检中找到调用，可以绕过fragment初始化&&id注册
        PerformanceDataManager.getInstance().init();
        NetworkManager.get().startMonitor();
        PerformanceDataManager.getInstance().startMonitorNetFlowInfo();
        PerformanceDataManager.getInstance().startMonitorCPUInfo();
        PerformanceDataManager.getInstance().startMonitorFrameInfo();
        PerformanceDataManager.getInstance().startMonitorMemoryInfo();
        PerformanceDokitViewManager.open(DataSourceFactory.TYPE_NETWORK, getString(R.string.net_work),null);
        PerformanceDokitViewManager.open(DataSourceFactory.TYPE_CPU, getString(R.string.cpu),null);
        PerformanceDokitViewManager.open(DataSourceFactory.TYPE_RAM, getString(R.string.ram),null);
        PerformanceDokitViewManager.open(DataSourceFactory.TYPE_FPS, getString(R.string.fps),null);
    }

    /**
     * 关闭监控视图
     */
    private void stopMonitor() {
        PerformanceDataManager.getInstance().stopMonitorCPUInfo();
        PerformanceDataManager.getInstance().stopMonitorMemoryInfo();
        PerformanceDataManager.getInstance().stopMonitorFrameInfo();
        NetworkManager.get().stopMonitor();
        PerformanceDataManager.getInstance().stopMonitorNetFlowInfo();
        PerformanceDokitViewManager.close(DataSourceFactory.TYPE_NETWORK, getString(R.string.net_work));
        PerformanceDokitViewManager.close(DataSourceFactory.TYPE_CPU, getString(R.string.cpu));
        PerformanceDokitViewManager.close(DataSourceFactory.TYPE_RAM, getString(R.string.ram));
        PerformanceDokitViewManager.close(DataSourceFactory.TYPE_FPS, getString(R.string.fps));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    /**
     * handler 静态内部类
     */
    static class MainHandler extends Handler {

        // 弱引用<引用外部类>
        WeakReference<FloatViewService> mService;
        // 构造创建弱引用
        MainHandler(FloatViewService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            // 通过弱引用获取外部类.
            FloatViewService service = mService.get();
            // 进行非空再操作
            if (service != null) {
                switch (msg.what) {
                    case 100:
                        service.mNetTextView.setText(msg.obj.toString());
                        break;
                    case 200:
                        service.mCpuTextView.setText(msg.obj.toString());
                        break;
                    case 300:
                        service.mMemoryTextView.setText(msg.obj.toString());
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        }
    }
}

