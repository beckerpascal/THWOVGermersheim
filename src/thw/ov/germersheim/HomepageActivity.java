package thw.ov.germersheim;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class HomepageActivity extends Activity {

	private WebView mWebView;
	private ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);

		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);
		progress.setProgressDrawable(getResources().getDrawable(
				R.drawable.progressbar_style));

		mWebView = (WebView) findViewById(R.id.homepageView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		mWebView.setInitialScale(1);
		mWebView.setWebChromeClient(new THWWebChromeClient());
		mWebView.setWebViewClient(new THWWebViewClient());

		mWebView.loadUrl("http://www.thw-germersheim.de");
	}

	private class THWWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			HomepageActivity.this.setValue(newProgress);
			super.onProgressChanged(view, newProgress);
		}
	}

	private class THWWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (!isOnline()) {
				view.loadData(
						"Sie haben momentan keinen Internetzugriff. <br/>Bitte sp&auml;ter erneut versuchen!<br/><br/><a href=\"http://www.thw-germersheim.de\">Neu laden...</a>",
						"text/html", "UTF-8");
			} else {
				view.loadUrl(url);
				view.setInitialScale(1);
			}
			return true;
		}
	}

	public void setValue(int progress) {
		this.progress.setProgress(progress);
		if (progress < 10) {

			AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
			fadeIn.setDuration(500);
			fadeIn.setFillAfter(true);
			this.progress.startAnimation(fadeIn);
			this.progress.setVisibility(ProgressBar.VISIBLE);
		}
		if (progress == 100) {
			AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
			fadeOut.setDuration(500);
			fadeOut.setFillAfter(true);
			this.progress.startAnimation(fadeOut);
			this.progress.setVisibility(ProgressBar.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
