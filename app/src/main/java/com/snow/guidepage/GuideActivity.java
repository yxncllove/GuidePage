package com.snow.guidepage;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener
{

  ViewPager mViewPager;
  TextView mIndicator_1;
  TextView mIndicator_2;
  TextView mIndicator_3;

  ImageButton mPrevious;
  ImageButton mNext;
  Button mButton;

  static String[] mTitle = new String[]{"第一页", "第二页", "第三页"};
  static int mIndex;
  static int mPos;
  int[] mBgColors;
  ArgbEvaluator mEvaluator = new ArgbEvaluator();
  List<View> mViews;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);

    initViews();
    initData();

  }

  private void initViews()
  {
    mViewPager = (ViewPager) findViewById(R.id.view_pager);
    mIndicator_1 = (TextView) findViewById(R.id.guide_indicator_1);
    mIndicator_2 = (TextView) findViewById(R.id.guide_indicator_2);
    mIndicator_3 = (TextView) findViewById(R.id.guide_indicator_3);
    mPrevious = (ImageButton) findViewById(R.id.guide_left);
    mNext = (ImageButton) findViewById(R.id.guide_right);
    mButton = (Button) findViewById(R.id.guide_finish);
    mPrevious.setOnClickListener(this);
    mNext.setOnClickListener(this);
    mButton.setOnClickListener(this);
  }

  private void initData()
  {
    mBgColors = new int[]{ContextCompat.getColor(this, R.color.background_color_1),
            ContextCompat.getColor(this, R.color.background_color_2),
            ContextCompat.getColor(this, R.color.background_color_3)};

    mViews = new ArrayList<>();
    mViews.add(mIndicator_1);
    mViews.add(mIndicator_2);
    mViews.add(mIndicator_3);

    GuideAdapter adapter = new GuideAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(adapter);
    mViewPager.setOffscreenPageLimit(3);
    mViewPager.addOnPageChangeListener(this);
  }

  @Override
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.guide_left:
        mViewPager.setCurrentItem(--mIndex);//上一页
        break;
      case R.id.guide_right:
        mViewPager.setCurrentItem(++mIndex);//下一页
        break;
      case R.id.guide_finish:
        Toast.makeText(this, "进入登录页", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
  {
    int colorUpdate = (Integer) mEvaluator.evaluate(positionOffset, mBgColors[position],
            mBgColors[position == 2 ? position : position + 1]);
    mViewPager.setBackgroundColor(colorUpdate);
  }

  @Override
  public void onPageSelected(int position)
  {
    mIndex = position;

    //跟新指示器
    updateIndicator(position);

    mViewPager.setBackgroundColor(mBgColors[position]);
    // 第一页时，不可见，其余时可见
    mPrevious.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    mNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
    mButton.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onPageScrollStateChanged(int state)
  {

  }

  private void updateIndicator(int position)
  {
    for (int i = 0; i < mViews.size(); i++)
    {
      mViews.get(i).setBackgroundResource(i == position ? R.drawable.guide_indicator_shape_true :
              R.drawable.guide_indicator_shape_false);
    }
  }


  // Fragment
  private static class GuideFragment extends Fragment
  {

    public static GuideFragment newInstance()
    {
      return new GuideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
      View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_guide, null);
      //这里只使用 TextView 展示
      TextView tv = view.findViewById(R.id.guide_fragment_text);
      tv.setText(mTitle[mPos++]);
      return view;
    }
  }

  // Adapter
  private static class GuideAdapter extends FragmentPagerAdapter
  {

    private GuideAdapter(FragmentManager fm)
    {
      super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
      return GuideFragment.newInstance();
    }

    @Override
    public int getCount()
    {
      return 3;
    }
  }

}
