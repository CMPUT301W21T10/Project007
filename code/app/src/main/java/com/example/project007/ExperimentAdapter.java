package com.example.project007;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This is ExperimentAdapter
 * Adapter for experiment List
 */
public class ExperimentAdapter extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;
    private Animation animation;  //删除时候的动画
    private float downX;  //点下时候获取的x坐标
    private float upX;   //手指离开时候的x坐标
    private Button globalDelButton; //用于执行删除的button

    private View globalView;

    public ExperimentAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context,0,experiments);
        this.experiments = experiments;
        this.context = context;
        animation= AnimationUtils.loadAnimation(context, R.anim.push_out);  //用xml获取一个动画

    }

    @Override
    public int getCount() {
        return experiments.size();
    }

    @Override
    public Experiment getItem(int arg0) {
        return experiments.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return experiments.get(arg0).getId();
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // face our layout
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.overview_content,parent,false);
        }
        // get current element
        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.name_view);
        TextView experimentDescription = view.findViewById(R.id.description_view);

        ImageView image = view.findViewById(R.id.experiment_image);

        experimentName.setText(experiment.getName());
        experimentDescription.setText(experiment.getDescription());
        switch (experiment.getType()){
            case "Binomial": image.setImageResource(R.drawable.b); break;
            case "Measurement": image.setImageResource(R.drawable.m); break;
            case "Count": image.setImageResource(R.drawable.c); break;
            case "IntCount": image.setImageResource(R.drawable.n); break;
        }
        /*
        view.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:  //手指按下
                        downX = event.getX(); //获取手指x坐标
                        if (globalDelButton != null)
                        break;
                    case MotionEvent.ACTION_UP:  //手指离开
                        upX = event.getX(); //获取x坐标值
                        break;
                }

                if (delButton != null) {
                    if (Math.abs(- downX + upX) > 300) {  //2次坐标的绝对值如果大于35，就认为是左右滑动
                        delButton.setVisibility(View.VISIBLE);  //显示删除button
                        globalDelButton = delButton;  //赋值给全局button，一会儿用
                        globalView = v; //得到itemview，在上面加动画
                        return true; //终止事件
                    }
                    return false;  //释放事件，使onitemClick可以执行
                }
                return false;
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {  //为button绑定事件

            @Override
            public void onClick(View v) {

                if (globalDelButton != null) {
                    globalDelButton.setVisibility(View.GONE);  //点击删除按钮后，影藏按钮
                    deleteItem(globalView, position);   //删除数据，加动画
                }

            }
        });

*/
        return view;

    }

    public void deleteItem(View view,final int position)
    {
        view.startAnimation(animation);  //给view设置动画
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { //动画执行完毕
                Experiment instance = experiments.get(position);
                DatabaseController.deleteExperiment(instance);
                experiments.remove(position);  //把数据源里面相应数据删除
                notifyDataSetChanged();
            }
        });

    }

}
