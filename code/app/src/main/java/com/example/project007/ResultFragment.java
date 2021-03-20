package com.example.project007;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import static android.graphics.Insets.add;
import static com.example.project007.R2.id.add;


public class ResultFragment extends Fragment{
    private String type;
    private String description;
    private String title;
    private View view;

    private LineChartView lineChartView;        //显示线条的自定义View
    private LineChartData lineChartData;          // 折线图封装的数据类
    private ColumnChartView columnCharView;
    private ColumnChartData columnChartData;
    private int numberOfLines = 1;
    private int numberOfPoints = 10000000;    //点的数量
    float[][] randomNumbersTab = new float[numberOfLines][numberOfPoints];

    private boolean hasAxes = true;       //是否有轴，x和y轴
    private boolean hasAxesNames = true;   //是否有轴的名字
    private boolean hasLines = true;       //是否有线（点和点连接的线）
    private boolean hasPoints = true;       //是否有点（每个值的点）
    private ValueShape shape = ValueShape.CIRCLE;    //点显示的形式，圆形，正方向，菱形
    private boolean isFilled = false;                //是否是填充
    private boolean hasLabels = false;               //每个点是否有名字
    private boolean isCubic = false;                 //是否是立方的，线条是直线还是弧线
    private boolean hasLabelForSelected = false;       //每个点是否可以选择（点击效果）
    private boolean pointsHaveDifferentColor;           //线条的颜色变换

    private ColumnChartView columnChartView;            //柱状图的自定义View
    private ColumnChartData data;             //存放柱状图数据的对象
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();



    //   四种数据的计算
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
        TrailsActivity activity = (TrailsActivity) getActivity();
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



//    准备数据绘制线性图
    //提取ArrayList<Trails>的variesData并转化成double[]
    @RequiresApi(api = Build.VERSION_CODES.N)
    public double[] getList(ArrayList<Trails> argument,String name){
        ArrayList<Double> list = new ArrayList<>();
        int l = 0;
        if(name.equals("Binomial")){
            for(Trails t : argument){
                list.add(Double.parseDouble(t.getSuccess()));
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
    //y轴获取最大值
    public static double getMaxValue(double[] arr) {
        double max = arr[0];
        for(int i=0;i<arr.length;i++) {
            if(arr[i]>max)
                max = arr[i];
        }
        return max;
    }

//    绘制图表
    private void initView() {
        //实例化
        lineChartView = view.findViewById(R.id.chart);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData(ArrayList<Trails> argument,String name) {
        // Generate some random values.
        generateValues(argument,name);   //设置四条线的值数据
        generateData(name);    //设置数据

        // Disable viewport recalculations, see toggleCubic() method for more info.
        lineChartView.setViewportCalculationEnabled(false);

        lineChartView.setZoomType(ZoomType.HORIZONTAL);//设置线条可以水平方向收缩，默认是全方位缩放
        resetViewport(argument,name);   //设置折线图的显示大小
    }
    /**
     * 图像显示大小
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resetViewport(ArrayList<Trails> argument,String name) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = new Float(getMaxValue(getList(argument,name))+1.0);
        v.left=0;
        v.right = numberOfPoints - 1;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);
    }

    /**
     * 设置四条线条的数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateValues(ArrayList<Trails> argument,String name) {
         //二维数组，线的数量和点的数量
        for (int i = 0; i < numberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] =new Float((getList(argument,name))[j]);
            }
        }
    }

    /**
     * 配置数据
     */
    private void generateData(String name) {

        //存放线条对象的集合
        List<Line> lines = new ArrayList<Line>();
        //把数据设置到线条上面去
        //线条的数量
        TrailsActivity activity = (TrailsActivity) getActivity();
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
                axisX.setTextColor(Color.BLACK);//设置x轴字体的颜色
                axisY.setTextColor(Color.BLACK);//设置y轴字体的颜色
                if (name.equals("Count-based")){
                    axisY.setName("quantity");
                }else if(name.equals("Binomial")){
                    axisY.setName("Number of Success");
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

//绘制直方图 准备数据
    public float getNumberOfSuccess(ArrayList<Trails> argument){
        float i = 0;
        for(Trails t : argument){
            i = i + Float.parseFloat(t.getSuccess());
        }
        return i;
    }
    public float getNumberOfFailure(ArrayList<Trails> argument){
        float i = 0;
        for(Trails t : argument){
            i = i + Float.parseFloat(t.getFailure());
        }
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double[] getNum(ArrayList<Trails> argument){
        ArrayList<Double> list = new ArrayList<>();
        for (Trails t : argument) {
            list.add(Double.parseDouble(t.getVariesData()));
        }
        return list.stream().mapToDouble(i->i).toArray();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Double> getXaxis(ArrayList<Trails> argument){
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
    public ArrayList<Float> getYaxis(ArrayList<Trails> argument){
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
    public float getMax(ArrayList<Float> arr){
        float max = arr.get(0);
        for(int i=0;i<arr.size();i++) {
            if(arr.get(i)>max)
                max = arr.get(i);
        }
        return max;
    }
    //绘制图表
    private void initview() {
        columnChartView = view.findViewById(R.id.column_chart);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resetviewport(ArrayList<Float> argument) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(columnChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = getMax(argument)+2;
        columnChartView.setMaximumViewport(v);
        columnChartView.setCurrentViewport(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initdata(ArrayList<Trails> argument,String name) {
        generateDefaultData(argument,name);
    }
    /**
     * 默认显示的数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateDefaultData(ArrayList<Trails> argument, String name) {
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        String[] str = {"True","False"};
        int numSubcolumns = 1;
        if (name.equals("Binomial")){
            int numColumns = 2;
            ArrayList<Float> l = new ArrayList<>();
            l.add(getNumberOfSuccess(argument));
            l.add(getNumberOfFailure(argument));
            resetviewport(l);
            for (int i = 0; i < numColumns; ++i) {
                values = new ArrayList<SubcolumnValue>();
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
            resetviewport(lu);
            for (int i = 0; i < numColumns; ++i) {
                values = new ArrayList<>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(lu.get(i), ChartUtils.pickColor()));
                }
                String s = (lm.get(i)+"-"+lm.get(i+1));
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

        initView();
        initData(argument,l.get(6));

        initview();
        initdata(argument,l.get(6));
        return view;
    }


}