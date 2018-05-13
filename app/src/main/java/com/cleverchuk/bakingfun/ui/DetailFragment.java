/*
 * MIT License
 *
 * Copyright (c) 2018 Chukwubuikem Ume-Ugwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.cleverchuk.bakingfun.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleverchuk.bakingfun.R;
import com.cleverchuk.bakingfun.adapters.StepsAdapter;
import com.cleverchuk.bakingfun.databinding.FragmentDetailBinding;
import com.cleverchuk.bakingfun.models.Step;
import com.cleverchuk.bakingfun.utils.ImageDownloader;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements ExoPlayer.EventListener,
        ImageDownloader.OndoInbackGroundCompleteListener {
    private static final String PLAYER_POSITION = "seek-location";
    private static final String PLAYER_STATE = "player-state";
    private FragmentDetailBinding mBinding;

    private SimpleExoPlayer mPlayer;
    private Step mStep;
    private Context mContext;
    private boolean playPause = true;
    private ImageDownloader imageDownloader;


    private long playerPosition = 0;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment getInstance(Step step, boolean playPause) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepsAdapter.KEY, step);
        args.putBoolean("state", playPause);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(StepsAdapter.KEY);
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playPause = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        Bundle args = getArguments();
        if (args != null) {
            mStep = args.getParcelable(StepsAdapter.KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentDetailBinding.inflate(inflater, container, false);
        mBinding.setStep(mStep);
        mBinding.playerView.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_video_unavailable));
        imageDownloader =new ImageDownloader(mStep.getThumbnailURL(),this);
        imageDownloader.execute();

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            preparePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            preparePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            playerPosition = mPlayer.getCurrentPosition();
            playPause = mPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            playerPosition = mPlayer.getCurrentPosition();
            playPause = mPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(StepsAdapter.KEY, mStep);
        outState.putLong(PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAYER_STATE, playPause);
    }

    private void preparePlayer() {

        if (mPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

            mBinding.playerView.setPlayer(mPlayer);
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            String userAgent = Util.getUserAgent(mContext, mContext.getPackageName());
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent,
                    bandwidthMeter);

            Log.e("VIDEO URL", mStep.getVideoURL());
            Uri uri = Uri.parse(mStep.getVideoURL());
            MediaSource videoSource = new ExtractorMediaSource(uri, dataSourceFactory,
                    new DefaultExtractorsFactory(), null, null);

            mPlayer.addListener(this);
            mPlayer.prepare(videoSource);
            mPlayer.setPlayWhenReady(playPause);
            mPlayer.seekTo(playerPosition);
        }else{
            mPlayer.setPlayWhenReady(playPause);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.removeListener(this);
            mPlayer.stop();
            mPlayer.release();
        }
        imageDownloader.cancel(true);
        imageDownloader = null;
        mPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e("loading", String.valueOf(isLoading));
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        /* change the state to pause if movie has ended */
        if (playbackState == ExoPlayer.STATE_ENDED) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        mBinding.playerView.setVisibility(View.INVISIBLE);
        mBinding.videoUnavailable.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPositionDiscontinuity() {
    }


    @Override
    public void deliverImage(Bitmap bitmap) {
        mBinding.playerView.setDefaultArtwork(bitmap);
    }

    @Override
    public void error(String error) {
        Log.e("Image down load error", error);
    }
}
