/*
 * Copyright (C) 2021 - Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.android.samples.getimephoneticname;

/* FILE: EventHighlighter.java
 * PURPOSE: Timer-based UI highlighter.  Highlights a view by changing its background color
 *          according to one of two available color sequences (gradations).
 *
 */

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EventHighlighter {
    public EventHighlighter(EditText etResult) {
        this.etResult = etResult;
        defaultColor = etResult.getBackground();
    }

    public void restartBlue() {
        intRestart(blueSequence);
    }
    public void restartRed() {
        intRestart(redSequence);
    }


    private static final int[] redSequence = new int[]{
            Color.rgb(0xf6, 0xe3, 0xff),
            Color.rgb(0xf6, 0xe3, 0xff),
            Color.rgb(0xf6, 0xe3, 0xff),
            Color.rgb(0xf1, 0xd4, 0xff),
            Color.rgb(0xec, 0xc6, 0xff),
            Color.rgb(0xcb, 0xb8, 0xff),
            Color.rgb(0xe3, 0xaa, 0xff),
            Color.rgb(0xde, 0x9c, 0xff),
            Color.rgb(0xd9, 0x8e, 0xff),
            Color.rgb(0xd4, 0x80, 0xff),
            Color.rgb(0xd0, 0x71, 0xff),
            Color.rgb(0xcb, 0x63, 0xff),
    };
    private static final int[] blueSequence = new int[]{
            Color.rgb(0xf4, 0xfa, 0xff),
            Color.rgb(0xea, 0xf5, 0xff),
            Color.rgb(0xe0, 0xf0, 0xff),
            Color.rgb(0xd5, 0xeb, 0xff),
            Color.rgb(0xca, 0xe6, 0xff),
            Color.rgb(0xc0, 0xe1, 0xff),
            Color.rgb(0xb5, 0xdd, 0xff),
            Color.rgb(0xab, 0xd8, 0xff),
            Color.rgb(0xa1, 0xd3, 0xff),
            Color.rgb(0x96, 0xce, 0xff),
            Color.rgb(0x8b, 0xc9, 0xff),
            Color.rgb(0x81, 0xc4, 0xff),
    };

    private int[] triggerSequence = blueSequence;

    private static final int triggerColorDurationMs = 100;
    private static final int delayForInitialColorTrigger = triggerColorDurationMs * 10;
    private int triggerState = -1;
    private EditText etResult;
    private Drawable defaultColor;
    private Handler timerHandler = new Handler();

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (triggerState >= 0) {
                etResult.setBackgroundColor(triggerSequence[triggerState]);
                --triggerState;
                timerHandler.postDelayed(this, triggerColorDurationMs);
            } else {
                etResult.setBackground(defaultColor);
            }
        }
    };
    private void intRestart(int[] newSequence) {
        this.triggerSequence = newSequence;
        if (triggerState >= 0) {
            // already running, cancel all delayed callbacks to react immediately
            timerHandler.removeCallbacksAndMessages(null);
        }
        triggerState = triggerSequence.length - 1;
        etResult.setBackgroundColor(triggerSequence[triggerState]);
        timerHandler.postDelayed(timerRunnable, delayForInitialColorTrigger );
    }

}
