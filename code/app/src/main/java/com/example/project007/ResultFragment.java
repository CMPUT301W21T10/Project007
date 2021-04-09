package com.example.project007;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * This class show the result of the trails, which includes six numbers and two plots.
 * <p>
 * The six numbers are the three quartile numbers and median and mean.<br>
 * The two plots are the histogram and the linear chart run with the time.<br>
 *     </p>
 * @return
 * Return a view to show the result of the experiment.
 */

public class ResultFragment extends Fragment{
    private String type;
    private String description;
    private String title;
    private View view;

    private LineChartView lineChartView;
    private LineChartData lineChartData;
    private ColumnChartView columnCharView;
    private ColumnChartData columnChartData;
    private final int numberOfLines = 1;
    private int numberOfPoints = 10000000;
    float[][] randomNumbersTab = new float[numberOfLines][numberOfPoints];

    private final boolean hasAxes = true;
    private final boolean hasAxesNames = true;
    private final boolean hasLines = true;
    private final boolean hasPoints = true;
    private final ValueShape shape = ValueShape.CIRCLE;
    private final boolean isFilled = false;
    private final boolean hasLabels = false;
    private final boolean isCubic = false;
    private final boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    private ColumnChartView columnChartView;
    private ColumnChartData data;
    private final List<AxisValue> mAxisXValues = new ArrayList<>();



    /**
     * This function calculate the quartiles, which includes three numbers.
     * @param val The double list of numbers.
     * @return Return a double list which contains the three quartiles.
     */
    public static double[] Quartiles(double[] val) {
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
    /**
     * This function calculate the median.
     * @param total A list of double type numbers.
     * @return A double type number to show the median of the arraylist of numbers.
     */
    public static double Med(ArrayList<Double> total){
        double j = 0;
        Collections.sort(total);
        int size = total.size();
        if(size % 2 == 1){
            j = total.get((size-1)/2);
        }else {
            j = (total.get(size/2-1) + total.get(size/2) + 0.0)/2;
        }
        return j;
    }
    /**
     * This function calculate the average.
     * @param x A list of double type numbers.
     * @return Return a double type number to show the average of the arraylist of numbers.
     */
    public static double avg(double[] x) {
        double sum = 0;
        for (double v : x) {
            sum += v;
        }
        double average = sum / x.length;
        return average;
    }
    public static double StandardDiviation(double[] x) {
        double dAve=avg(x);
        double dVar=0;
        for (double v : x) {
            dVar += (v - dAve) * (v - dAve);
        }
        return Math.sqrt(dVar/x.length);
    }
    /**
     * This method create a list to record the data of result.
     * The data includes six numbers,which are the three quartile numbers and median and mean.
     * @param argument Trails of the experiment.
     * @param activity From the TrailsActivity
     * @return Return a list to represent the important information like quartiles, median, average, standard deviation and type, description, title of the experiement.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> CreateList(ArrayList<Trails> argument,TrailsActivity activity){
        ArrayList<Double> list = new ArrayList<>();
        type = activity.getTrailsType();
        description = activity.getDescription();
        title = activity.getTitleName();
        if (type.equals("Binomial")){
            for(Trails t : argument){
                list.add(Double.parseDouble(t.getSuccess())/(Double.parseDouble(t.getSuccess())+Double.parseDouble(t.getFailure())));
            }
        }else{
            for(Trails t : argument){
                list.add(Double.parseDouble(t.getVariesData()));
            }
        }
        double[] d = list.stream().mapToDouble(i->i).toArray();
        ArrayList<String> l = new ArrayList<>();
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
        l.add(p4);
        l.add(p5);
        l.add(p6);
        l.add(type);
        l.add(description);
        l.add(title);
        return l;
    }

    private double getRateOfSuccess(Trails t) {
        double s = Double.parseDouble(t.getSuccess());
        double f = Double.parseDouble(t.getFailure());
        double m = s / (s + f);
        return m;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private double[] getList(ArrayList<Trails> argument,String name){
        ArrayList<Double> list = new ArrayList<>();
        int l = 0;
        if(name.equals("Binomial")){
            for(Trails t : argument){
                list.add(getRateOfSuccess(t));
            }
        }else if(name.equals("Count-based")){
            for(Trails t : argument){
                list.add(Double.parseDouble(t.getVariesData()));
            }
        }else {
            Double m = Double.parseDouble(argument.get(0).getVariesData());
            list.add(m);
            l+=m;
            for (int i=1;i<argument.size();i++) {
                Double n = Double.parseDouble(argument.get(i).getVariesData());
                list.add((n+l)/(i+1));
                l+=n;
            }
        }
        return list.stream().mapToDouble(i->i).toArray();
    }

    private static double getMaxValue(double[] arr) {
        double max = arr[0];
        for(int i=0;i<arr.length;i++) {
            if(arr[i]>max)
                max = arr[i];
        }
        return max;
    }

    private void initView() {
        lineChartView = view.findViewById(R.id.chart);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData(ArrayList<Trails> argument,String name) {
        generateValues(argument,name);
        generateData(name);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        resetViewport(argument,name);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resetViewport(ArrayList<Trails> argument,String name) {
        final Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = new Float(getMaxValue(getList(argument,name))+0.2);
        v.left=0;
        v.right = numberOfPoints - 1;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateValues(ArrayList<Trails> argument,String name) {
        for (int i = 0; i < numberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] =new Float((getList(argument,name))[j]);
            }
        }
    }

    private void generateData(String name) {
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }
            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }
        lineChartData = new LineChartData(lines);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setTextColor(Color.BLACK);
                axisY.setTextColor(Color.BLACK);
                if (name.equals("Count-based")){
                    axisY.setName("quantity");
                }else if(name.equals("Binomial")){
                    axisY.setName("Rate of Success");
                }else {
                    axisY.setName("Mean");
                }
                axisX.setName("Number of Trails(Time)");
            }
            lineChartData.setAxisXBottom(axisX);
            lineChartData.setAxisYLeft(axisY);
        } else {
            lineChartData.setAxisXBottom(null);
            lineChartData.setAxisYLeft(null);
        }
        lineChartData.setBaseValue(Float.NEGATIVE_INFINITY);
        lineChartView.setLineChartData(lineChartData);
    }

    private float getNumberOfSuccess(ArrayList<Trails> argument){
        float i = 0;
        for(Trails t : argument){
            i = i + Float.parseFloat(t.getSuccess());
        }
        return i;
    }

    private float getNumberOfFailure(ArrayList<Trails> argument){
        float i = 0;
        for(Trails t : argument){
            i = i + Float.parseFloat(t.getFailure());
        }
        return i;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private double[] getNum(ArrayList<Trails> argument){
        ArrayList<Double> list = new ArrayList<>();
        for (Trails t : argument) {
            list.add(Double.parseDouble(t.getVariesData()));
        }
        return list.stream().mapToDouble(i->i).toArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Double> getXaxis(ArrayList<Trails> argument){
        double[] d = getNum(argument);
        double max = getMaxValue(d);
        ArrayList<Double> list = new ArrayList<>();
        list.add(0.0);
        for(double i=0.2;i<1.0;i+=0.2){
            list.add(max*i);
        }
        list.add(max);
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Float> getYaxis(ArrayList<Trails> argument){
        ArrayList<Double> list = getXaxis(argument);
        ArrayList<Float> l = new ArrayList<>();
        float[]item={0,0,0,0,0};
        double[] d = getNum(argument);
        for(double n : d){
            if(n>=list.get(0)&&n<=list.get(1)){
                item[0]+=1;
            }else if(n>=list.get(1)&&n<=list.get(2)){
                item[1]+=1;
            }else if(n>=list.get(2)&&n<=list.get(3)){
                item[2]+=1;
            }else if(n>=list.get(3)&&n<=list.get(4)){
                item[3]+=1;
            }else{
                item[4]+=1;
            }
        }
        for(float m : item){
            l.add(m);
        }
        return l;
    }

    private float getMax(ArrayList<Float> arr){
        float max = arr.get(0);
        for(int i=0;i<arr.size();i++) {
            if(arr.get(i)>max)
                max = arr.get(i);
        }
        return max;
    }

    private void initview() {
        columnChartView = view.findViewById(R.id.column_chart);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initdata(ArrayList<Trails> argument,String name) {
        generateDefaultData(argument,name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateDefaultData(ArrayList<Trails> argument, String name) {
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        String[] str = {"True","False"};
        int numSubcolumns = 1;
        if (name.equals("Binomial")){
            int numColumns = 2;
            ArrayList<Float> l = new ArrayList<>();
            l.add(getNumberOfSuccess(argument));
            l.add(getNumberOfFailure(argument));
            for (int i = 0; i < numColumns; ++i) {
                values = new ArrayList<>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(l.get(i), ChartUtils.pickColor()));
                }
                mAxisXValues.add(new AxisValue(i).setLabel(str[i]));
                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }
        }else{
            int numColumns = 5;
            ArrayList<Float> lu = getYaxis(argument);
            ArrayList<Double> lm = getXaxis(argument);
            for (int i = 0; i < numColumns; ++i) {
                values = new ArrayList<>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(lu.get(i), ChartUtils.pickColor()));
                }
                @SuppressLint("DefaultLocale") String s = (String.format("%.2f",lm.get(i))+"-"+String.format("%.2f",lm.get(i+1)));
                mAxisXValues.add(new AxisValue(i).setLabel(s));
                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                columns.add(column);
            }
        }
        data = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Range");
                axisY.setName("Quantity");
                axisX.setValues(mAxisXValues);
                axisX.setTextColor(Color.BLACK);
                axisY.setTextColor(Color.BLACK);
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        columnChartView.setColumnChartData(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Trails> sortTime(ArrayList<Trails> arr){
        arr.sort((p1, p2) -> p1.getTime().compareTo(p2.getTime()));
        return arr;
    }
    //disable menu in frag
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    //disable menu in frag

    /**
     * This method create a view to show the results and the plots.
     * The six numbers are the three quartile numbers and median and mean.
     * The two plots are the histogram and the linear chart run with the time.
     * @param inflater load layout
     * @param container Gather views
     * @param savedInstanceState Save current data to avoid data loss.
     * @return Return a view, which shows the results of the experiments and the two plots.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Trails> argument = (ArrayList<Trails>) getArguments().get("result");
        numberOfPoints = argument.size();
        argument = sortTime(argument);
        view =inflater.inflate(R.layout.fragment_result, container, false);
        TextView Quartile1 = view.findViewById(R.id.quartile1);
        TextView Quartile2 = view.findViewById(R.id.quartile2);
        TextView Quartile3 = view.findViewById(R.id.quartile3);
        TextView Median = view.findViewById(R.id.median);
        TextView Mean = view.findViewById(R.id.mean);
        TextView Stdev = view.findViewById(R.id.stdev);
        TextView Type = view.findViewById(R.id.type);
        TextView Description = view.findViewById(R.id.description);
        TextView Title = view.findViewById(R.id.title);

        TrailsActivity activity = (TrailsActivity) getActivity();
        ArrayList<String> l = CreateList(argument,activity);

        Quartile1.setText(l.get(0));
        Quartile2.setText(l.get(1));
        Quartile3.setText(l.get(2));
        Median.setText(l.get(3));
        Mean.setText(l.get(4));
        Stdev.setText(l.get(5));
        Type.setText(l.get(6));
        Description.setText(l.get(7));
        Title.setText(l.get(8));

        initView();
        initData(argument,l.get(6));

        initview();
        initdata(argument,l.get(6));
        return view;
    }


}