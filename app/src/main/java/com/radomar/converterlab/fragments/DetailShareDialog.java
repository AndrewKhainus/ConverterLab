package com.radomar.converterlab.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radomar.converterlab.DetailsActivity;
import com.radomar.converterlab.R;
import com.radomar.converterlab.model.CurrencyInfoItemModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Radomar on 17.09.2015
 */
public class DetailShareDialog extends DialogFragment implements View.OnClickListener {

    private ArrayList<CurrencyInfoItemModel> currencyList;
    private String mBankName;
    private String mCityName;

    private Button mShare;
    private Bitmap mBitmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        currencyList = bundle.getParcelableArrayList(DetailsActivity.KEY_BUNDLE);
        mBankName = bundle.getString(DetailsActivity.KEY_BANK);
        mCityName = bundle.getString(DetailsActivity.KEY_CITY);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.fragment_dialog_details, null);

        ImageView image = (ImageView) v.findViewById(R.id.ivImage_FDD);
        mShare = (Button) v.findViewById(R.id.btShare_FDD);
        mShare.setOnClickListener(this);

        View view = getInflatedView();
        mBitmap = getViewBitmap(view);
        image.setImageBitmap(mBitmap);
        return v;
    }


    private Bitmap getViewBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.draw(canvas);

        return bitmap;
    }

    private View getInflatedView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_for_image, null);
        TextView bankName = (TextView)view.findViewById(R.id.tvBankName_LFI);
        bankName.setText(mBankName);

        TextView city = (TextView)view.findViewById(R.id.tvCity_LFI);
        city.setText(mCityName);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llForCurrencies);

        for (int i = 0; i < currencyList.size(); i++) {
            TextView textView = new TextView(getActivity());
            textView.setTextColor(getResources().getColor(R.color.dialog_currency_text_color));
            textView.setTextSize(21);

            StringBuilder sb = new StringBuilder(currencyList.get(i).currencyId);
            sb.append("          ")
                    .append(currencyList.get(i).ask.substring(0, 5))
                    .append("/")
                    .append(currencyList.get(i).bid.substring(0, 5));
            textView.setText(sb.toString());

            textView.setId(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
        }

        int screenWidth = getDisplayWidth();

        LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        linearLayout1.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearLayout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.layout(0, 0, screenWidth, linearLayout1.getMeasuredHeight() + linearLayout.getMeasuredHeight());

        return view;
    }

    private int getDisplayWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    public File saveBitmapToFile(Bitmap _bitmap, String _filename) {

        File result = new File( getActivity().getCacheDir() + File.separator + _filename);
        try {
            FileOutputStream out = new FileOutputStream(result);
            _bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        File file = saveBitmapToFile(mBitmap, "file.png");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
        dismiss();
    }
}
