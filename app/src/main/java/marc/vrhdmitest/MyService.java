package marc.vrhdmitest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyService extends Service {

    private DisplayManager mDisplayManager;
    Context context;
    Handler handler;


    public MyService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();
        context = this;
        handler = new Handler();

        new Thread(){
            @Override
            public void run() {
                super.run();

                final WebView[] webView = new WebView[1];

                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        // Create Window Layout (Overlay)
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                1280,
                                720,
                                WindowManager.LayoutParams.TYPE_TOAST,WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                                PixelFormat.TRANSLUCENT);

                        View view = layoutInflater.inflate(R.layout.hdmi_output, null);
                        webView[0] = (WebView) view.findViewById(R.id.webView);
                        webView[0].getSettings().setJavaScriptEnabled(true);
                        webView[0].setWebViewClient(new WebViewClient());



                        // Get HDMI Display
                        mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
                        String displayCategory = DisplayManager.DISPLAY_CATEGORY_PRESENTATION;
                        Display[] displays = mDisplayManager.getDisplays(displayCategory);

                        // Output to HDMI
                        WindowManager hdmiwm = (WindowManager)createDisplayContext(displays[0]).getSystemService(Context.WINDOW_SERVICE);
                        hdmiwm.addView(view, params);

                    }
                });


                while( true ) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            webView[0].loadUrl("http://casiobp.co.uk");
                        }
                    });

                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            webView[0].loadUrl("http://google.com");
                        }
                    });


                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }



            }
        }.start();




    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }






}
