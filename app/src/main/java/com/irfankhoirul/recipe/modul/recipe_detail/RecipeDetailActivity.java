package com.irfankhoirul.recipe.modul.recipe_detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.pojo.Step;
import com.irfankhoirul.recipe.modul.ingredient.IngredientFragment;
import com.irfankhoirul.recipe.modul.step.StepFragment;
import com.irfankhoirul.recipe.modul.step_detail.StepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepFragment.StepFragmentListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.container)
    ViewPager viewPager;
    FrameLayout flDetailStepContainer;

    private RecipeDetailViewPagerAdapter mSectionsPagerAdapter;
    private IngredientFragment ingredientFragment;
    private StepFragment stepFragment;
    private boolean isTablet;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        recipe = getIntent().getParcelableExtra("recipe");

        setupToolbar();

        flDetailStepContainer = (FrameLayout) findViewById(R.id.fl_detail_step_container);
        isTablet = flDetailStepContainer != null;

        setupFragment(savedInstanceState);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupFragment(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            ingredientFragment = IngredientFragment.newInstance(recipe.getIngredients());
            stepFragment = StepFragment.newInstance(recipe.getSteps(), isTablet);
        } else {
            ingredientFragment = (IngredientFragment) fragmentManager
                    .getFragment(savedInstanceState, "ingredientFragment");
            stepFragment = (StepFragment) fragmentManager.getFragment(savedInstanceState,
                    "stepFragment");
        }

        mSectionsPagerAdapter = new RecipeDetailViewPagerAdapter(
                fragmentManager,
                ingredientFragment,
                stepFragment);
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showDetailStepFragment(Step step) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_detail_step_container, StepDetailFragment.newInstance(step, isTablet))
                .commit();
    }

    @Override
    public void onStepClicked(Step step) {
        showDetailStepFragment(step);
    }
}
