package com.example.project007;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Result extends Fragment {


    static Result newInstance(Trails trails){
        Bundle args = new Bundle();
        args.putSerializable("result", trails);
        Result fragment = new Result();
        fragment.setArguments(args);
        return fragment;
    }
    public static Pair QuarAndMed(double[] x) {
        // 转成BigDecimal类型，避免失去精度
        BigDecimal[] datas = new BigDecimal[x.length];
        for (int i = 0; i < x.length; i++) {
            datas[i] = BigDecimal.valueOf(x[i]);
        }
        int len = datas.length;// 数组长度
        Arrays.sort(datas);    // 数组排序，从小到大
        BigDecimal q1 = null;  // 第一四分位
        BigDecimal q2 = null;  // 第二四分位
        Pair<BigDecimal,BigDecimal> pair = new Pair<>(q1,q2);
        int index = 0; // 记录下标
        // n代表项数，因为下标是从0开始所以这里理解为：len = n+1
        if (len % 2 == 0) { // 偶数
            index = new BigDecimal(len).divide(new BigDecimal("4")).intValue();
            q1 = datas[index - 1].multiply(new BigDecimal("0.25")).add(datas[index].multiply(new BigDecimal("0.75")));
            q2 = datas[len / 2].add(datas[len / 2 - 1]).divide(new BigDecimal("2"));
        } else { // 奇数
            q1 = datas[new BigDecimal(len).multiply(new BigDecimal("0.25")).intValue()];
            q2 = datas[new BigDecimal(len).multiply(new BigDecimal("0.5")).intValue()];
        }
        return pair;
    }
    public static double avg(double[] x) {
        double sum = 0;//用来保存数组内所有数值的和
        for (int i = 0; i < x.length; i++) {
            sum += x[i];//遍历每一个数组相，将每一个数组相的值加到sum上
        }
        double avg = sum / x.length;//计算平均值：总和除以总个数
        return avg;
    }
    public static double StandardDiviation(double[] x) {
        double dAve=avg(x);
        double dVar=0;
        for(int i=0;i<x.length;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return Math.sqrt(dVar/x.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Trails> argument = (ArrayList<Trails>) getArguments().get("result");
        ArrayList<Double> list = new ArrayList<Double>();
        for(Trails t : argument){
            list.add(Double.parseDouble(t.getVariesData()));
        }
        double[] d = list.stream().mapToDouble(i->i).toArray();

        View view =inflater.inflate(R.layout.fragment_result, container, false);
        TextView Quartile = view.findViewById(R.id.quartile);
        TextView Median = view.findViewById(R.id.median);
        TextView Mean = view.findViewById(R.id.mean);
        TextView Stdev = view.findViewById(R.id.stdev);

        Quartile.setText("1");
        Median.setText("1");
//        Mean.setText("1");
//        Stdev.setText("1");
//        Pair pair = QuarAndMed(d);
//        Quartile.setText(pair.first.toString());
//        Median.setText(pair.second.toString());
        Mean.setText(Double.toString(avg(d)));
        Stdev.setText(Double.toString(StandardDiviation(d)));

        return view;
    }
}