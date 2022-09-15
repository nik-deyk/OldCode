package com.velikiyprikalel.developerslifegifs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.HandlerThread;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.Callable;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "GifLoader";

    private enum CurrentStatus {
        LOADED, PROGRESS, ERROR
    }

    private class GifWithText {
        String url;
        String description;

        GifWithText(String url, String des) {
            this.url = url;
            this.description = des;
        }
    }

    private class Steps {
        public long page = -1;
        ArrayList<GifWithText> GifUrls = new ArrayList<>();
        public int last_elem = 0;
        public int totalCount = 0;
    }

    private final ArrayList<GifWithText> mGifList = new ArrayList<>();
    private volatile int mGifListIndex = 0;

    private volatile boolean bLastWasError = false;

    private TextView mDescriptionView;
    private ImageView mImageView;
    private Spinner mCategorySpinner;
    private Button mClearButton;
    private Button mPrevButton;
    private Handler mHandler;
    private final AtomicReference<CurrentStatus> status = new AtomicReference<>(CurrentStatus.ERROR);
    private final HashMap<String, Steps> mSteps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Gif description:
        mDescriptionView = findViewById(R.id.gifDescription);

        // Set spinner items:
        mCategorySpinner = findViewById(R.id.spinner1);
        String[] items = new String[]{"latest", "hot", "top"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, items);
        mCategorySpinner.setAdapter(adapter);

        // Initialize map:
        for (String item : items) {
            mSteps.put(item, new Steps());
        }

        // Gif view:
        mImageView = findViewById(R.id.imageView);

        // Thread, where we will load json:
        HandlerThread mHandlerThread = new HandlerThread("LoadjsonThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // Next button:
        Button mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new NextClick());

        // Clear button:
        mClearButton = (Button)findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do not allow to do anything when we are in progress:
                // Sorry for that, we will do this chech manually in call on_click methods.
                synchronized (this) {
                    if (!status.compareAndSet(CurrentStatus.LOADED, CurrentStatus.PROGRESS)) {
                        // Maybe error ?
                        if (!status.compareAndSet(CurrentStatus.ERROR, CurrentStatus.PROGRESS)) {
                            // Do not allow:
                            return;
                        }
                    }
                }
                // Make all to the initial state (but they still in glide cache):
                mGifList.clear();
                mGifListIndex = 0;
                mDescriptionView.setText(R.string.clear_btn_show_text);
                mImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_alert, getTheme()));

                // Disable buttons:
                mClearButton.setEnabled(false);
                mPrevButton.setEnabled(false);

                bLastWasError = false;

                status.set(CurrentStatus.LOADED);
            }
        });
        mClearButton.setEnabled(false);

        // Previous button:
        mPrevButton = (Button)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new PrevClick());
        mPrevButton.setEnabled(false);
    }

    private void error(Exception e, String msg) {
        if (!status.compareAndSet(CurrentStatus.PROGRESS, CurrentStatus.ERROR)) {
            Log.d(TAG, "Trying to set state error to non-progress state!");
        } else {
            Log.d(TAG, (e != null) ? e.getMessage() : msg);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), msg,
                            Toast.LENGTH_LONG).show();
                    mDescriptionView.setText(msg);
                    mImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_alert, getTheme()));
                }
            });
        }
    }

    private class PrevClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Do not allow to do anything when we are in progress:
            synchronized (this) {
                if (!status.compareAndSet(CurrentStatus.LOADED, CurrentStatus.PROGRESS)) {
                    // Maybe error ?
                    if (!status.compareAndSet(CurrentStatus.ERROR, CurrentStatus.PROGRESS)) {
                        // Do not allow:
                        Log.d(TAG, "Not allow to go back - progress is running");
                        return;
                    }
                }
            }
            //mGifListIndex is th index of next gif,
            //mGifListIndex-1 is the index of current shown gif.
            //mGifListIndex-2 is the index of gif we need to show now.
            int gif_index = mGifListIndex - (bLastWasError ? 1 : 2);
            if (gif_index < 0) {
                // Impossible situation.
                mPrevButton.setEnabled(false);
                Log.d(TAG, "Why prev button is called?");
                return;
            }
            // If last was
            GifWithText gif_to_show = mGifList.get(gif_index);
            glide_show(gif_to_show, new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object o, Target<GifDrawable> target, boolean b) {
                    synchronized (RequestListener.class) {
                        if (status.get() == CurrentStatus.PROGRESS) {
                            error(e, "Previous gif show error");
                        }
                    }
                    return true;
                }

                @Override
                public boolean onResourceReady(GifDrawable gifDrawable, Object o, Target<GifDrawable> target, DataSource dataSource, boolean b) {
                    synchronized (RequestListener.class) {
                        if (status.get() == CurrentStatus.PROGRESS) {
                            if (!bLastWasError) {
                                mGifListIndex--;
                            }
                            bLastWasError = false;
                            if (mGifListIndex < 2) {
                                mPrevButton.setEnabled(false);
                            }
                            mDescriptionView.setText(gif_to_show.description);
                            status.set(CurrentStatus.LOADED);
                        }
                    }
                    return false;
                }
            });
        }
    }

    /**
     * @param gif_to_show The gif struct to show.
     * @param d The listener to pass to glide.
     */
    @SuppressLint("SetTextI18n")
    private void glide_show(GifWithText gif_to_show, RequestListener<GifDrawable> d) {
        mDescriptionView.setText("Loading...");

        try{
            ViewTarget into = Glide
                    .with(getApplicationContext())
                    .asGif()
                    .load(gif_to_show.url)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                    .listener(d)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private class NextClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Do not allow to do anything when we are in progress:
            synchronized (this) {
                if (!status.compareAndSet(CurrentStatus.LOADED, CurrentStatus.PROGRESS)) {
                    // Maybe error ?
                    if (!status.compareAndSet(CurrentStatus.ERROR, CurrentStatus.PROGRESS)) {
                        // Do not allow:
                        return;
                    }
                }
            }
            GifWithText gif_to_show;
            // If step not null that means we loaded new gif and we need to add it to array.
            final Steps s;
            if (mGifListIndex >= mGifList.size()) {
                // Load new gif:
                String category = mCategorySpinner.getSelectedItem().toString();
                s = mSteps.get(category);
                // If first time, last elem is 0 and size is 0 => stay valid:
                if (s.last_elem >= s.GifUrls.size() - 1) {
                    // Need to get new json:
                    JsonLoader loader = new JsonLoader("https://developerslife.ru/" +
                            category + "/" + (s.page + 1) + "?json=true");
                    FutureTask<String> task = new FutureTask<>(loader);
                    mHandler.post(task);
                    try {
                        String res = task.get();
                        if (res == null) {
                            throw new RuntimeException("Json loader returned null!");
                        }
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray gifs = jsonObject.getJSONArray("result");
                        s.totalCount = jsonObject.getInt("totalCount");
                        s.GifUrls.clear();
                        for (int i = 0; i < gifs.length(); i++) {
                            JSONObject info = gifs.getJSONObject(i);
                            s.GifUrls.add(new GifWithText(info.getString("gifURL"),
                                    info.getString("description")));
                        }
                        s.last_elem = 0;
                        s.page++;
                    } catch (Exception e) {
                        bLastWasError = true;
                        error(e, "API error");
                        return;
                    }
                } else {
                    s.last_elem++;
                }
                if (s.totalCount <= mGifListIndex) {
                    gif_to_show = null;
                } else {
                    gif_to_show = s.GifUrls.get(s.last_elem);
                }
                // add it to the array of cached gifs (in case all was good).
            } else {
                // Load cached gif:
                gif_to_show = mGifList.get(mGifListIndex);
                s = null;
            }

            if (gif_to_show == null) {
                error(null, "No more gifs");
                status.set(CurrentStatus.LOADED);
                return;
            }

            glide_show(gif_to_show, new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object o, Target<GifDrawable> target, boolean b) {
                    synchronized (RequestListener.class) {
                        if (status.get() == CurrentStatus.PROGRESS) {
                            if (s != null) {
                                s.last_elem--; // revert, next try will load this gif.
                            }
                            bLastWasError = true;
                            if (mGifList.size() > 0) {
                                mClearButton.setEnabled(true);
                                mPrevButton.setEnabled(true);
                            }
                            error(e, "Gif load error");
                        }
                    }
                    return true;
                }

                @Override
                public boolean onResourceReady(GifDrawable gifDrawable, Object o, Target<GifDrawable> target, DataSource dataSource, boolean b) {
                    // If all was good - increase mGifListIndex. if New - add.
                    synchronized (RequestListener.class) {
                        if (status.get() == CurrentStatus.PROGRESS) {
                            mGifListIndex++;
                            if (s != null) {
                                mGifList.add(gif_to_show);
                            }
                            mClearButton.setEnabled(true);
                            bLastWasError = false;
                            if (mGifList.size() > 1) {
                                mPrevButton.setEnabled(true);
                            }
                            mDescriptionView.setText(gif_to_show.description);
                            status.set(CurrentStatus.LOADED);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private class JsonLoader implements Callable<String> {
        String url;

        JsonLoader(String url) { this.url = url; }

        @Override
        public String call() {
            HttpURLConnection connection = null;
            AtomicReference<BufferedReader> reader = new AtomicReference<>(null);

            try {
                URL url = new URL(this.url);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader.set(new BufferedReader(new InputStreamReader(stream)));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.get().readLine()) != null) {
                    buffer.append(line+"\n");
                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader.get() != null) {
                        reader.get().close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            return null;
        }
    }
}