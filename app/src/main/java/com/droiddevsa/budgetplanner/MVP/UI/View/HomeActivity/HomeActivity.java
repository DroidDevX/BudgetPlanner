package com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity;


import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.droiddevsa.budgetplanner.Async.BackgroundTaskManger;
import com.droiddevsa.budgetplanner.Async.RxJavaWrapper;
import com.droiddevsa.budgetplanner.MVP.Data.Models.Budget;
import com.droiddevsa.budgetplanner.MVP.Data.Repository.Repository;
import com.droiddevsa.budgetplanner.MVP.Presenters.HomePresenter;
import com.droiddevsa.budgetplanner.MVP.UI.HomeViewPager.HomeViewPagerAdapter;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ActivityNavigator;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.ConcreteActivityNavigator;
import com.droiddevsa.budgetplanner.MVP.UI.ViewInterface.HomeView;
import com.droiddevsa.budgetplanner.R;
import com.droiddevsa.budgetplanner.Utilities.ViewClickableFlags;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements HomeView, AllBudgetFragmentPage.EventListener{

    private String BUNDLE_SELECTED_INDEX="selectedIndex";
    private int VIEWPAGER_SELECTED_PAGEINDEX=0;


    //Log
    private final String TAG = "homeActivityFilter";

    //OnClickListener() flags
    ViewClickableFlags viewIsClickableFlags = new ViewClickableFlags();
    private String FLAG_ADD_BTN_CLICKABLE="addBtnClickable";
    private String FLAG_DELETE_BTN_CLICKABLE="deleteBtnClickable";
    private String FLAG_BUDGET_CLICKABLE="budgetViewClickable";

    //Loading and Get started overlays
    View loadingOverlayContainer;
    View getStartedFrameLayout;


    //Presenter
    HomePresenter presenter;
    BackgroundTaskManger taskManger = new RxJavaWrapper();
    ConcreteActivityNavigator activityNavigator = new ConcreteActivityNavigator();

    //UI
    ViewPager viewPager;
    HomeViewPagerAdapter homepagerAdapter;
    FloatingActionButton  createBudgetBtn;
    TabLayout tabLayout;
    TabLayout.OnTabSelectedListener tabSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");


        if(savedInstanceState!=null)
            VIEWPAGER_SELECTED_PAGEINDEX = savedInstanceState.getInt(BUNDLE_SELECTED_INDEX);

        createBudgetBtn = findViewById(R.id.createBudgetBtn);
        tabLayout =findViewById(R.id.homeTabs);

        //Overlays
        loadingOverlayContainer = findViewById(R.id.home_loading_container);
        getStartedFrameLayout =findViewById(R.id.no_content_overlay_container);

        //Vjew Pager
        homepagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.homeViewPager);
        viewPager.setAdapter(homepagerAdapter);
        setupViewPagerTabs(viewPager);


        //Initialization of Presenter
        Repository repository =  new Repository();
        repository.setDatabaseManager(this,getResources().getString(R.string.DATABASE_NAME),getResources().getInteger(R.integer.DATBASE_VERSION));
        presenter = new HomePresenter(repository);
        presenter.attachView(this);
        presenter.setBackgroundTaskManger(taskManger);

        //Navigator
        activityNavigator.attachContext(this);
        viewIsClickableFlags.addFlag(FLAG_ADD_BTN_CLICKABLE,true);
        viewIsClickableFlags.addFlag(FLAG_DELETE_BTN_CLICKABLE,true);
        viewIsClickableFlags.addFlag(FLAG_BUDGET_CLICKABLE,true);

 }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt(BUNDLE_SELECTED_INDEX,VIEWPAGER_SELECTED_PAGEINDEX);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void setupViewPagerTabs(final ViewPager viewPager) {
        final TabLayout tablayout = findViewById(R.id.homeTabs);

        tabSelectedListener=createTabSelectedListener();
        tablayout.addOnTabSelectedListener(tabSelectedListener);

        viewPager.addOnPageChangeListener(createHomeViewPagerListener());

    }

    public ViewPager.OnPageChangeListener createHomeViewPagerListener(){
       Log.d(TAG, "createHomeViewPagerListener: ");

        return  new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position,positionOffset,false);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: ");
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab == null)
                    return;

                switch (position) {

                    case 0:
                        setTitle("Home");
                        createBudgetBtn.show();
                        break;

                    case 1:
                        setTitle("Budget History");
                        createBudgetBtn.hide();
                        break;

                    case 2:
                        setTitle("Accumulated Balance");
                        createBudgetBtn.hide();
                        break;
                }

                tab.select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
   }

   public TabLayout.OnTabSelectedListener createTabSelectedListener(){
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG,"onTabSelected()");
                if(tab.getIcon()==null)
                    return;

                highlightSelectedTab(tabLayout,tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getIcon()==null)
                    return;

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getIcon()==null)
                    return;

            }
        };
   }




   public void highlightIcon(Drawable icon){
        int highligthtColor = getResources().getColor(R.color.colorprimaryText_new);
        icon.setColorFilter(highligthtColor,PorterDuff.Mode.SRC_IN);
   }

    public void dehighlightIcon(Drawable icon){
        int highligthtColor = getResources().getColor(R.color.colorSecondaryText_new);
        icon.setColorFilter(highligthtColor,PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onDeleteBtnClicked(String budgetID) {
        if(!viewIsClickableFlags.getValue(FLAG_DELETE_BTN_CLICKABLE))
            return;

        viewIsClickableFlags.setViewClickable(FLAG_DELETE_BTN_CLICKABLE,false);// Only may click once

        presenter.deleteBudget(budgetID);
    }

    public void onAddBudgetBtnClicked(View view){
        Log.d(TAG, "onAddBudgetBtnClicked: ");
        if(!viewIsClickableFlags.getValue(FLAG_ADD_BTN_CLICKABLE))
            return;

        viewIsClickableFlags.setViewClickable(FLAG_ADD_BTN_CLICKABLE,false);

        presenter.createBudget();
    }

    @Override
    public void onBudgetCreated(String newBudgetID) {
        Log.d(TAG, "onBudgetCreated: ");
         loadingOverlayContainer.setVisibility(View.VISIBLE);
         activityNavigator.startCurrentBudgetActivity(newBudgetID);
    }

    @Override
    public void onBudgetSelected(String budgetID) {
        if(!viewIsClickableFlags.getValue(FLAG_BUDGET_CLICKABLE))
            return;

        viewIsClickableFlags.setViewClickable(FLAG_ADD_BTN_CLICKABLE,false);

        loadingOverlayContainer.setVisibility(View.VISIBLE);
        activityNavigator.startCurrentBudgetActivity(budgetID);
    }

    @Override
    public void onBudgetsReady(ArrayList<Budget> budgets) {
        Log.d(TAG,"onBudgetsReady()");

        if(budgets!=null)
            Log.d(TAG, "budgetlist size: "+budgets.size());

        viewIsClickableFlags.setAllViewsClickable(true);
        homepagerAdapter.setBudgetlist(budgets);
        homepagerAdapter.notifyDataSetChanged();

    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");

        displayloadingScreen(true);
        displayGetStartedMessage(false);

        presenter.onViewResume();


    }

    @Override
    public void displayloadingScreen(boolean isVisible) {
        if(isVisible)
            loadingOverlayContainer.setVisibility(View.VISIBLE);
        else
            loadingOverlayContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        activityNavigator.detachContext();
        ((RxJavaWrapper)taskManger).disposeObserverable();
        super.onDestroy();
    }


    @Override
    public void displayGetStartedMessage(boolean messageIsVisible) {
        Log.d(TAG,"displayGetStartedMessage(), visble: "+messageIsVisible);

        if(messageIsVisible) {

            applyColorFilterToView(getStartedFrameLayout.findViewById(R.id.getStartedImageView),
                                    getResources().getColor(R.color.colorprimaryText_new));

            getStartedFrameLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        }
        else {
            getStartedFrameLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }

    }

    public void applyColorFilterToView(View view, int color){
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        view.getBackground().setColorFilter(colorFilter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();

        highlightSelectedTab(tabLayout,0);
    }

    private void highlightSelectedTab(TabLayout tabLayout, int tabSelectedPos){
        Log.d(TAG, "highlightSelectedTab: ");
        for(int tabPos=0;tabPos< tabLayout.getTabCount();tabPos++){
            TabLayout.Tab tab = tabLayout.getTabAt(tabPos);
            if(tab==null)
                continue;

            if(tab.getIcon()==null)
                continue;

            if(tabPos==tabSelectedPos)
                highlightIcon(tab.getIcon());
            else
                dehighlightIcon(tab.getIcon());
        }
    }

}





