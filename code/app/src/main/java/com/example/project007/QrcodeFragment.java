package com.example.project007;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrcodeFragment extends Fragment {
    public String testtype="null";
    public String binomial_type="null";
    public Trails trail;

    public QrcodeFragment(String testtype){
        this.testtype=testtype;
    }
    public QrcodeFragment(){}
    public QrcodeFragment(Trails trails,String testtype,String binomial_type ){
        this.trail=trails;
        this.testtype=testtype;
        this.binomial_type=binomial_type;
    }

    /**
     * generate a fixed-size QRcode(without network permission required)
     *
     * @param content content of Qrcode
     * @param width   width of Qrcode
     * @param height  Height of Qrcode
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (testtype.equals("experiment")) {
            String type;
            String description;
            String title;
            TrailsActivity activity = (TrailsActivity) getActivity();
            type = activity.getTrailsType();
            description = activity.getDescription();
            title = activity.getTitleName();
            View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
            ImageView imageView = view.findViewById(R.id.image_zxing);
            ArrayList<Trails> argument = (ArrayList<Trails>) getArguments().get("result");
            ResultFragment resultFragment = new ResultFragment();
            ArrayList<String> l;
            l = resultFragment.CreateList(argument, activity);
            String result = "\nType: " + type + "\ndescription: " + description + "\nTitle:" + title + "\nQuartiles:" + l.get(0) + "｜" + l.get(1) + "｜" + l.get(2) + "\nMedian" + l.get(3) + "\nAverage" + l.get(4) + "/nStandardDiviation" + l.get(5);
            //        "Type: "+ l.get(6)+"\ndescription: "+l.get(7)+"\nTitle:"+l.get(8)+"
            imageView.setImageBitmap(generateBitmap(result, 250, 250));
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
            ImageView imageView = view.findViewById(R.id.image_zxing);
            String binotrail_result;
            String trail_type;
            String trail_result;
            String ID=trail.getID().toString();
            if (binomial_type.equals("success") || binomial_type.equals("failure")) {
                binotrail_result = trail.getSuccess();
                trail_type = trail.getType();
                String result = "\nType: " + trail_type +" ID is: "+ ID+" "+binomial_type+" "+"<--binomial_type";
                imageView.setImageBitmap(generateBitmap(result, 250, 250));
                return view;
            } else {
                trail_result = trail.getVariesData();
                trail_type = trail.getType();
                String result = "\nType: " + trail_type +" ID is: "+ ID+" "+"\n";
                imageView.setImageBitmap(generateBitmap(result, 250, 250));
                return view;
            }
        }
    }
}
