package com.example.project007;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    static QrcodeFragment newInstance(Trails trails){
        Bundle args = new Bundle();
        args.putSerializable("result", trails);
        QrcodeFragment fragment = new QrcodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 生成固定大小的二维码(不需网络权限)
     *
     * @param content 需要生成的内容
     * @param width   二维码宽度
     * @param height  二维码高度
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String type;
        String description;
        String title;
        TrailsActivity activity = (TrailsActivity) getActivity();
        type = activity.getTrailsType();
        description = activity.getDescription();
        title = activity.getTitleName();
        View view =inflater.inflate(R.layout.fragment_qrcode, container, false);
        ImageView imageView = view.findViewById(R.id.image_zxing);
        ArrayList<Trails> argument = (ArrayList<Trails>) getArguments().get("result");
        ResultFragment resultFragment =new ResultFragment();
        ArrayList<String> l;
        l=resultFragment.CreateList(argument);
        String result = "\nType: "+ type+"\ndescription: "+description+"\nTitle:"+title+"\nQuartiles:"+l.get(0)+"｜"+l.get(1)+"｜"+l.get(2)+"\nMedian"+l.get(3)+"\nAverage"+l.get(4)+"/nStandardDiviation"+l.get(5);
//        "Type: "+ l.get(6)+"\ndescription: "+l.get(7)+"\nTitle:"+l.get(8)+"
        imageView.setImageBitmap(generateBitmap(result,250,250));
        return view;

    }


}
