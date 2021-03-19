package com.example.project007;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ResultFragment extends Fragment {
    private String type;
    private String description;
    private String title;


    static ResultFragment newInstance(Trails trails){
        Bundle args = new Bundle();
        args.putSerializable("result", trails);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public double[] Quartiles(double[] val) {
        double[] ans = new double[3];

        for (int quartileType = 1; quartileType < 4; quartileType++) {
            float length = val.length + 1;
            double quartile;
            float newArraySize = (length * ((float) (quartileType) * 25 / 100)) - 1;
            Arrays.sort(val);
            if (newArraySize % 1 == 0) {
                quartile = val[(int) (newArraySize)];
            } else {
                int newArraySize1 = (int) (newArraySize);
                quartile = (val[newArraySize1] + val[newArraySize1 + 1]) / 2;
            }
            ans[quartileType - 1] =  quartile;
        }
        return ans;
    }
    public static double Med(ArrayList<Double> total){
        double j = 0;
        //集合排序
        Collections.sort(total);
        int size = total.size();
        if(size % 2 == 1){
            j = total.get((size-1)/2);
        }else {
            //加0.0是为了把int转成double类型，否则除以2会算错
            j = (total.get(size/2-1) + total.get(size/2) + 0.0)/2;
        }
        return j;
    }
    public static double avg(double[] x) {
        double sum = 0;//用来保存数组内所有数值的和
        for (double v : x) {
            sum += v;//遍历每一个数组相，将每一个数组相的值加到sum上
        }
        return sum / x.length;
    }
    public static double StandardDiviation(double[] x) {
        double dAve=avg(x);
        double dVar=0;
        for (double v : x) {//求方差
            dVar += (v - dAve) * (v - dAve);
        }
        return Math.sqrt(dVar/x.length);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> CreateList(ArrayList<Trails> argument){
        ArrayList<Double> list = new ArrayList<>();
        ArrayList<String> l = new ArrayList<>();
        for(Trails t : argument){
            list.add(Double.parseDouble(t.getVariesData()));
        }
        double[] d = list.stream().mapToDouble(i->i).toArray();
        if(d == null || d.length <4){
            String p1 = "None";
            String p2 = "None";
            String p3 = "None";
            l.add(p1);
            l.add(p2);
            l.add(p3);
        }else {
            double[] a = Quartiles(d);
            @SuppressLint("DefaultLocale") String p1 = String.format("%.2f",a[0]);
            @SuppressLint("DefaultLocale") String p2 = String.format("%.2f",a[1]);
            @SuppressLint("DefaultLocale") String p3 = String.format("%.2f",a[2]);
            l.add(p1);
            l.add(p2);
            l.add(p3);
        }
        @SuppressLint("DefaultLocale") String p4 = String.format("%.2f",Med(list));
        @SuppressLint("DefaultLocale") String p5 = String.format("%.2f",avg(d));
        @SuppressLint("DefaultLocale") String p6 = String.format("%.2f",StandardDiviation(d));
        TrailsActivity activity = (TrailsActivity) getActivity();
        type = activity.getTrailsType();
     //   description = activity.getDescription();
      //  title = activity.getTitleName();
        l.add(p4);
        l.add(p5);
        l.add(p6);
        l.add(type);
        l.add(description);
        l.add(title);
        return l;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Trails> argument = (ArrayList<Trails>) getArguments().get("result");

        View view =inflater.inflate(R.layout.fragment_result, container, false);
        TextView Quartile1 = view.findViewById(R.id.quartile1);
        TextView Quartile2 = view.findViewById(R.id.quartile2);
        TextView Quartile3 = view.findViewById(R.id.quartile3);
        TextView Median = view.findViewById(R.id.median);
        TextView Mean = view.findViewById(R.id.mean);
        TextView Stdev = view.findViewById(R.id.stdev);
        TextView Type = view.findViewById(R.id.type);
        TextView Description = view.findViewById(R.id.description);
        TextView Title = view.findViewById(R.id.title);

        ArrayList<String> l = CreateList(argument);

        Quartile1.setText(l.get(0));
        Quartile2.setText(l.get(1));
        Quartile3.setText(l.get(2));
        Median.setText(l.get(3));
        Mean.setText(l.get(4));
        Stdev.setText(l.get(5));
        Type.setText(l.get(6));
        Description.setText(l.get(7));
        Title.setText(l.get(8));

        return view;
    }
}