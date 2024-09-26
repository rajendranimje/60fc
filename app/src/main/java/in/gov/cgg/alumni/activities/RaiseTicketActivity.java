package in.gov.cgg.alumni.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import in.gov.cgg.alumni.R;
import in.gov.cgg.alumni.databinding.ActivityRaiseticketBinding;

public class RaiseTicketActivity extends Activity {
    ProgressDialog pd;
    //WebView wb;
    //RelativeLayout rl;
    //TextView tv;

    ActivityRaiseticketBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_raiseticket);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_raiseticket);
      /*  wb=findViewById(R.id.webView);
        rl=findViewById(R.id.rl);
        tv=findViewById(R.id.tv_title);*/


        String url = "http://astrouser.com/helpdesk/index.php?a=add&custom1=60FC%20Mobile%20App";
//        String url = "https://dost.cgg.gov.in/documents/Frequently-Asked-Questions-v5.pdf";

//
//
        pd = new ProgressDialog(RaiseTicketActivity.this);
        pd.setMessage("Loading...Please wait ");
        pd.setCancelable(false);

        pd.show();

        binding.webView.getSettings().setJavaScriptEnabled(true);

        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if((!pd.isShowing())&&pd!=null)
                pd.show();
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if((pd.isShowing())&&pd!=null)
                    pd.dismiss();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.loadUrl(url);
    }

}
