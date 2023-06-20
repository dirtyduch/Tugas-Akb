package com.example.rekomendasirestoran;

//Nama: Zulfa Firdaus
//Nim: 10120067
//Kelas: IF-2

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabIndicator;
    private Button btnNext;
    private int position = 0;
    private Button btnGetStarted;
    private Animation btnAnim;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // membuat aktivitas menjadi layar penuh
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // saat aktivitas ini akan diluncurkan, perlu dicek apakah sudah pernah dibuka sebelumnya atau belum
        if (restorePrefData()) {
            // Intro sudah pernah dibuka sebelumnya, langsung ke MainActivity
            openMainActivity();
        }

        setContentView(R.layout.activity_intro);

        // menyembunyikan action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // inisialisasi tampilan
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // mengisi list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Find the Best Restaurants Around You", "Welcome to our Restaurant Recommendations app! Find a traditional restaurant with authentic dishes. Enjoy signature dishes from recipes passed down through generations with an unrivaled taste. From local cuisine to regional specialties, the choices are varied. Treat yourself to an authentic and unforgettable culinary experience.", R.drawable.img12));
        mList.add(new ScreenItem("Powerful Search and Filter Features", "Our maps app has powerful search and filter features. Search for restaurants according to keywords or the type of cuisine you want. Apply distance, price, rating, and additional features filters. Finding the perfect restaurant just got easier and more efficient.", R.drawable.img11));
        mList.add(new ScreenItem("Restaurant Details and Easy Navigation", "Once you've found a restaurant of interest, view its full details such as address, opening hours, menu and user reviews. Our map application has an easy-to-use navigation feature. Get clear directions with one touch. Enjoy a culinary journey without the hassle of finding your way around.", R.drawable.img13));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        // listener untuk tombol next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) { // ketika mencapai halaman terakhir
                    // tampilkan tombol GETSTARTED dan sembunyikan indikator dan tombol next
                    loadLastScreen();
                }
            }
        });

        // listener untuk tablayout
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // listener untuk tombol Get Started
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // buka MainActivity
                openMainActivity();
                // juga perlu menyimpan nilai boolean ke penyimpanan sehingga saat pengguna menjalankan aplikasi lain kali
                // kita dapat mengetahui bahwa mereka telah melihat layar intro sebelumnya
                savePrefsData();
            }
        });

        // listener untuk tombol skip
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

    // menampilkan tombol GETSTARTED dan menyembunyikan indikator dan tombol next
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : menambahkan animasi ke tombol getstarted
        btnGetStarted.setAnimation(btnAnim);
    }

    private void openMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
