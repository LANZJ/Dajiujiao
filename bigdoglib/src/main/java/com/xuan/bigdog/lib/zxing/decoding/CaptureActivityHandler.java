package com.xuan.bigdog.lib.zxing.decoding;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xuan.bigdog.lib.zxing.DGScanActivity;
import com.xuan.bigdog.lib.zxing.camera.CameraManager;
import com.xuan.bigdog.lib.zxing.view.ViewfinderResultPointCallback;

public class CaptureActivityHandler extends Handler {

	private static final String TAG = CaptureActivityHandler.class
			.getSimpleName();

	private final DGScanActivity activity;
	private final DecodeThread decodeThread;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(DGScanActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(activity.getViewfinderView()));
		decodeThread.start();
		state = State.SUCCESS;
		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case ZxingConstants.AUTO_FOCUE:
			// Log.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this,
						ZxingConstants.AUTO_FOCUE);
			}
			break;
		case ZxingConstants.RESTART_PREVIEW:
			Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case ZxingConstants.DECODE_SUCCEEDED:
			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			/***********************************************************************/
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
					.getParcelable(DecodeThread.BARCODE_BITMAP);// 锟斤拷锟矫憋拷锟斤拷锟竭筹拷

			activity.handleDecode((Result) message.obj, barcode);// 锟斤拷锟截斤拷锟?
																	// /***********************************************************************/
			break;
		case ZxingConstants.DECODE_FAILED:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					ZxingConstants.DECODE);
			break;
		case ZxingConstants.RETURN_SCAN_RESULT:
			Log.d(TAG, "Got return scan result message");
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
			break;
		case ZxingConstants.LAUNCH_PRODUCT_QUERY:
			Log.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(),
				ZxingConstants.QUIT);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(ZxingConstants.DECODE_SUCCEEDED);
		removeMessages(ZxingConstants.DECODE_FAILED);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					ZxingConstants.DECODE);
			CameraManager.get().requestAutoFocus(this,
					ZxingConstants.AUTO_FOCUE);
			activity.drawViewfinder();
		}
	}

}
